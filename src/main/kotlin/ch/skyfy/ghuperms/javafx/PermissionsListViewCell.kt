package ch.skyfy.ghuperms.javafx

import ch.skyfy.ghuperms.data.CommandPermission
import javafx.scene.control.ListCell
import javafx.scene.layout.*
import java.util.*

@Suppress("unused")
class PermissionsListViewCell(
    private val removePermission: (String) -> Unit,
    private val updateName: (String, String)->Unit,
    private val updateValue: (String, Boolean) -> Unit
) : ListCell<CommandPermission>() {

    override fun updateItem(item: CommandPermission?, empty: Boolean) {
        super.updateItem(item, empty)

        if (empty || item == null) {
            text = null
            graphic = null
        } else {
            val permissionsCellView = PermissionsCellView(removePermission, updateName, updateValue)
            permissionsCellView.permissionName_TextField.text = item.name
            permissionsCellView.value_ChoiceBox.value = item.value

            text = null
            graphic = permissionsCellView
        }
    }
}