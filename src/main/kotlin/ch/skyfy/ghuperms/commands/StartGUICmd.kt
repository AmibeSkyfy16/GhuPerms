package ch.skyfy.ghuperms.commands

import ch.skyfy.ghuperms.GhuPermsMod
import ch.skyfy.ghuperms.utils.ModsUtils
import com.mojang.brigadier.Command
import com.mojang.brigadier.Command.SINGLE_SUCCESS
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import javafx.application.Application
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource

class StartGUICmd : Command<ServerCommandSource> {

    companion object {
        fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
            val cmd = literal("ghuperms").requires { source -> source.hasPermissionLevel(4) }
                .then(literal("gui").executes(StartGUICmd()))
            dispatcher.register(cmd)
        }
    }

    override fun run(context: CommandContext<ServerCommandSource>): Int {
        if (ModsUtils.canUseGUI()) {
            println("Starting the JavaFX Permission Manager")
            object : Thread(Runnable {
                Application.launch(GhuPermsMod.App::class.java)
            }) {init { this.name = "GUI Thread" } }.start()
        }
        return SINGLE_SUCCESS
    }

}