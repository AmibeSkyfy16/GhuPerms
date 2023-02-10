package ch.skyfy.ghuperms.javafx

import ch.skyfy.ghuperms.javafx.utils.FXMLUtils
import ch.skyfy.ghuperms.javafx.utils.UIUtils
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
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
    lateinit var groups_Button: Button

    private var groupsView = GroupsView()

    init {
        UIUtils.loadFont()
        FXMLUtils.loadFXML(this)
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        groups_Button.onMouseClicked = EventHandler { mouseEvent ->
            if (mouseEvent.button == MouseButton.PRIMARY) {
                if (!UIUtils.isInGridPane(0, 1, groupsView, second_GridPane)) {
                    GridPane.setConstraints(groupsView, 1, 0)
                    second_GridPane.children.add(groupsView)
                }
            }
        }
    }
}