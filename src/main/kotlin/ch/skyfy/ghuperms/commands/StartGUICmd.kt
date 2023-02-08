package ch.skyfy.ghuperms.commands

import ch.skyfy.ghuperms.javafx.PermissionManagerApp
import ch.skyfy.ghuperms.javafx.RootView
import ch.skyfy.ghuperms.utils.ModsUtils
import com.mojang.brigadier.Command
import com.mojang.brigadier.Command.SINGLE_SUCCESS
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import javafx.application.Application
import javafx.application.Platform
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class StartGUICmd : Command<ServerCommandSource> {

    companion object {

        var applicationLaunched = false

        val EXECUTOR: ExecutorService = Executors.newFixedThreadPool(10) { runnable ->
            return@newFixedThreadPool object : Thread(runnable) {
                init {
                    name = "A THREAD"
                    println("isDaemon $isDaemon")
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
                    Platform.setImplicitExit(false)
                    applicationLaunched = true
                    Application.launch(PermissionManagerApp::class.java)
                } else {
                    Platform.runLater {
                        val stage = Stage()
                        stage.title = "Permission Manager GUI"
                        stage.width = 1600.0
                        stage.height = 800.0
                        stage.scene = Scene(RootView())
                        stage.show()
                    }
                }
            }
        }
        return SINGLE_SUCCESS
    }

}