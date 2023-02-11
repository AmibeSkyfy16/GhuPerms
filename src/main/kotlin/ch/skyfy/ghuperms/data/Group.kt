package ch.skyfy.ghuperms.data

import ch.skyfy.json5configlib.Defaultable
import ch.skyfy.json5configlib.Validatable
import io.github.xn32.json5k.SerialComment
import kotlinx.serialization.Serializable

@Serializable
data class Group(
    @SerialComment("The name of the group")
    val name: String,
    @SerialComment("The weight of the group, the OP group has a weight of 100, it will have priority over other groups")
    var weight: Int,
    @SerialComment("The list of permission")
    val permissions: MutableSet<CommandPermission>,
    @SerialComment("The players that are member of this group")
    val members: MutableSet<String>
) : Validatable

@Serializable
data class Groups(
    @SerialComment("The list of groups to which players can belong and therefore have access to the command with the corresponding permissions")
    var list: MutableSet<Group>
) : Validatable

class DefaultGroups : Defaultable<Groups> {
    override fun getDefault(): Groups {
        return Groups(
            mutableSetOf(
                Group(
                    "DEFAULT",
                    0,
                    mutableSetOf(
                        CommandPermission("command", false),
                        CommandPermission("command.msg", true),
                        CommandPermission("command.ghuperms", true)
                    ),
                    mutableSetOf()
                ),
                Group(
                    "OP",
                    100,
                    mutableSetOf(
                        CommandPermission("command", true)
                    ),
                    mutableSetOf()
                )
            )
        )
    }
}
