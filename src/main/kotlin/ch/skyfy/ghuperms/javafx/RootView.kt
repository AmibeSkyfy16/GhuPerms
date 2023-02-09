package ch.skyfy.ghuperms.javafx

import ch.skyfy.ghuperms.javafx.utils.FXMLUtils
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.input.MouseButton
import javafx.scene.layout.GridPane
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import java.net.URL
import java.util.*

@Suppress("PropertyName", "unused")
class RootView : StackPane(), Initializable {

    @FXML
    lateinit var root_GridPane: GridPane

    @FXML
    lateinit var second_GridPane: GridPane

    @FXML
    lateinit var root_VBox: VBox

    @FXML
    lateinit var groups_Label: Label

    init { FXMLUtils.loadFXML(this) }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        groups_Label.onMouseClicked = EventHandler { mouseEvent ->
            if (mouseEvent.button == MouseButton.PRIMARY) {
                val groupsView = GroupsView()
                GridPane.setConstraints(groupsView, 1, 0)
                second_GridPane.children.add(groupsView)
            }
        }
    }
}