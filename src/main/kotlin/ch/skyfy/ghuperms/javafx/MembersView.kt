package ch.skyfy.ghuperms.javafx

import ch.skyfy.ghuperms.javafx.utils.FXMLUtils
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.Initializable
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.layout.*
import java.net.URL
import java.util.*

@Suppress("unused")
class MembersView : StackPane(), Initializable {


    private val studentObservableList: ObservableList<String>

    private val listView: ListView<String>

    init {

        studentObservableList = FXCollections.observableArrayList()
        studentObservableList.addAll("Skyfy16", "Alex", "Steve", "Lorem", "Ipsum")

        listView = ListView()

        FXMLUtils.loadFXML(this)
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {

        listView.items = studentObservableList
        listView.setCellFactory { MembersListViewCell2() }

        this.children.add(listView)

    }

}