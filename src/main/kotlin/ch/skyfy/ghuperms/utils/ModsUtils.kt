package ch.skyfy.ghuperms.utils

import ch.skyfy.ghuperms.config.Configs
import ch.skyfy.ghuperms.data.Group
import net.minecraft.server.network.ServerPlayerEntity

object ModsUtils {

    fun getPlayerNameWithUUID(spe: ServerPlayerEntity) = "${spe.name.string}#${spe.uuidAsString}"

    fun getPermsByPlayer(playerNameWithUUID: String) : List<Group> {

        return Configs.GROUPS.serializableData.list.filter { group ->
            group.members.any { it == playerNameWithUUID }
        }.sortedByDescending { group -> group.weight }

//        Configs.PLAYER_GROUPS.serializableData.list.filter { playerGroup -> playerGroup.playerNameWithUUID == playerNameWithUUID }.let { playerGroups ->
//
//                val groups = Configs.GROUPS.serializableData.list.filter { group ->
//                    playerGroups.any{playerGroup -> playerGroup.groupName == group.name }
//                }.sortedByDescending { group -> group.weight }
//
//            return groups
//        }
    }

}