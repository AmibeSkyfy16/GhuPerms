package ch.skyfy.ghuperms.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.Command.SINGLE_SUCCESS
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource

class EnableDisableAllCommandCmd : Command<ServerCommandSource> {

    companion object {

        var ENABLED = false

        fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
            val cmd = literal("ghuperms").requires { source -> source.hasPermissionLevel(4) }
                .then(
                    literal("enableDisableAllCommand").executes(EnableDisableAllCommandCmd())
                )
            dispatcher.register(cmd)
        }
    }

    override fun run(context: CommandContext<ServerCommandSource>): Int {
        ENABLED = !ENABLED
        return SINGLE_SUCCESS
    }

}