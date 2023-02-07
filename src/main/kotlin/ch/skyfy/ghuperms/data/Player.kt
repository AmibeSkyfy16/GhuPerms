package ch.skyfy.ghuperms.data

import ch.skyfy.json5configlib.Validatable
import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val name: String,
    val permissions: MutableSet<CommandPermission>
) : Validatable {
}