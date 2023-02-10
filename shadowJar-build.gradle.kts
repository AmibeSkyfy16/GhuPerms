@file:Suppress("GradlePackageVersionRange")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.utils.extendsFrom


plugins {
    id("fabric-loom") version "1.1-SNAPSHOT"
    id("org.jetbrains.kotlin.jvm") version "1.8.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.0"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    idea
}

base {
    archivesName.set(properties["archives_name"].toString())
    group = property("maven_group")!!
    version = property("mod_version")!!
}

repositories {
    mavenCentral()
    google()
    maven("https://repo.repsy.io/mvn/amibeskyfy16/repo")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven(url = "https://dl.bintray.com/animeshz/maven")
    maven("https://packages.jetbrains.team/maven/p/skija/maven")
}

dependencies {
    minecraft("com.mojang:minecraft:${properties["minecraft_version"]}")
    mappings("net.fabricmc:yarn:${properties["yarn_mappings"]}:v2")

    modImplementation("net.fabricmc:fabric-loader:${properties["loader_version"]}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${properties["fabric_version"]}")
    modImplementation("net.fabricmc:fabric-language-kotlin:${properties["fabric_kotlin_version"]}")

    shadow("ch.skyfy.json5configlib:json5-config-lib:1.0.22")

//    shadow("org.openjfx:javafx:17.0.2:win")
    shadow("org.openjfx:javafx-base:17.0.2:win"){
//        exclude("org.openjfx")
    }
    shadow("org.openjfx:javafx-graphics:17.0.2:win"){
//        exclude("org.openjfx")
    }
//    shadow("org.openjfx:javafx-controls:17.0.2:win"){
//        exclude("org.openjfx")
//    }
//    shadow("org.openjfx:javafx-fxml:17.0.2:win"){
//        exclude("org.openjfx")
//    }
//    shadow("org.openjfx:javafx-swing:17.0.2:win"){
//        exclude("org.openjfx")
//    }
//    shadow("org.openjfx:javafx-media:17.0.2:win"){
//        exclude("org.openjfx")
//    }
//    shadow("org.openjfx:javafx-web:17.0.2:win"){
//        exclude("org.openjfx")
//    }

    testImplementation("org.jetbrains.kotlin:kotlin-test:1.8.0")
}

configurations {
    shadow.get()
    modImplementation.get().extendsFrom(shadow.get())
}


tasks {

    val javaVersion = JavaVersion.VERSION_17

    processResources {
        inputs.property("version", project.version)
        filteringCharset = "UTF-8"
        filesMatching("fabric.mod.json") {
            expand(mutableMapOf("version" to project.version))
        }
    }

    loom {

    }

    shadowJar {
        classifier = "dev"
        configurations = listOf(project.configurations.shadow.get())
        relocate("ch.skyfy.ghuperms", "ch.skyfy.ghuperms_02")
    }

    remapJar {
        dependsOn(shadowJar.get())
        inputFile.set(shadowJar.get().archiveFile)
        exclude("module-info.class")
    }

    javafx {
        version = "17"
        modules("javafx.controls", "javafx.fxml")
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(javaVersion.toString()))
            vendor.set(JvmVendorSpec.BELLSOFT)
        }
        modularity.inferModulePath.set(false)
        withSourcesJar()
        withJavadocJar()
    }

    named<Wrapper>("wrapper") {
        gradleVersion = "7.6"
        distributionType = Wrapper.DistributionType.BIN
    }

    named<KotlinCompile>("compileKotlin") {
        kotlinOptions.jvmTarget = javaVersion.toString()
    }

    named<JavaCompile>("compileJava") {
        options.encoding = "UTF-8"
        options.release.set(javaVersion.toString().toInt())
    }

    named<Jar>("jar") {
        from("LICENSE") {
            rename { "${it}_${base.archivesName}" }
        }
    }

    named<Test>("test") {
        useJUnitPlatform()

        testLogging {
            outputs.upToDateWhen { false } // When the build task is executed, stderr-stdout of test classes will be show
            showStandardStreams = true
        }
    }

    val copyJarToTestServer = register("copyJarToTestServer") {
        println("copy to server")
        copyFile("build/libs/GhuPerms-${project.properties["mod_version"]}.jar", project.property("testServerModsFolder") as String)
    }

    build { doLast { copyJarToTestServer.get() } }

}

fun copyFile(src: String, dest: String) = copy { from(src);into(dest) }