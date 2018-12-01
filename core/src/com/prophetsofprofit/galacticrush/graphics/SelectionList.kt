package com.prophetsofprofit.galacticrush.graphics

import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import ktx.scene2d.Scene2DSkin

/**
 * An element that handles displaying a list of items
 * Different from the LibGDX default List widget because it only allows one item to be selected at once and has a much nicer syntax
 */
class SelectionList : ScrollPane(List<String>(Scene2DSkin.defaultSkin), Scene2DSkin.defaultSkin) {

    //Gets the list that is in the scroll pane
    val list = this.actor as List<String>

    //Gets the selected item
    val selected: String get() = this.list.selected

    //Gets the selected index
    val index: Int = this.list.selectedIndex

    /**
     * Sets the list to only have one selectable item
     */
    init {
        this.list.selection.rangeSelect = false
    }

    /**
     * Adds an item to a selection list
     */
    fun addItem(item: String) {
        val items = this.list.items
        items.add(item)
        this.list.setItems(items)
    }

}