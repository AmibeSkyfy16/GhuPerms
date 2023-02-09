package ch.skyfy.ghuperms.javafx

import ch.skyfy.ghuperms.data.CommandPermission
import ch.skyfy.ghuperms.data.Group
import ch.skyfy.ghuperms.javafx.utils.FXMLUtils
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleMapProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.fxml.Initializable
import javafx.scene.control.ListView
import javafx.scene.layout.*
import java.net.URL
import java.util.*

@Suppress("unused")
class PermissionsView(private val groupProp: SimpleObjectProperty<Group?>) : StackPane(), Initializable {

    val permissionsProperty = SimpleMapProperty<String, SimpleListProperty<CommandPermission>>(FXCollections.observableHashMap())

    private val listView: ListView<CommandPermission> = ListView()

    init { FXMLUtils.loadFXML(this) }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        val removePermission: (String) -> Unit = { permissionName ->
            listView.items.removeIf { item -> item.name == permissionName }
            listView.refresh()
        }

        val updateName: (String, String) -> Unit = { key, value ->
            groupProp.value?.let { group ->
                permissionsProperty[group.name]?.firstOrNull { commandPermission -> commandPermission.name == key }?.let { it.name = value }
            }
        }

        val updateValue: (String, Boolean) -> Unit = { key, value ->
            groupProp.value?.let { group ->
                permissionsProperty[group.name]?.firstOrNull { commandPermission -> commandPermission.name == key }?.let { it.value = value }
            }
        }

        listView.setCellFactory { PermissionsListViewCell(removePermission, updateName, updateValue) }
        this.children.add(listView)
    }

    fun update(groupName: String) {
        val members = permissionsProperty.value[groupName]
        if (members != null) listView.itemsProperty().set(members)
        listView.refresh()
    }

}