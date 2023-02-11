package ch.skyfy.ghuperms.prelaunch.config

import ch.skyfy.ghuperms.prelaunch.GhuPermsPreLauncher
import ch.skyfy.json5configlib.ConfigData

object PreLaunchConfigs {

    @JvmField
    val COMMANDS_ALIASES = ConfigData.invokeSpecial<GhuPermsConfig>(GhuPermsPreLauncher.CONFIG_DIRECTORY.resolve("aliases.json5"), false)

}