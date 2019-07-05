package com.prophetsofprofit.galacticrush.graphics

import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Window

/**
 * The basic interface for more specific interactions
 * Acts as a modal window, but can also handle UI-specific things like animation
 */
abstract class GalacticRushWindow(title: String, skin: Skin, draggable: Boolean, x: Float, y: Float, width: Float, height: Float) : Window(title, skin) {

    init {
        this.isMovable = draggable
        this.x = x
        this.y = y
        this.width = width
        this.height = height
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

    /**
     * Animate the window's movement to the target location
     * Linear for now
     */
    fun move(x: Float, y: Float, width: Float, height: Float, time: Float) {
        val move = MoveToAction()
        move.setPosition(x, y)
        move.duration = time
        val scale = ScaleToAction()
        scale.duration = time
        scale.setScale(width / this.width, height / this.height)
        this.addAction(ParallelAction(move, scale))
    }

}