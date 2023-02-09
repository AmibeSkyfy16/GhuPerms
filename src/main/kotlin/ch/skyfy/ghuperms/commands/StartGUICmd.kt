package ch.skyfy.ghuperms.commands

import ch.skyfy.ghuperms.javafx.PermissionManagerApp
import ch.skyfy.ghuperms.utils.ModsUtils
import com.mojang.brigadier.Command
import com.mojang.brigadier.Command.SINGLE_SUCCESS
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import javafx.application.Application
import javafx.application.Platform
import javafx.stage.Stage
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.reflect.full.createInstance

class StartGUICmd : Command<ServerCommandSource> {

    companion object {

        var applicationLaunched = false

        val EXECUTOR: ExecutorService = Executors.newFixedThreadPool(5) { runnable ->
            return@newFixedThreadPool object : Thread(runnable) {
                init {
                    name = "A THREAD"
                    isDaemon = true
                }
            }
        }

        fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
            val cmd = literal("ghuperms").requires { source -> source.hasPermissionLevel(4) }
                .then(literal("gui").executes(StartGUICmd()))
            dispatcher.register(cmd)
        }
    }

    override fun run(context: CommandContext<ServerCommandSource>): Int {
        if (ModsUtils.canUseGUI()) {
            println("Starting the JavaFX Permission Manager")
            EXECUTOR.submit {
                if (!applicationLaunched) {
                    applicationLaunched = true
                    Platform.setImplicitExit(false)
                    Application.launch(PermissionManagerApp::class.java)
                } else Platform.runLater { PermissionManagerApp::class.createInstance().start(Stage()) }
            }
        }
        return SINGLE_SUCCESS
    }

}