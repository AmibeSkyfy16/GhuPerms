package ch.skyfy.ghuperms.javafx

import ch.skyfy.ghuperms.javafx.utils.FXMLUtils
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.TextField
import javafx.scene.layout.*
import java.net.URL
import java.util.*

@Suppress("PropertyName", "unused")
class MembersCellView : StackPane(), Initializable {

    @FXML
    lateinit var playerName_TextField: TextField

    @FXML
    lateinit var playerUUID_TextField: TextField

    init {
        FXMLUtils.loadFXML(this)
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {



    }

}