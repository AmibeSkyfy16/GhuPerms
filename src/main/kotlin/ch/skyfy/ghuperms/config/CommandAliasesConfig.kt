package ch.skyfy.ghuperms.config

import ch.skyfy.json5configlib.Defaultable
import ch.skyfy.json5configlib.Validatable
import kotlinx.serialization.Serializable

@Serializable
data class CommandAliasesConfig(
    val aliases: MutableSet<CommandAlias>
) : Validatable

@Serializable
data class CommandAlias(
    val baseCommand: String,
    val alias: String
) : Validatable

class DefaultCommandAliasesConfig : Defaultable<CommandAliasesConfig> {
    override fun getDefault(): CommandAliasesConfig {
        return CommandAliasesConfig(mutableSetOf(
            CommandAlias("mc:gamerule", "gamerule"),
            CommandAlias("mc:gamemode", "gamemode")
        ))
    }
}


