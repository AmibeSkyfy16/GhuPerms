import ch.skyfy.ghuperms.data.CommandPermission
import ch.skyfy.ghuperms.data.Group
import javafx.application.Platform
import org.junit.jupiter.api.BeforeAll
import kotlin.test.Test


class UnitTestKt {

    companion object {
        @JvmStatic
        @BeforeAll
        fun initJfxRuntime(): Unit {
            println("beforeALL")
            Platform.startup {}
        }
    }


    @Test
    fun test() {
//        Application.launch(PermissionManagerApp::class.java)


        val l = "mc:command.op".substringAfterLast(":")
        val l3 = "command".substringAfterLast("command.")


        val groups = mutableSetOf<Group>(
            Group(
                "DEFAULT",
                0,
                mutableSetOf(
                    CommandPermission("any:command", false),
                    CommandPermission("mc:command.op", true),
//                    CommandPermission("ghuperms:command", false),
//                    CommandPermission("ghuperms:command.ghuperms", true)
                ),
                mutableSetOf(
                    "Skyfy16#c3419ddb-f019-3764-a6b2-47a0f91e25ee"
                )
            )
        )



        val result = check(groups, "Skyfy16#c3419ddb-f019-3764-a6b2-47a0f91e25ee", "mc:command.op")
        println(result)

        val result2 = check(groups, "Skyfy16#c3419ddb-f019-3764-a6b2-47a0f91e25ee", "ghuperms:command.ghuperms.gui")
        println(result2)
////
////        val result3 = check(perms, "Skyfy16", "homes.delete")
////        println(result3)
////
//        val result4 = check(perms, "Skyfy16", "command.homes")
//        println(result4)

    }


    @Suppress("LocalVariableName")
    private fun check(groups: MutableSet<Group>, playerName: String, permissionNameToCheck: String): Boolean {
        var _permissionNameToCheck = permissionNameToCheck

        groups.forEach { group ->

            var anyFound = false

            group.permissions.forEach { commandPermission ->
                var commandPermissionName = commandPermission.name

                if(commandPermissionName.contains("any:command")){
                    anyFound = true
                    _permissionNameToCheck = permissionNameToCheck.replaceBeforeLast(":", "any")
                }
                if(anyFound)
                    commandPermissionName = commandPermissionName.replaceBeforeLast(":", "any")


                if (!commandPermissionName.contains(".")) {
                    if (commandPermissionName == _permissionNameToCheck) {
                        if (commandPermission.value) return true
                    } else {
                        if (_permissionNameToCheck.split(".").contains(commandPermissionName))
                            if (commandPermission.value) return true
                    }
                } else {
                    val permissionNameToCheckList = _permissionNameToCheck.split(".")
                    for (i in permissionNameToCheckList.size downTo 0 step 1) {
                        val sb = StringBuilder()
                        for (j in 0 until i) {
                            if (sb.isNotEmpty()) sb.append(".")
                            sb.append(permissionNameToCheckList[j])
                        }
                        if (commandPermissionName == sb.toString()) if (commandPermission.value) return true
                    }

                    val commandPermissionList = commandPermissionName.split(".")
                    for (i in commandPermissionList.size downTo 0 step 1) {
                        val sb = StringBuilder()
                        for (j in 0 until i) {
                            if (sb.isNotEmpty()) sb.append(".")
                            sb.append(commandPermissionList[j])
                        }
                        if (_permissionNameToCheck == sb.toString()) if (commandPermission.value) return true
                    }
                }
            }
        }

        return false
    }

}