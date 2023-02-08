package ch.skyfy.ghuperms.javafx

import ch.skyfy.ghuperms.javafx.utils.FXMLUtils
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.layout.*
import javafx.scene.paint.Color
import java.net.URL
import java.util.*

@Suppress("PropertyName", "unused")
class RootView() : StackPane(), Initializable {

    @FXML
    lateinit var root_GridPane: GridPane
    @FXML
    lateinit var root_VBox: VBox
    @FXML
    lateinit var stackpane2: StackPane


    init {
        FXMLUtils.loadFXML<RootView>(this)
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {

        stackpane2.minHeight = 10 * 400 * 1.4

        for(i in 0..10){
            val groupView = GroupView()
            root_VBox.children.add(groupView)
        }

        println("")
    }
}