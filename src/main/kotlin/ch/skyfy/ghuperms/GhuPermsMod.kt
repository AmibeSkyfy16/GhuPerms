package ch.skyfy.ghuperms

import ch.skyfy.ghuperms.callback.AddToOperatorsCallback
import ch.skyfy.ghuperms.callback.RemoveFromOperatorsCallback
import ch.skyfy.ghuperms.commands.PermissionsCmd
import ch.skyfy.ghuperms.commands.ReloadFilesCmd
import ch.skyfy.ghuperms.commands.StartGUICmd
import ch.skyfy.ghuperms.config.Configs
import ch.skyfy.ghuperms.data.CommandPermission
import ch.skyfy.ghuperms.data.Group
import ch.skyfy.ghuperms.data.Groups
import ch.skyfy.ghuperms.prelaunch.GhuPermsPreLauncher
import ch.skyfy.ghuperms.prelaunch.GhuPermsPreLauncher.Companion.MOD_ID
import ch.skyfy.ghuperms.prelaunch.config.PreLaunchConfigs
import ch.skyfy.ghuperms.utils.ModsUtils.getPlayerNameFromNameWithUUID
import ch.skyfy.ghuperms.utils.ModsUtils.getPlayerNameWithUUID
import ch.skyfy.ghuperms.utils.ModsUtils.sendCommandTreeToAll
import ch.skyfy.json5configlib.ConfigManager
import ch.skyfy.json5configlib.SetOperation
import ch.skyfy.json5configlib.updateIterableNested
import net.fabricmc.api.DedicatedServerModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.server.MinecraftServer
import kotlin.io.path.*

@Suppress("MemberVisibilityCanBePrivate")
class GhuPermsMod : DedicatedServerModInitializer {

    companion object {
//        const val MOD_ID: String = "ghuperms"
//        val CONFIG_DIRECTORY: Path = FabricLoader.getInstance().configDir.resolve(MOD_ID)
//        val LOGGER: Logger = LogManager.getLogger(GhuPermsMod::class.java)

        const val COMMAND_PERMISSION_PREFIX = "command"
        val PLAYERS_NAMES_AND_UUIDS = mutableMapOf<String, String>()
        val MOD_CONTAINER = FabricLoader.getInstance().getModContainer(MOD_ID).get()


        fun convertNameSpacedCommandToPermission(command: String): String? {
            val sb = StringBuilder(COMMAND_PERMISSION_PREFIX)
            if (command.contains(":")) {
                val splits = command.split(":")
                if (splits.size >= 2) return sb.replace(0, 0, splits[0] + ":").append(".").append(splits[1]).toString()
            }
            return null
        }
    }

    init {
        ConfigManager.loadConfigs(arrayOf(Configs::class.java))
        CommandNodeMixinImpl()
    }

    override fun onInitializeServer() {
        registerCommands()

        ServerLifecycleEvents.SERVER_STARTED.register{
            if (PreLaunchConfigs.COMMANDS_ALIASES.serializableData.enableNameSpacingCommands) {

                // We will have to rename permission to match with command namespacing

                Configs.GROUPS.serializableData.list.forEach { group ->
                    Configs.GROUPS.updateIterableNested(Group::permissions, group.permissions) { permissions ->
                        val updatedPermissionsToAdd = mutableSetOf<CommandPermission>()
                        val iterator = permissions.iterator()
                        while (iterator.hasNext()) {
                            val next = iterator.next()

                            // Check if matching with command namespacing
                            if (!next.name.contains(":")) {
                                val commandName = next.name.substringAfterLast("$COMMAND_PERMISSION_PREFIX.")
                                GhuPermsPreLauncher.namespacedCommands[commandName]?.let {
                                    val nameSpacedPermission = convertNameSpacedCommandToPermission(it.first)

                                    if(nameSpacedPermission != null && nameSpacedPermission == it.first){
                                        updatedPermissionsToAdd.add(CommandPermission("any:$nameSpacedPermission", next.value))
                                        iterator.remove()
                                    }

                                    if (nameSpacedPermission != null) {
                                        updatedPermissionsToAdd.add(CommandPermission(nameSpacedPermission, next.value))
                                        iterator.remove()
                                    }
                                }
                            }
                        }
                        permissions.addAll(updatedPermissionsToAdd)
                    }
                }
            } else {

                // We will have to rename permission to match with command without namespacing
                Configs.GROUPS.serializableData.list.forEach { group ->
                    Configs.GROUPS.updateIterableNested(Group::permissions, group.permissions) { permissions ->
                        val updatedPermissionsToAdd = mutableSetOf<CommandPermission>()
                        val iterator = permissions.iterator()
                        while (iterator.hasNext()){
                            val next = iterator.next()
                            if(next.name.contains(":")){
                                val commandName = next.name.substringAfterLast(":")
                                updatedPermissionsToAdd.add(CommandPermission(commandName, next.value))
                                iterator.remove()
                            }
                        }
                        permissions.addAll(updatedPermissionsToAdd)
                    }
                }

            }
        }

        // User that use the PermissionManagerApp will cause a modification of the list variable when pressing the APPLY button
        Configs.GROUPS.registerOnUpdateOn(Groups::list) {
            if (it is SetOperation<*, *>) {
                updatePlayerNameInConfig()
                @Suppress("DEPRECATION")
                sendCommandTreeToAll((FabricLoader.getInstance().gameInstance as MinecraftServer).playerManager)
            }
        }

        AddToOperatorsCallback.EVENT.register { profile ->
            val nameWithUUID: String = profile.name + "#" + profile.id.toString()
            Configs.GROUPS.serializableData.list.firstOrNull { it.name == "OP" }?.let { group ->
                Configs.GROUPS.updateIterableNested(Group::members, group.members) {
                    if (!it.contains(nameWithUUID)) it.add(nameWithUUID)
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
            val playerName = handler.player.name.string
            val playerUUID = handler.player.uuidAsString
            PLAYERS_NAMES_AND_UUIDS.putIfAbsent(playerName, playerUUID)

            // When using the GUI to add members, we just add their name,
            // except that all the code is based on value keys in the format <playerName>#<UUID>,
            // so when a player comes back, we have to change the member name
            Configs.GROUPS.serializableData.list.forEach { group ->
                Configs.GROUPS.updateIterableNested(Group::members, group.members) { members ->
                    if (group.name == "DEFAULT") {
                        val name = getPlayerNameWithUUID(handler.player)
                        if (!members.contains(name)) members.add(name)
                    }

                    members.firstOrNull { getPlayerNameFromNameWithUUID(it) == playerName }?.let {
                        if (it.split("#").size == 1) {
                            val nameWithUUID = "$it#${PLAYERS_NAMES_AND_UUIDS[it]}"
                            if (members.contains(it)) {
                                members.remove(it)
                                members.add(nameWithUUID)
                            }
                        }
                    }
                }
            }
        }

        ServerPlayConnectionEvents.DISCONNECT.register { handler, _ ->
            PLAYERS_NAMES_AND_UUIDS.remove(handler.player.name.string)
        }
    }

    private fun registerCommands() = CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
        ReloadFilesCmd.register(dispatcher)
        PermissionsCmd.register(dispatcher)
        StartGUICmd.register(dispatcher)
    }

    private fun updatePlayerNameInConfig() {
        // When using the GUI to add members, we just add their name,
        // except that all the code is based on value keys in the format <playerName>#<UUID>,
        // so when a player comes back, we have to change the member name
        Configs.GROUPS.serializableData.list.forEach { group ->
            Configs.GROUPS.updateIterableNested(Group::members, group.members) { members ->
                val nameWithUUIDToAdd = mutableSetOf<String>()
                val iterator = members.iterator()
                while (iterator.hasNext()) {
                    val next = iterator.next()
                    if (next.split("#").size == 1) {
                        PLAYERS_NAMES_AND_UUIDS.firstNotNullOfOrNull { if (it.key == next) it else null }?.let {
                            if (members.contains(it.key)) {
                                iterator.remove()
                                nameWithUUIDToAdd.add("${it.key}#${it.value}")
                            }
                        }
                    }
                }
                members.addAll(nameWithUUIDToAdd)
            }
        }
    }

}


