package ch.skyfy.ghuperms.permission

import ch.skyfy.ghuperms.utils.ModsUtils

object Permissions {

    // OLD
//    @JvmStatic
//    fun check(playerName: String, permissionNameToCheck: String) : Boolean {
//        val players = PERMS.serializableData.players
//
//        players.firstOrNull { it.name == playerName }?.let { player ->
//            player.permissions.forEach { commandPermission ->
//                if (!commandPermission.name.contains(".")) {
//                    if (commandPermission.name == permissionNameToCheck){
//                        if(commandPermission.value) return true
//                    }else{
//                        if(permissionNameToCheck.split(".").contains(commandPermission.name))
//                            if(commandPermission.value) return true
//                    }
//                }else{
//                    val permissionNameToCheckList = permissionNameToCheck.split(".")
//                    for (i in permissionNameToCheckList.size downTo 0 step 1){
//                        val sb = StringBuilder()
//                        for (j in 0 until i){
//                            if(sb.isNotEmpty())sb.append(".")
//                            sb.append(permissionNameToCheckList[j])
//                        }
//                        if(commandPermission.name == sb.toString()) if(commandPermission.value) return true
//                    }
//
//                    val commandPermissionList = commandPermission.name.split(".")
//                    for (i in commandPermissionList.size downTo 0 step 1){
//                        val sb = StringBuilder()
//                        for (j in 0 until i){
//                            if(sb.isNotEmpty())sb.append(".")
//                            sb.append(commandPermissionList[j])
//                        }
//                        if(permissionNameToCheck == sb.toString()) if(commandPermission.value) return true
//                    }
//
////                    if(commandPermission.name.split(".").contains(permissionNameToCheck)) return true
//                }
//            }
//        }
//
//        return false
//    }

    @JvmStatic
    fun checkNew(playerNameWithUUID: String, permissionNameToCheck: String) : Boolean {
        val groups = ModsUtils.getPermsByPlayer(playerNameWithUUID)

        groups.forEach { group ->
            group.permissions.forEach { commandPermission ->
                if (!commandPermission.name.contains(".")) {
                    if (commandPermission.name == permissionNameToCheck){
                        if(commandPermission.value) return true
                    }else{
                        if(permissionNameToCheck.split(".").contains(commandPermission.name))
                            if(commandPermission.value) return true
                    }
                }else{
                    val permissionNameToCheckList = permissionNameToCheck.split(".")
                    for (i in permissionNameToCheckList.size downTo 0 step 1){
                        val sb = StringBuilder()
                        for (j in 0 until i){
                            if(sb.isNotEmpty())sb.append(".")
                            sb.append(permissionNameToCheckList[j])
                        }
                        if(commandPermission.name == sb.toString()) if(commandPermission.value) return true
                    }

                    val commandPermissionList = commandPermission.name.split(".")
                    for (i in commandPermissionList.size downTo 0 step 1){
                        val sb = StringBuilder()
                        for (j in 0 until i){
                            if(sb.isNotEmpty())sb.append(".")
                            sb.append(commandPermissionList[j])
                        }
                        if(permissionNameToCheck == sb.toString()) if(commandPermission.value) return true
                    }

//                    if(commandPermission.name.split(".").contains(permissionNameToCheck)) return true
                }
            }
        }

        return false
    }
}