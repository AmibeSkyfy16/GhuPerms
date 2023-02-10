package ch.skyfy.ghuperms.javafx.utils

import ch.skyfy.ghuperms.GhuPermsMod
import ch.skyfy.ghuperms.GhuPermsMod.Companion.MOD_CONTAINER
import javafx.application.Platform
import javafx.scene.layout.GridPane
import javafx.scene.text.Font
import java.util.zip.ZipInputStream

object UIUtils {

    inline fun <reified T> getNodeByRowColumnIndex(row: Int, column: Int, gridPane: GridPane): T? {
        for (node in gridPane.childrenUnmodifiable) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                return if (node is T) node
                else null
            }
        }
        return null
    }

    inline fun <reified T> isInGridPane(row: Int, column: Int, target: T, gridPane: GridPane): Boolean {
        for (node in gridPane.childrenUnmodifiable)
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column)
                return node is T && node == target
        return false
    }

    @Suppress("SameParameterValue")
    fun removeChildInGridPane(row: Int, column: Int, gridPane: GridPane) {
        val listIterator = gridPane.children.listIterator()
        listIterator.forEachRemaining { node ->
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column){
                listIterator.remove()
                return@forEachRemaining
            }
        }
    }

    fun loadFont(){
        Font.loadFont(GhuPermsMod::class.java.classLoader.getResource("javafx/ui/font/FiraCode-Bold.ttf")!!.toExternalForm(), 1.0)
    }

}