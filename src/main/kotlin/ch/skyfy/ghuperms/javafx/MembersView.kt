package ch.skyfy.ghuperms.javafx

import ch.skyfy.ghuperms.data.Group
import ch.skyfy.ghuperms.javafx.utils.FXMLUtils
import ch.skyfy.ghuperms.utils.ModsUtils
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleMapProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.fxml.Initializable
import javafx.scene.control.ListView
import javafx.scene.layout.StackPane
import java.net.URL
import java.util.*
import java.util.concurrent.atomic.AtomicReference

class MembersView(private val groupProp: SimpleObjectProperty<Group?>) : StackPane(), Initializable {

    val membersProperty = SimpleMapProperty<String, SimpleListProperty<AtomicReference<String>>>(FXCollections.observableHashMap())

    private val listView: ListView<AtomicReference<String>> = ListView()

    init { FXMLUtils.loadFXML(this) }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        val removePlayer: (String) -> Unit = { playerName ->
            listView.items.removeIf { item -> ModsUtils.getPlayerNameFromNameWithUUID(item.get()) == playerName }
            listView.refresh()
        }

        val updateName: (String, String) -> Unit = { oldName, newName ->
            groupProp.value?.let { group ->
                membersProperty[group.name]?.firstOrNull { ref -> ref.get() == oldName }?.set(newName)
            }
        }

        listView.setCellFactory { MembersListViewCell(removePlayer, updateName) }
        this.children.add(listView)
    }

    fun update(groupName: String) {
        val members = membersProperty.value[groupName]
        if (members != null) listView.itemsProperty().set(members)
        listView.refresh()
    }

}