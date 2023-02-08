package ch.skyfy.ghuperms.javafx

import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage

class PermissionManagerApp : Application() {
    override fun start(stage: Stage) {
        stage.title = "Permission Manager GUI"
        stage.width = 1600.0
        stage.height = 800.0
        stage.scene = Scene(RootView())
        stage.show()
    }
}