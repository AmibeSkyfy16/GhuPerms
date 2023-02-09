package ch.skyfy.ghuperms.javafx

import ch.skyfy.ghuperms.javafx.utils.FXMLUtils
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.ChoiceBox
import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem
import javafx.scene.control.TextField
import javafx.scene.input.MouseButton
import javafx.scene.layout.*
import java.net.URL
import java.util.*

@Suppress("PropertyName", "unused")
class PermissionsCellView(
    private val removePermission: (String) -> Unit,
    private val updateName: (String, String) -> Unit,
    private val updateValue: (String, Boolean) -> Unit
) : StackPane(), Initializable {

    @FXML
    lateinit var permissionName_TextField: TextField

    @FXML
    lateinit var value_ChoiceBox: ChoiceBox<Boolean>

    init {
        FXMLUtils.loadFXML(this)
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        value_ChoiceBox.items.addAll(true, false)

        permissionName_TextField.textProperty().addListener { _, oldValue, newValue ->
            updateName.invoke(oldValue, newValue)
        }

        value_ChoiceBox.valueProperty().addListener { _, _, newValue ->
            updateValue.invoke(permissionName_TextField.text, newValue)
        }


        val contextMenu = ContextMenu()
        val item = MenuItem("remove")

        item.setOnAction {
            removePermission.invoke(permissionName_TextField.text)
        }

        contextMenu.items.add(item)

        this.setOnMouseClicked {
            if (it.button == MouseButton.SECONDARY) {
                contextMenu.show(this, it.screenX, it.screenY)
            }
        }


    }

}