package ch.skyfy.ghuperms.utils

import ch.skyfy.ghuperms.GhuPermsMod
import ch.skyfy.ghuperms.config.Configs
import ch.skyfy.ghuperms.data.Group
import javafx.application.Application
import net.minecraft.server.PlayerManager
import net.minecraft.server.network.ServerPlayerEntity
import java.nio.file.Paths
import kotlin.io.path.*

object ModsUtils {

    fun getPlayerNameWithUUID(spe: ServerPlayerEntity) = "${spe.name.string}#${spe.uuidAsString}"

    fun getPermsByPlayer(playerNameWithUUID: String) : List<Group> {
        return Configs.GROUPS.serializableData.list.filter { group ->
            group.members.any { it == playerNameWithUUID }
        }.sortedByDescending { group -> group.weight }
    }

    fun sendCommandTreeToAll(playerManager: PlayerManager){
        for (serverPlayerEntity in playerManager.playerList) {
            playerManager.sendCommandTree(serverPlayerEntity)
        }
    }

    @OptIn(ExperimentalPathApi::class)
    fun canUseGUI() : Boolean {
        val javaHome = System.getProperty("java.home")
        val jmodsFolder = Paths.get(javaHome).resolve("jmods")
        if (jmodsFolder.exists()) {

            val map = mutableMapOf<String, String?>(
                "javafx.base.jmod" to null,
                "javafx.controls.jmod" to null,
                "javafx.fxml.jmod" to null,
                "javafx.graphics.jmod" to null,
                "javafx.media.jmod" to null,
                "javafx.swing.jmod" to null,
                "javafx.web.jmod" to null,
            )

            jmodsFolder.walk(PathWalkOption.INCLUDE_DIRECTORIES).iterator().forEach { path ->
                if(map.containsKey(path.fileName.name)) map.putIfAbsent(path.fileName.name, "")
            }

            if(map.none { it.value == null }){
                println("javafx is there")
                return true
            }
        }
        return false
    }

}