package ch.skyfy.ghuperms.config

import ch.skyfy.ghuperms.GhuPermsMod
import ch.skyfy.ghuperms.data.DefaultGroups
import ch.skyfy.ghuperms.data.Groups
import ch.skyfy.json5configlib.ConfigData


object Configs {
    @JvmField
    val COMMAND_ALIASES = ConfigData.invoke<CommandAliasesConfig, DefaultCommandAliasesConfig>(GhuPermsMod.CONFIG_DIRECTORY.resolve("command-aliases.json5"), true)

    @JvmField
    val PERMISSIONS_DATA = ConfigData.invoke<PermissionsData, DefaultPermissionsData>(GhuPermsMod.CONFIG_DIRECTORY.resolve("all-permissions.json5"), false)

    @JvmField
    val GROUPS = ConfigData.invoke<Groups, DefaultGroups>(GhuPermsMod.CONFIG_DIRECTORY.resolve("groups.json5"), true)
}
