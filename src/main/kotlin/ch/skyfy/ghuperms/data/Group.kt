package ch.skyfy.ghuperms.data

import ch.skyfy.json5configlib.Defaultable
import ch.skyfy.json5configlib.Validatable
import kotlinx.serialization.Serializable

@Serializable
data class Group(
    val name: String,
    val weight: Int,
    val permissions: MutableSet<CommandPermission>,
    val members: MutableSet<String>
) : Validatable

@Serializable
data class Groups(
    val list: MutableSet<Group>
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
                        CommandPermission("command.ghuperms.reload", true),
                        CommandPermission("command.manymanycommands", true),
                        CommandPermission("command.gamemode", true)
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
