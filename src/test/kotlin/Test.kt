import kotlin.test.Test

class Test {

    @Test
    fun test() {
//        val str1 = "homes.commands.*"
//        val str2 = "homes.commands.homes.create"

//        if(str2.contains(str1.substringBeforeLast('*').substringBeforeLast('.'))) println(true)

//        var sb = StringBuilder("commands.hadda")
//        sb = StringBuilder("luckperms.").append(sb)
//
//        println(sb.toString())


//
//        val result = check(perms, "Skyfy16", "command.homes")
//        println(result)
//
//        val result2 = check(perms, "Skyfy16", "command.homes.teleport")
//        println(result2)
////
////        val result3 = check(perms, "Skyfy16", "homes.delete")
////        println(result3)
////
//        val result4 = check(perms, "Skyfy16", "command.homes")
//        println(result4)

    }


//    private fun check(config: PermissionsConfig, playerName: String, permissionNameToCheck: String): Boolean {
//
//        var temp = false
//
//        config.players.firstOrNull { it.name == playerName }?.let { player ->
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
////                        if(commandPermission.name.split(".").contains(sb.toString())) if(commandPermission.value) return true
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
////                    if(commandPermission.name.split(".").any { permissionNameToCheck.split(".").contains(it) })
////                        if(commandPermission.value) return true
////                    if(commandPermission.name.split(".").contains(permissionNameToCheck)) if(commandPermission.value) return true
//                }
//            }
//        }
//
//        return temp
//    }

}