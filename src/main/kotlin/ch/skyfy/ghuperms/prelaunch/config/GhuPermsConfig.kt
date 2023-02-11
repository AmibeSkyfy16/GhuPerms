package ch.skyfy.ghuperms.prelaunch.config

import ch.skyfy.json5configlib.Validatable
import kotlinx.serialization.Serializable

@Serializable
data class GhuPermsConfig(
    val enableNameSpacingCommands: Boolean = false,
    val list: MutableList<CommandAlias> = mutableListOf(
        CommandAlias("mc:op", "op")
    )
) : Validatable

@Serializable
data class CommandAlias(
    val baseCommand: String,
    val alias: String
) : Validatable