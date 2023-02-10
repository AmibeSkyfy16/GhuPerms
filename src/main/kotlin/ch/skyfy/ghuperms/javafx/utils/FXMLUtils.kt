package ch.skyfy.ghuperms.javafx.utils

import ch.skyfy.ghuperms.GhuPermsMod
import ch.skyfy.ghuperms.GhuPermsMod.Companion.MOD_CONTAINER
import javafx.fxml.FXMLLoader
import net.fabricmc.loader.api.FabricLoader
import java.io.IOException
import java.net.URL
import java.nio.file.Path
import java.nio.file.Paths


object FXMLUtils {

    fun <R> loadFXML(view: R) {
//        val p = view!!::class.java.classLoader.getResource("javafx/ui/fxml/${view!!::class.java.simpleName}.fxml")
//        loadFXML(p, view)
        loadFXML(MOD_CONTAINER.findPath("javafx/ui/fxml/${view!!::class.java.simpleName}.fxml").get(), view)
    }

    fun <R> loadFXML(fxmlName: String, view: R) {
//        loadFXML(FabricLoader.getInstance().getModContainer(GhuPermsMod.MOD_ID).get().findPath("javafx/ui/fxml/RootView.fxml").get(), view)
//        val p = view!!::class.java.classLoader.getResource("javafx/ui/fxml/$fxmlName")
//        loadFXML(p, view)
//        loadFXML(FabricLoader.getInstance().getModContainer(GhuPermsMod.MOD_ID).get().findPath("javafx/ui/fxml/${view!!::class.java.simpleName}.fxml").get(), view)
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun <R> loadFXML(path: Path, view: R) {
        try {
            object : FXMLLoader(path.toUri().toURL()) {
                init {
                    setController(view)
                    setRoot(view)
                }
            }.load()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    fun <R> loadFXML(url: URL, view: R) {
        try {
            object : FXMLLoader(url) {
                init {
                    setController(view)
                    setRoot(view)
                }
            }.load()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}