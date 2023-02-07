package ch.skyfy.ghuperms

import ch.skyfy.ghuperms.config.Configs
import ch.skyfy.json5configlib.ConfigManager
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.nio.file.Path

class GhuPermsModPreLaunch : PreLaunchEntrypoint {

//    companion object {
//        private const val MOD_ID: String = "ghuperms"
//        val CONFIG_DIRECTORY: Path = FabricLoader.getInstance().configDir.resolve(MOD_ID)
//        val LOGGER: Logger = LogManager.getLogger(GhuPermsModPreLaunch::class.java)
//
//        init {
//
//            ConfigManager.loadConfigs(arrayOf(Configs::class.java))
//        }
//    }

    override fun onPreLaunch() {}

}