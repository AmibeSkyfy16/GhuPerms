package ch.skyfy.ghuperms.javafx

import ch.skyfy.ghuperms.data.Groups
import ch.skyfy.ghuperms.javafx.utils.FXMLUtils
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.geometry.Insets
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.control.cell.ComboBoxListCell
import javafx.scene.layout.*
import javafx.scene.paint.Color
import java.net.URL
import java.util.*

@Suppress("PropertyName", "unused")
class GroupView : StackPane(), Initializable {

    @FXML
    lateinit var root_GridPane: GridPane

    @FXML
    lateinit var groupName_TextField: TextField
    @FXML
    lateinit var groupWeight_TextField: TextField
    @FXML
    lateinit var permissions_ListView: ListView<String>
    @FXML
    lateinit var members_ListView: ListView<String>

    init {
        FXMLUtils.loadFXML<GroupView>(this)
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        this.background = Background(BackgroundFill(Color.YELLOWGREEN, CornerRadii(1.0), Insets(10.0)))

        val names: ObservableList<String> = FXCollections.observableArrayList()

        for(i in 0..12){
            names.add("player$i$i")
        }
        members_ListView.items = names
//        members_ListView.setCellFactory { ComboBoxListCell.forListView(Callba) }
    }

}