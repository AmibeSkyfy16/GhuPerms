package ch.skyfy.ghuperms.javafx

import ch.skyfy.ghuperms.javafx.utils.FXMLUtils
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Label
import javafx.scene.input.MouseButton
import javafx.scene.layout.*
import java.net.URL
import java.util.*

@Suppress("PropertyName", "unused")
class GroupsView : StackPane(), Initializable {

    @FXML
    lateinit var root_GridPane: GridPane

    @FXML
    lateinit var groups_ChoiceBox: ChoiceBox<String>

    @FXML
    lateinit var members_Label: Label

    init {
        FXMLUtils.loadFXML(this)
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {

        groups_ChoiceBox.items = FXCollections.observableArrayList<String>("DEFAULT", "OP")

        members_Label.onMouseClicked = EventHandler { mouseEvent ->
            if(mouseEvent.button == MouseButton.PRIMARY){
                val membersView = MembersView()
                GridPane.setConstraints(membersView, 0, 2)
                root_GridPane.children.add(membersView)
            }
        }

    }

}