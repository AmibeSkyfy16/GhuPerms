package ch.skyfy.ghuperms.javafx

import ch.skyfy.ghuperms.javafx.utils.FXMLUtils
import ch.skyfy.ghuperms.utils.ModsUtils
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem
import javafx.scene.control.TextField
import javafx.scene.input.MouseButton
import javafx.scene.layout.StackPane
import java.net.URL
import java.util.*

@Suppress("PropertyName", "unused")
class MembersCellView(private val removePlayer: (String) -> Unit, private val updateName: (String, String) -> Unit) : StackPane(), Initializable {

    @FXML
    lateinit var playerName_TextField: TextField

    @FXML
    lateinit var playerUUID_TextField: TextField

    init { FXMLUtils.loadFXML(this) }

    override fun initialize(location: URL?, resources: ResourceBundle?) {

        playerName_TextField.textProperty().addListener { _, oldValue, newValue ->
            updateName.invoke(oldValue, newValue)
            playerUUID_TextField.text = ModsUtils.getPlayerUUIDFromNameWithUUID(newValue)
        }

        val contextMenu = ContextMenu()
        val item = MenuItem("remove")
        item.setOnAction { removePlayer.invoke(playerName_TextField.text) }

        contextMenu.items.add(item)

        this.setOnMouseClicked {
            if (it.button == MouseButton.SECONDARY) contextMenu.show(this, it.screenX, it.screenY)
        }


    }

}