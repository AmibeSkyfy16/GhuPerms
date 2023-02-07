package ch.skyfy.ghuperms.data

import ch.skyfy.json5configlib.Validatable
import kotlinx.serialization.Serializable

@Serializable
data class CommandPermission(
    val name: String,
    val value: Boolean
) : Validatable
