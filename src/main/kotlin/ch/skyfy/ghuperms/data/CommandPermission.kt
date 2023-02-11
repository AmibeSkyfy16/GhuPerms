package ch.skyfy.ghuperms.data

import ch.skyfy.json5configlib.Validatable
import io.github.xn32.json5k.SerialComment
import kotlinx.serialization.Serializable

@Serializable
data class CommandPermission(
    @SerialComment("The permission name (something like \"command.msg\" or \"command.gamerule\" for no-namespaces commands) or (\"mc:command.msg\" for namespaces commands)")
    var name: String,
    @SerialComment("The value of the permission, false means that the permission is denied")
    var value: Boolean
) : Validatable
