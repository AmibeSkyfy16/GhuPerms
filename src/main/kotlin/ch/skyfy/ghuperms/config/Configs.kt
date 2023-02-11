package ch.skyfy.ghuperms.config

import ch.skyfy.ghuperms.data.DefaultGroups
import ch.skyfy.ghuperms.data.Groups
import ch.skyfy.ghuperms.prelaunch.GhuPermsPreLauncher
import ch.skyfy.json5configlib.ConfigData


object Configs {
    @JvmField
    val PERMISSIONS_DATA = ConfigData.invoke<PermissionsData, DefaultPermissionsData>(GhuPermsPreLauncher.CONFIG_DIRECTORY.resolve("all-permissions.json5"), false)

    @JvmField
    val GROUPS = ConfigData.invoke<Groups, DefaultGroups>(GhuPermsPreLauncher.CONFIG_DIRECTORY.resolve("groups.json5"), true)
}
