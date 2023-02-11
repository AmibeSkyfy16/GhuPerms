package ch.skyfy.ghuperms.commands

import ch.skyfy.ghuperms.config.Configs
import ch.skyfy.ghuperms.data.CommandPermission
import ch.skyfy.ghuperms.data.Group
import ch.skyfy.ghuperms.data.Groups
import ch.skyfy.ghuperms.utils.ModsUtils.getPlayerNameWithUUID
import ch.skyfy.ghuperms.utils.ModsUtils.sendCommandTreeToAll
import ch.skyfy.json5configlib.ConfigManager
import ch.skyfy.json5configlib.updateIterable
import ch.skyfy.json5configlib.updateIterableNested
import com.mojang.brigadier.Command
import com.mojang.brigadier.Command.SINGLE_SUCCESS
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.Message
import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.arguments.BoolArgumentType.getBool
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType.getInteger
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.arguments.StringArgumentType.getString
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.CommandSource.suggestMatching
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class PermissionsCmd : Command<ServerCommandSource> {

    companion object {
        fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
            val cmd = literal("ghuperms").requires { source -> source.hasPermissionLevel(4) }
                .then(
                    literal("permissions")
                        .then(
                            literal("saveAllPermissions").executes(Command {
                                if (it.source.player != null) sendCommandTreeToAll(it.source.player!!.server.playerManager)
                                ConfigManager.save(Configs.PERMISSIONS_DATA)
                                it.source.player?.sendMessage(Text.literal("Saved ! You can find a list of all permissions in the all-permissions.json5 file").setStyle(Style.EMPTY.withColor(Formatting.GOLD)))
                                return@Command 1
                            })
                        ).then(
                            literal("add")
                                .then(
                                    argument("permissionName", StringArgumentType.string())
                                        .suggests { _, suggestionsBuilder -> suggestMatching(Configs.PERMISSIONS_DATA.serializableData.list.map { "\"$it\"" }, suggestionsBuilder) }
                                        .then(
                                            argument("value", BoolArgumentType.bool())
                                                .suggests { _, suggestionsBuilder -> suggestMatching(listOf(true, false), suggestionsBuilder, { t -> t.toString() }, { _ -> Message { "" } }) }
                                                .then(
                                                    argument("groupName", StringArgumentType.string())
                                                        .suggests { _, suggestionsBuilder -> suggestMatching(Configs.GROUPS.serializableData.list.map { it.name }, suggestionsBuilder) }
                                                        .executes(AddPermissionToGroupCommand())
                                                )
                                        )
                                )
                        ).then(
                            literal("remove")
                                .then(
                                    argument("permissionName", StringArgumentType.string())
                                        .suggests { _, suggestionsBuilder -> suggestMatching(Configs.PERMISSIONS_DATA.serializableData.list, suggestionsBuilder) }
                                        .then(
                                            argument("groupName", StringArgumentType.string())
                                                .suggests { _, suggestionsBuilder -> suggestMatching(Configs.GROUPS.serializableData.list.map { it.name }, suggestionsBuilder) }
                                                .executes(RemovePermissionFromGroupCommand())
                                        )
                                )
                        )
                ).then(
                    literal("groups")
                        .then(
                            literal("add")
                                .then(
                                    literal("player")
                                        .then(
                                            argument("playerName", StringArgumentType.string()).suggests { context, sb -> suggestMatching(context.source.server.playerNames.toList(), sb) }
                                                .then(
                                                    argument("groupName", StringArgumentType.string())
                                                        .suggests { context, suggestionsBuilder ->
                                                            val playerName = getString(context, "playerName")
                                                            val player = context.source.server.playerManager.getPlayer(playerName)
                                                            val playerNameWithUUID = if (player != null) getPlayerNameWithUUID(player) else ""
                                                            suggestMatching(Configs.GROUPS.serializableData.list.mapNotNull { group ->
                                                                if (group.members.none { it == playerNameWithUUID })
                                                                    group.name
                                                                else null
                                                            }, suggestionsBuilder)
                                                        }
                                                        .executes(AddPlayerToGroupCommand())
                                                )
                                        )
                                )
                        ).then(
                            literal("remove")
                                .then(
                                    literal("player")
                                        .then(
                                            argument("playerName", StringArgumentType.string()).suggests { context, sb -> suggestMatching(context.source.server.playerNames.toList(), sb) }
                                                .then(
                                                    argument("groupName", StringArgumentType.string())
                                                        .suggests { context, suggestionsBuilder ->
                                                            val playerName = getString(context, "playerName")
                                                            val player = context.source.server.playerManager.getPlayer(playerName)
                                                            val playerNameWithUUID = if (player != null) getPlayerNameWithUUID(player) else ""
                                                            suggestMatching(Configs.GROUPS.serializableData.list.mapNotNull { group ->
                                                                if (group.members.any { it == playerNameWithUUID })
                                                                    group.name
                                                                else null
                                                            }, suggestionsBuilder)
                                                        }
                                                        .executes(RemovePlayerFromGroupCommand())
                                                )
                                        )
                                )
                        )
                )
                .then(
                    literal("create")
                        .then(
                            literal("group")
                                .then(
                                    argument("groupName", StringArgumentType.string()).then(
                                        argument("weight", IntegerArgumentType.integer()).executes(CreateGroupCommand())
                                    )
                                )
                        )
                )
                .then(
                    literal("remove")
                        .then(
                            literal("group")
                                .then(
                                    argument("groupName", StringArgumentType.string()).suggests { _, sb -> suggestMatching(Configs.GROUPS.serializableData.list.map { it.name }, sb) }.executes(RemoveGroupCommand())
                                )
                        )
                )
                .then(
                    literal("show")
                        .then(
                            literal("group").then(
                                argument("groupName", StringArgumentType.string()).suggests { _, sb -> suggestMatching(Configs.GROUPS.serializableData.list.map { it.name }, sb) }
                                    .executes(Command {context ->
                                        val groupName = getString(context, "groupName")
                                        Configs.GROUPS.serializableData.list.firstOrNull { it.name == groupName }?.let {
//                                            context.source.sendMessage(Text.literal("Here is some infos about group $groupName").setStyle(Style.EMPTY.withColor(Formatting.GOLD)))
                                             // TODO find a good way to format permissions and members and have a nice message
                                        }
                                        return@Command SINGLE_SUCCESS
                                    })
                            )
                        )
                )
            dispatcher.register(cmd)
        }

//        @Suppress("UNUSED_PARAMETER")
//        private fun <S : ServerCommandSource> getAllPermissions(commandContext: CommandContext<S>, suggestionsBuilder: SuggestionsBuilder): CompletableFuture<Suggestions> {
//            return suggestMatching(Configs.PERMISSIONS_DATA.serializableData.list, suggestionsBuilder)
//        }
    }

    class AddPermissionToGroupCommand : Command<ServerCommandSource> {
        override fun run(context: CommandContext<ServerCommandSource>): Int {
            val permissionName = getString(context, "permissionName")
            val value = getBool(context, "value")
            val groupName = getString(context, "groupName")
            Configs.GROUPS.serializableData.list.firstOrNull { it.name == groupName }?.let { group ->
                Configs.GROUPS.updateIterableNested(Group::permissions, group.permissions) { set ->
                    if (set.any { it.name == permissionName }) {
                        context.source.sendMessage(Text.literal("This permission is already in group $groupName").setStyle(Style.EMPTY.withColor(Formatting.RED)))
                    } else {
                        set.add(CommandPermission(permissionName, value))
                        if (context.source.player != null) sendCommandTreeToAll(context.source.player!!.server.playerManager)
                        context.source.sendMessage(Text.literal("Permission -> $permissionName added to group -> $groupName").setStyle(Style.EMPTY.withColor(Formatting.GREEN)))
                    }
                }
            }
            return SINGLE_SUCCESS
        }
    }

    class RemovePermissionFromGroupCommand : Command<ServerCommandSource> {
        override fun run(context: CommandContext<ServerCommandSource>): Int {
            val permissionName = getString(context, "permissionName")
            val groupName = getString(context, "groupName")
            Configs.GROUPS.serializableData.list.firstOrNull { it.name == groupName }?.let { group ->
                Configs.GROUPS.updateIterableNested(Group::permissions, group.permissions) { set ->
                    if (set.removeIf { it.name == permissionName }) {
                        if (context.source.player != null) sendCommandTreeToAll(context.source.player!!.server.playerManager)
                        context.source.sendMessage(Text.literal("Permission -> $permissionName has been remove from group -> $groupName").setStyle(Style.EMPTY.withColor(Formatting.GREEN)))
                    } else context.source.sendMessage(Text.literal("Permission -> $permissionName has not been found in group -> $groupName").setStyle(Style.EMPTY.withColor(Formatting.RED)))
                }
            }
            return SINGLE_SUCCESS
        }
    }

    class AddPlayerToGroupCommand : Command<ServerCommandSource> {
        override fun run(context: CommandContext<ServerCommandSource>): Int {
            val playerName = getString(context, "playerName")
            val groupName = getString(context, "groupName")
            val player = context.source.server.playerManager.getPlayer(playerName)
            val playerNameWithUUID = if (player != null) getPlayerNameWithUUID(player) else ""
            Configs.GROUPS.serializableData.list.firstOrNull { it.name == groupName }?.let { group ->
                Configs.GROUPS.updateIterableNested(Group::members, group.members) { members ->
                    if (members.none { it == playerNameWithUUID }) {
                        if (context.source.player != null) sendCommandTreeToAll(context.source.player!!.server.playerManager)
                        members.add(playerNameWithUUID)
                        context.source.sendMessage(Text.literal("Player -> $playerName added to group -> $groupName").setStyle(Style.EMPTY.withColor(Formatting.GREEN)))
                    } else context.source.sendMessage(Text.literal("Player -> $playerName is already in group -> $groupName").setStyle(Style.EMPTY.withColor(Formatting.RED)))
                }
            }
            return SINGLE_SUCCESS
        }
    }

    class RemovePlayerFromGroupCommand : Command<ServerCommandSource> {
        override fun run(context: CommandContext<ServerCommandSource>): Int {
            val playerName = getString(context, "playerName")
            val groupName = getString(context, "groupName")
            val player = context.source.server.playerManager.getPlayer(playerName)
            val playerNameWithUUID = if (player != null) getPlayerNameWithUUID(player) else ""
            Configs.GROUPS.serializableData.list.firstOrNull { it.name == groupName }?.let { group ->
                Configs.GROUPS.updateIterableNested(Group::members, group.members) { members ->
                    if (members.removeIf { it == playerNameWithUUID }) {
                        if (context.source.player != null) sendCommandTreeToAll(context.source.player!!.server.playerManager)
                        context.source.sendMessage(Text.literal("Player -> $playerName has been removed from group -> $groupName").setStyle(Style.EMPTY.withColor(Formatting.GREEN)))
                    } else context.source.sendMessage(Text.literal("Player -> $playerName isn't already in group -> $groupName").setStyle(Style.EMPTY.withColor(Formatting.RED)))
                }
            }
            return SINGLE_SUCCESS
        }
    }

    class CreateGroupCommand : Command<ServerCommandSource> {
        override fun run(context: CommandContext<ServerCommandSource>): Int {
            val groupName = getString(context, "groupName")
            val weight = getInteger(context, "weight")
            Configs.GROUPS.updateIterable(Groups::list) { groups ->
                if (groups.none { it.name == groupName }) {
                    groups.add(Group(groupName, weight, mutableSetOf(), mutableSetOf()))
                    context.source.sendMessage(Text.literal("Group -> $groupName has been added to the list of groups").setStyle(Style.EMPTY.withColor(Formatting.GREEN)))
                } else context.source.sendMessage(Text.literal("Group -> $groupName already exist !").setStyle(Style.EMPTY.withColor(Formatting.RED)))
            }
            return SINGLE_SUCCESS
        }
    }

    class RemoveGroupCommand : Command<ServerCommandSource> {
        override fun run(context: CommandContext<ServerCommandSource>): Int {
            val groupName = getString(context, "groupName")
            Configs.GROUPS.updateIterable(Groups::list) { groups ->
                if (groups.removeIf { it.name == groupName }) {
                    if (context.source.player != null) sendCommandTreeToAll(context.source.player!!.server.playerManager)
                    context.source.sendMessage(Text.literal("Group -> $groupName has been remove from the list of groups").setStyle(Style.EMPTY.withColor(Formatting.GREEN)))
                } else context.source.sendMessage(Text.literal("Group -> $groupName isn't already exist !").setStyle(Style.EMPTY.withColor(Formatting.RED)))
            }
            return SINGLE_SUCCESS
        }
    }

    override fun run(context: CommandContext<ServerCommandSource>): Int {


        return SINGLE_SUCCESS
    }

}