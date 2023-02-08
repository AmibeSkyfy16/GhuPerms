package ch.skyfy.ghuperms.utils

import ch.skyfy.ghuperms.config.Configs
import ch.skyfy.ghuperms.data.Group
import net.minecraft.server.PlayerManager
import net.minecraft.server.network.ServerPlayerEntity

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

}