package ch.skyfy.ghuperms.prelaunch

import ch.skyfy.ghuperms.prelaunch.callback.CommandDispatcherOnRegisterCallback
import ch.skyfy.ghuperms.prelaunch.callback.LiteralArgumentBuilderInitCallback
import ch.skyfy.ghuperms.prelaunch.mixin.LiteralAccessor
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint
import net.fabricmc.loader.impl.FabricLoaderImpl
import net.fabricmc.loader.impl.ModContainerImpl
import net.fabricmc.loader.impl.metadata.EntrypointMetadata
import net.minecraft.util.TypedActionResult
import java.lang.StackWalker.StackFrame
import java.lang.reflect.Method
import java.util.concurrent.atomic.AtomicBoolean
import java.util.function.Consumer

class GhuPermsPreLauncher : PreLaunchEntrypoint {

    companion object {
        val map2 = mutableMapOf<String, String>()



        var previous: LiteralArgumentBuilder<*>? = null

        fun removeUppercase(str: String) {
            val regex = Regex("[A-Z]")
            regex.replace(str, "")
        }
    }


    override fun onPreLaunch() {

        CommandDispatcherOnRegisterCallback.EVENT.register(CommandDispatcherOnRegisterCallback { literal ->
            var newLiteral = literal.literal

            val foundCommandDispatcher = AtomicBoolean(false)
            val foundCommandRegistrationCallback = AtomicBoolean(false)
            val stackFrames = mutableSetOf<StackFrame>()
            StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).forEach { stackFrame ->
                stackFrames.add(stackFrame)

                if(map2.contains(literal.literal)){
                    newLiteral = map2[literal.literal]
                    return@forEach
                }

                if (foundCommandDispatcher.get() && stackFrame.declaringClass.simpleName != "CommandDispatcher") {
                    val packagesNames = stackFrame.declaringClass.canonicalName.split(".")

                    if(literal.literal.contains("ghuperms")){
                        1
                    }

                    if (packagesNames.size >= 2 && packagesNames[0] == "net" && packagesNames[1] == "minecraft") {
                        newLiteral = "mc:${literal.literal}"
                        map2.putIfAbsent(literal.literal, newLiteral)
                        return@forEach
                    }

                    FabricLoaderImpl.INSTANCE.modsInternal.forEach(Consumer { modContainer: ModContainerImpl ->
                        for (entrypointKey in modContainer.metadata.entrypointKeys) {
                            val entryPoint = modContainer.metadata.getEntrypoints(entrypointKey)
                            entryPoint.forEach(Consumer { entrypointMetadata: EntrypointMetadata ->
                                val splits = entrypointMetadata.value.split(".")
                                var firstThreeTimesMatching = 0
                                for ((index, packageName) in packagesNames.withIndex()) {
                                    if (firstThreeTimesMatching == 3)
                                        break
                                    if (index > splits.size - 1)
                                        break
                                    if (packageName == splits[index])
                                        firstThreeTimesMatching++
                                }
                                if (firstThreeTimesMatching == 3) {
                                    val newLiteral2 = modContainer.metadata.id + ":" + literal.literal
                                    newLiteral = newLiteral2
                                    map2.putIfAbsent(literal.literal, newLiteral2)
                                }
                            })
                        }
                    })
                }

                if (stackFrame.declaringClass.simpleName.equals("CommandDispatcher", ignoreCase = true)) foundCommandDispatcher.set(true)
                if (stackFrame.declaringClass.simpleName.equals("CommandRegistrationCallback", ignoreCase = true)) foundCommandRegistrationCallback.set(true)
            }

            (literal as LiteralAccessor).setLiteral(newLiteral)

            return@CommandDispatcherOnRegisterCallback TypedActionResult.pass("")
        })










        LiteralArgumentBuilderInitCallback.EVENT.register(LiteralArgumentBuilderInitCallback { literal, instance ->
            if (0 == 0) return@LiteralArgumentBuilderInitCallback TypedActionResult.pass(literal)
            var newLiteral = literal


//            instance.arguments.forEach {
//                if(it.name == previous?.literal){
//                    list.add(Test(previous, Test(instance, null)))
//                }
//            }


//            if (map2.contains(literal)) {
//                return@LiteralArgumentBuilderInitCallback TypedActionResult.pass(map2[literal])
//            }

            var wasAChild = false
            val stackFrames = mutableSetOf<StackFrame>()
            StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).forEach { stackFrame ->
                stackFrames.add(stackFrame)


                if (previous != null) {
                    if (map[stackFrame.declaringClass] != null) {
                        previous!!.arguments.forEach s@{
                            if (it.name == instance.literal) {
                                return@forEach
                            }
                        }
                    }
                }

                val potentialMethod = mutableSetOf<Method>()

                // DEBUG -------
                try {
                    if (stackFrame.declaringClass.canonicalName.contains("ch.skyfy.manymanycommands") && literal == "tpahere") {
                        1
                    }
                } catch (e: Exception) {
                }
                // DEBUG -------

                var classicRegisterMethodFound = false
                try {
                    val method = stackFrame.declaringClass.getDeclaredMethod(stackFrame.methodName, CommandDispatcher::class.java)
                    potentialMethod.add(method)
                    classicRegisterMethodFound = true
                } catch (_: Exception) {
                }

                if (!classicRegisterMethodFound) {

                    // DEBUG -------
                    if (literal == "tpahere") {
                        1
                    }
                    // DEBUG -------

                    try {
                        val array: Array<out Class<*>> = stackFrame.methodType.parameterArray()
                        if (array.any { it.simpleName == "CommandDispatcher" }) {
                            val method = stackFrame.declaringClass.getDeclaredMethod(stackFrame.methodName, *array)
                            potentialMethod.add(method)
                        }
                    } catch (_: Exception) {
                    }
                }

                if (potentialMethod.size >= 1) {
                    val first = potentialMethod.elementAt(0)
//                    if (map[stackFrame.declaringClass] != null) {
//                        wasAChild = true
//                        return@forEach
//                    }
                    map.putIfAbsent(stackFrame.declaringClass, literal)

                    if (stackFrame.declaringClass.canonicalName == null) return@forEach

                    val packagesNames = stackFrame.declaringClass.canonicalName.split(".")

                    if (packagesNames.size >= 2 && packagesNames[0] == "net" && packagesNames[1] == "minecraft") {
                        newLiteral = "mc:$literal"
                        map2.putIfAbsent(literal, newLiteral)
                        map3.put(instance, Pair(literal, newLiteral))
                        return@forEach
                    }

                    FabricLoaderImpl.INSTANCE.modsInternal.forEach(Consumer { modContainer: ModContainerImpl ->
                        for (entrypointKey in modContainer.metadata.entrypointKeys) {
                            val entryPoint = modContainer.metadata.getEntrypoints(entrypointKey)
                            entryPoint.forEach(Consumer { entrypointMetadata: EntrypointMetadata ->
                                val splits = entrypointMetadata.value.split(".")
                                var firstThreeTimesMatching = 0
                                for ((index, packageName) in packagesNames.withIndex()) {
                                    if (firstThreeTimesMatching == 3) break
                                    if (index > splits.size - 1) {
                                        println("breaking")
                                        break
                                    }
                                    if (packageName == splits[index]) {
                                        firstThreeTimesMatching++
                                    }
                                }
                                if (firstThreeTimesMatching == 3) {
                                    val newLiteral2 = modContainer.metadata.id + ":" + literal
                                    newLiteral = newLiteral2
                                    map2.putIfAbsent(literal, newLiteral2)
                                    map3.put(instance, Pair(literal, newLiteral))
                                }
                            })
                        }
                    })
                } else {

                    if (map2.contains(literal)) {

                        return@forEach
                    }

                }

                1
            }

            // DEBUG -------
            if (!wasAChild && literal == newLiteral) {
                1
            }
            // DEBUG -------

            previous = instance
            return@LiteralArgumentBuilderInitCallback TypedActionResult.pass(newLiteral)
        })
    }

}