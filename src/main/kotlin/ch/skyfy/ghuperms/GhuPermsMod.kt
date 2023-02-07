package ch.skyfy.ghuperms

import ch.skyfy.ghuperms.callback.AddToOperatorsCallback
import ch.skyfy.ghuperms.callback.RemoveFromOperatorsCallback
import ch.skyfy.ghuperms.commands.ReloadFilesCmd
import ch.skyfy.ghuperms.config.Configs
import ch.skyfy.ghuperms.data.Group
import ch.skyfy.ghuperms.utils.ModsUtils.getPlayerNameWithUUID
import ch.skyfy.json5configlib.ConfigManager
import ch.skyfy.json5configlib.updateIterableNested
import net.fabricmc.api.DedicatedServerModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.fabricmc.loader.api.FabricLoader
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.nio.file.Path

@Suppress("MemberVisibilityCanBePrivate")
class GhuPermsMod : DedicatedServerModInitializer {

    companion object {
        private const val MOD_ID: String = "ghuperms"
        val CONFIG_DIRECTORY: Path = FabricLoader.getInstance().configDir.resolve(MOD_ID)
        val LOGGER: Logger = LogManager.getLogger(GhuPermsMod::class.java)
    }

    init {
        ConfigManager.loadConfigs(arrayOf(Configs::class.java))
        CommandNodeMixinImpl()
    }

    override fun onInitializeServer() {
        registerCommands()

        AddToOperatorsCallback.EVENT.register { profile ->
            val nameWithUUID: String = profile.name + "#" + profile.id.toString()
            Configs.GROUPS.serializableData.list.firstOrNull { it.name == "OP" }?.let { group ->
                Configs.GROUPS.updateIterableNested(Group::members, group.members) {
                    if(!it.contains(nameWithUUID))it.add(nameWithUUID)
                }
            }
        }

        RemoveFromOperatorsCallback.EVENT.register { profile ->
            val nameWithUUID: String = profile.name + "#" + profile.id.toString()
            Configs.GROUPS.serializableData.list.firstOrNull { it.name == "OP" }?.let { group ->
                Configs.GROUPS.updateIterableNested(Group::members, group.members) {
                    it.removeIf { str -> str == nameWithUUID }
                }
            }
        }

        ServerPlayConnectionEvents.JOIN.register { handler, _, _ ->
            Configs.GROUPS.serializableData.list.firstOrNull { group -> group.name == "DEFAULT" }?.let { group ->
                Configs.GROUPS.updateIterableNested(Group::members, group.members) { members ->
                    val name = getPlayerNameWithUUID(handler.player)
                    if (!members.contains(name)) members.add(name)
                }
            }
        }
    }

    private fun registerCommands() = CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
        ReloadFilesCmd.register(dispatcher)
    }

}


