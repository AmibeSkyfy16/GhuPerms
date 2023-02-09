package ch.skyfy.ghuperms.javafx

import ch.skyfy.ghuperms.utils.ModsUtils
import javafx.scene.control.ListCell
import javafx.scene.layout.*
import java.util.*
import java.util.concurrent.atomic.AtomicReference

@Suppress("unused")
class MembersListViewCell(private val removePlayer: (String) -> Unit, private val updateName: (String, String) -> Unit) : ListCell<AtomicReference<String>>() {

    override fun updateItem(item: AtomicReference<String>?, empty: Boolean) {
        super.updateItem(item, empty)

        if (empty || item == null) {
            text = null
            graphic = null
        } else {
            val membersCellView = MembersCellView(removePlayer, updateName)
            membersCellView.playerName_TextField.text = ModsUtils.getPlayerNameFromNameWithUUID(item.get())
            membersCellView.playerUUID_TextField.text = ModsUtils.getPlayerUUIDFromNameWithUUID(item.get())

            text = null
            graphic = membersCellView
        }
    }
}