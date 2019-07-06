package com.prophetsofprofit.galacticrush.graphics

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Window

/**
 * The basic interface for more specific interactions
 * Acts as a modal window, but can also handle UI-specific things like animation
 */
abstract class GalacticRushWindow(val screen: GalacticRushScreen, title: String, skin: Skin, draggable: Boolean, x: Float, y: Float, width: Float, height: Float, align: Int) : Window(title, skin) {

    //Whether the panel can be interacted with: can't be interacted with when moving
    val canBeUsed: Boolean
        get() = this.actions.size == 0

    init {
        this.isMovable = draggable
        this.isModal = false
        this.setSize(width, height)
        this.setPosition(x, y, align)
        this.setKeepWithinStage(false)
    }

    /**
     * What happens when the window appears
     * How does it appear?
     */
    abstract fun appear()

    /**
     * What happens when the window disappears
     * How does it disappear?
     */
    abstract fun disappear()

}