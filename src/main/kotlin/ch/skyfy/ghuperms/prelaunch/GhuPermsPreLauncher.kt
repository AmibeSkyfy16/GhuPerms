package ch.skyfy.ghuperms.prelaunch

import ch.skyfy.ghuperms.prelaunch.callback.CommandDispatcherOnRegisterCallback
import ch.skyfy.ghuperms.prelaunch.config.PreLaunchConfigs
import ch.skyfy.ghuperms.prelaunch.mixin.LiteralAccessor
import ch.skyfy.json5configlib.ConfigManager
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint
import net.fabricmc.loader.impl.FabricLoaderImpl
import net.fabricmc.loader.impl.ModContainerImpl
import net.fabricmc.loader.impl.metadata.EntrypointMetadata
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource
import java.lang.StackWalker.StackFrame
import java.nio.file.Path
import java.util.concurrent.atomic.AtomicBoolean
import java.util.function.Consumer

class GhuPermsPreLauncher : PreLaunchEntrypoint {

    companion object {
        private const val MOD_ID: String = "ghuperms"
        val CONFIG_DIRECTORY: Path = FabricLoader.getInstance().configDir.resolve(MOD_ID)

        val renamedCommands = mutableMapOf<String, Pair<String, LiteralArgumentBuilder<*>>>()
    }

    init {
        ConfigManager.loadConfigs(arrayOf(PreLaunchConfigs::class.java))
    }

    override fun onPreLaunch() {


        ServerLifecycleEvents.SERVER_STARTED.register { server ->
            if(!PreLaunchConfigs.COMMANDS_ALIASES.serializableData.enableNameSpacingCommands)
                return@register

            PreLaunchConfigs.COMMANDS_ALIASES.serializableData.list.forEach { alias ->
                renamedCommands[alias.alias]?.let {
                    println("creating an alias called ${alias.alias} for command $it")
                    server.commandManager.dispatcher.register(literal(alias.alias).redirect(it.second.build() as LiteralCommandNode<ServerCommandSource>))
                }
            }
        }

        CommandDispatcherOnRegisterCallback.EVENT.register(CommandDispatcherOnRegisterCallback { literal ->
            if(!PreLaunchConfigs.COMMANDS_ALIASES.serializableData.enableNameSpacingCommands)
                return@CommandDispatcherOnRegisterCallback

            var newLiteral = literal.literal

            PreLaunchConfigs.COMMANDS_ALIASES.serializableData.list.forEach {
                if (renamedCommands.containsKey(literal.literal)) {
                    if (it.baseCommand == renamedCommands[literal.literal]?.first) {
                        println("Found a alias ${it.alias} for command ${it.baseCommand}")
                        return@CommandDispatcherOnRegisterCallback
                    }
                }
            }

            val foundCommandDispatcher = AtomicBoolean(false)
            val foundCommandRegistrationCallback = AtomicBoolean(false)
            val stackFrames = mutableSetOf<StackFrame>()
            StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).forEach { stackFrame ->
                stackFrames.add(stackFrame)

                if (renamedCommands.containsKey(literal.literal)) {
                    newLiteral = renamedCommands[literal.literal]?.first
                    return@forEach
                }

                if (foundCommandDispatcher.get() && stackFrame.declaringClass.simpleName != "CommandDispatcher") {
                    val packagesNames = stackFrame.declaringClass.canonicalName.split(".")

                    if (packagesNames.size >= 2 && packagesNames[0] == "net" && packagesNames[1] == "minecraft") {
                        newLiteral = "mc:${literal.literal}"
                        renamedCommands.putIfAbsent(literal.literal, Pair(newLiteral, literal))
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
                                    renamedCommands.putIfAbsent(literal.literal, Pair(newLiteral, literal))
                                }
                            })
                        }
                    })
                }

                if (stackFrame.declaringClass.simpleName.equals("CommandDispatcher", ignoreCase = true)) foundCommandDispatcher.set(true)
                if (stackFrame.declaringClass.simpleName.equals("CommandRegistrationCallback", ignoreCase = true)) foundCommandRegistrationCallback.set(true)
            }

            (literal as LiteralAccessor).setLiteral(newLiteral)
        })
    }

}