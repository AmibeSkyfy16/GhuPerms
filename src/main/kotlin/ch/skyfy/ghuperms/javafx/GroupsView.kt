package ch.skyfy.ghuperms.javafx

import ch.skyfy.ghuperms.config.Configs
import ch.skyfy.ghuperms.data.CommandPermission
import ch.skyfy.ghuperms.data.Group
import ch.skyfy.ghuperms.data.Groups
import ch.skyfy.ghuperms.javafx.utils.FXMLUtils
import ch.skyfy.ghuperms.utils.TestUtils
import ch.skyfy.json5configlib.update
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.input.MouseButton
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.stage.Modality
import javafx.stage.Stage
import java.net.URL
import java.util.*
import java.util.concurrent.atomic.AtomicReference

@Suppress("PropertyName", "unused")
class GroupsView : StackPane(), Initializable {

    @FXML
    lateinit var root_GridPane: GridPane

    @FXML
    lateinit var groups_ChoiceBox: ChoiceBox<String>

    @FXML
    lateinit var members_Label: Label

    @FXML
    lateinit var permissions_Label: Label

    @FXML
    lateinit var groupWeight_TextField: TextField

    @FXML
    lateinit var apply_Button: Button

    private val groupProperty = SimpleObjectProperty<Group?>(null)

    private var membersView = MembersView(groupProperty)

    private val permissionsView = PermissionsView(groupProperty)

    private var testGroups = mutableSetOf(

        Group(
            "DEFAULT",
            0,
            mutableSetOf(
                CommandPermission("command", false),
                CommandPermission("command.gamemode.create", true),
                CommandPermission("command.gamemode.survival", true),
                CommandPermission("command.gamerule", true),
                CommandPermission("command.msg", true),
                CommandPermission("command.fill", true),
                CommandPermission("command.tell", true),
            ),
            mutableSetOf(
                "Skyfy16#3231313",
                "Steve#456382",
                "Alex#25252",
                "Alex2#235325",
                "Alex3#252532",
                "Alex4#52352",
                "Alex5#52352",
                "Alex6#52352",
                "Alex7#52352",
                "Alex8#52352",
                "Alex9#52352",
                "Alex10#52352",
            )
        ),
        Group(
            "OP",
            1000,
            mutableSetOf(
                CommandPermission("command", true),
            ),
            mutableSetOf(
                "Skyfy16#3231313",
                "Steve#456382",
                "Alex#25252",
                "Steve2#235325",
                "Steve3#252532",
                "Steve4#52352",
                "Steve5#52352",
                "Steve6#52352",
                "Steve7#52352",
                "Steve8#52352",
                "Steve9#52352",
                "Steve10#52352",
            )
        ), Group(
            "ADMIN",
            1000,
            mutableSetOf(
                CommandPermission("command", false),
                CommandPermission("command.ghuperms", true),
            ),
            mutableSetOf(
                "Admin#3231313",
                "Admin#456382",
                "Admin#25252",
                "Admin2#235325",
                "Admin3#252532"
            )
        )
    )

    private val groups = Configs.GROUPS.serializableData.list.toSet()

    init { FXMLUtils.loadFXML(this) }

    override fun initialize(location: URL?, resources: ResourceBundle?) {

        registerOnApply()

        groups_ChoiceBox.valueProperty().addListener { _, _, newValue ->
            val group = TestUtils.getGroupByName(groups, newValue)
            groupWeight_TextField.text = group?.weight.toString()
            groupProperty.set(group)
        }

        groupProperty.addListener { _, _, newValue ->
            newValue?.let { group ->
                membersView.membersProperty.putIfAbsent(group.name, SimpleListProperty(FXCollections.observableArrayList(group.members.map { AtomicReference(it) }.toList())))

                permissionsView.permissionsProperty.putIfAbsent(group.name, SimpleListProperty(FXCollections.observableArrayList(group.permissions)))
                permissionsView.update(group.name)
                membersView.update(group.name)
            }

            if (isInGridPane(2, 0, membersView, root_GridPane)) showMembersView()
            else if (isInGridPane(2, 0, permissionsView, root_GridPane)) showPermissionsView()

        }

        members_Label.onMouseClicked = EventHandler { mouseEvent ->
            if (mouseEvent.button == MouseButton.PRIMARY) showMembersView()
        }

        permissions_Label.onMouseClicked = EventHandler { mouseEvent ->
            if (mouseEvent.button == MouseButton.PRIMARY) showPermissionsView()
        }

        groups_ChoiceBox.items = FXCollections.observableArrayList(groups.map { it.name })
        groups_ChoiceBox.value = groups_ChoiceBox.items.firstOrNull { it == "DEFAULT" }

        buildMembersContextMenu()
        buildPermissionsContextMenu()
    }

    private fun registerOnApply() {
        apply_Button.setOnAction {
            val updateGroups = mutableSetOf<Group>()
            groups_ChoiceBox.items.forEach { groupName ->
                val members = membersView.membersProperty[groupName]?.toMutableSet()
                val permissions = permissionsView.permissionsProperty[groupName]?.toMutableSet()
                val weight = groups.firstOrNull { it.name == groupName }?.weight

                if (members == null || permissions == null || weight == null) return@forEach
                else updateGroups.add(Group(groupName, weight, permissions, members.map { it.get() }.toMutableSet()))
            }

            Configs.GROUPS.update(Groups::list, updateGroups)
        }
    }

    private fun buildMembersContextMenu() {
        val contextMenu = ContextMenu()
        val menuItem1 = MenuItem("new")
        menuItem1.setOnAction {
            val stage = Stage()
            stage.width = 450.0
            stage.height = 300.0
            stage.initModality(Modality.APPLICATION_MODAL)
            stage.initOwner(root_GridPane.scene.window)
            val sp = StackPane()
            sp.setMinSize(450.0, 300.0)

            val hbox = HBox()
            hbox.alignment = Pos.CENTER

            val textField = TextField()
            textField.promptText = "player name"

            hbox.children.add(textField)

            val ok = Button("OK")
            ok.setOnAction {
                groupProperty.value?.let { group ->
                    membersView.membersProperty[group.name]?.add(AtomicReference(textField.text))
                    membersView.update(group.name)
                }
                stage.close()
            }

            hbox.children.add(ok)

            sp.children.add(hbox)

            stage.scene = Scene(sp)
            stage.show()
        }
        contextMenu.items.add(menuItem1)

        members_Label.contextMenu = contextMenu
    }

    private fun buildPermissionsContextMenu() {
        val contextMenu = ContextMenu()
        val menuItem1 = MenuItem("new")
        menuItem1.setOnAction {
            val stage = Stage()
            stage.width = 450.0
            stage.height = 300.0
            stage.initModality(Modality.APPLICATION_MODAL)
            stage.initOwner(root_GridPane.scene.window)
            val sp = StackPane()
            sp.setMinSize(450.0, 300.0)

            val hbox = HBox()
            hbox.alignment = Pos.CENTER

            val textField = TextField()
            textField.promptText = "permission name"

            val value = ChoiceBox<Boolean>()
            value.items.addAll(true, false)
            value.value = value.items.first()

            hbox.children.addAll(textField, value)

            val ok = Button("OK")
            ok.setOnAction {
                groupProperty.value?.let { group ->
                    permissionsView.permissionsProperty[group.name]?.add(CommandPermission(textField.text, value.value))
                    permissionsView.update(group.name)
                }
                stage.close()
            }

            hbox.children.add(ok)

            sp.children.add(hbox)

            stage.scene = Scene(sp)
            stage.show()
        }
        contextMenu.items.add(menuItem1)

        permissions_Label.contextMenu = contextMenu
    }

    private fun showMembersView() {
        if (!isInGridPane(2, 0, membersView, root_GridPane)) {
            removeChildInGridPane(2, 0, root_GridPane)
            GridPane.setConstraints(membersView, 0, 2)
            root_GridPane.children.add(membersView)
        }
    }

    private fun showPermissionsView() {
        if (!isInGridPane(2, 0, permissionsView, root_GridPane)) {
            removeChildInGridPane(2, 0, root_GridPane)
            GridPane.setConstraints(permissionsView, 0, 2)
            root_GridPane.children.add(permissionsView)
        }
    }

    private inline fun <reified T> getNodeByRowColumnIndex(row: Int, column: Int, gridPane: GridPane): T? {
        for (node in gridPane.childrenUnmodifiable) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                return if (node is T) node
                else null
            }
        }
        return null
    }

    private inline fun <reified T> isInGridPane(row: Int, column: Int, target: T, gridPane: GridPane): Boolean {
        for (node in gridPane.childrenUnmodifiable)
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column)
                return node is T && node == target
        return false
    }

    @Suppress("SameParameterValue")
    private fun removeChildInGridPane(row: Int, column: Int, gridPane: GridPane) {
        val listIterator = gridPane.children.listIterator()
        listIterator.forEachRemaining { node ->
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) listIterator.remove()
        }
    }

}