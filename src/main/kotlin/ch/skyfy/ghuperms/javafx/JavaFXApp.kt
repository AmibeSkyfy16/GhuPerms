package ch.skyfy.ghuperms.javafx

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.stage.Stage

class JavaFXApp : Application() {
    override fun start(stage: Stage) {
        stage.scene = Scene(StackPane(), 400.0, 400.0)
        stage.title = "Test"
        stage.show()
    }
}