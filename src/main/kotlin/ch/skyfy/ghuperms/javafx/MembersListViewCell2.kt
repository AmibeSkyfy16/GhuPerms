package ch.skyfy.ghuperms.javafx

import ch.skyfy.ghuperms.javafx.utils.FXMLUtils
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.control.TextField
import javafx.scene.input.MouseButton
import javafx.scene.layout.*
import java.net.URL
import java.util.*

@Suppress("unused")
class MembersListViewCell2 : ListCell<String>() {


    init {

    }

    override fun updateItem(item: String?, empty: Boolean) {
        super.updateItem(item, empty)

        if(empty || item == null){
            text = null
            graphic = null
        }else{
            val membersCellView = MembersCellView()
            membersCellView.playerName_TextField.text = item


            text = null
            graphic = membersCellView
        }
    }
}