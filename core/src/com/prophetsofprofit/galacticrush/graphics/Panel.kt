package com.prophetsofprofit.galacticrush.graphics

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.utils.Align
import com.prophetsofprofit.galacticrush.graphics.screen.GalacticRushScreen
import ktx.math.minus
import ktx.scene2d.Scene2DSkin

/**
 * An enumeration of the directions that a panel can appear and disappear
 */
enum class Direction {
    TOP, RIGHT, BOTTOM, LEFT, POP
}

/**
 * A class that represents a fixed-place or static panel that doesn't move outside of animations
 */
open class Panel(val screen: GalacticRushScreen, title: String, x: Float, y: Float, w: Float, h: Float, align: Int = Align.bottomLeft) : Window(title, Scene2DSkin.defaultSkin) {

    //Where the panel should be
    val location: Rectangle
    //Whether the panel is where it should be
    val isInLocation
        get() = this.x == location.x && this.y == location.y && this.width == location.width && this.height == location.height
    //Whether the panel can be interacted with: can't be interacted with when moving
    var canBeUsed = true

    /**
     * Initializes the Window as an immovable panel
     */
    init {
        this.style.stageBackground = null
        this.setSize(w, h)
        this.setPosition(x, y, align)
        this.location = Rectangle(this.x, this.y, w, h)
        this.titleLabel.setAlignment(Align.center)
        this.isModal = false
        this.isMovable = false
        this.isResizable = false
    }

    /**
     * The procedure for the panel to appear from the given direction
     */
    fun appear(direction: Direction, time: Float) {
        if (!this.canBeUsed) {
            return
        }
        this.canBeUsed = false
        this.isVisible = true
        when (direction) {
            Direction.TOP -> {
                this.setPosition(this.x, this.screen.uiCamera.viewportHeight)
            }
            Direction.RIGHT -> {
                this.setPosition(this.screen.uiCamera.viewportWidth, this.y)
            }
            Direction.BOTTOM -> {
                this.setPosition(this.x, -this.height)
            }
            Direction.LEFT -> {
                this.setPosition(-this.width, this.y)
            }
            Direction.POP -> {
                this.setScale(0f)
            }
        }
        val startingPosition = Vector2(this.x, this.y)
        val necessaryMovement = Vector2(this.location.x, this.location.y) - startingPosition
        var elapsedTime = 0f
        this.addAction(object : Action() {
            override fun act(delta: Float): Boolean {
                elapsedTime = minOf(time, elapsedTime + delta)
                setPosition(startingPosition.x + elapsedTime / time * necessaryMovement.x, startingPosition.y + elapsedTime / time * necessaryMovement.y)
                if (scaleX < 1) {
                    setScale(elapsedTime / time)
                }
                if (elapsedTime == time) {
                    removeAction(this)
                    canBeUsed = true
                    return true
                }
                return false
            }
        })
    }

    /**
     * The procedure for the panel to disappear from the given direction
     */
    fun disappear(direction: Direction, time: Float) {
        if (!this.canBeUsed) {
            return
        }
        this.canBeUsed = false
        val necessaryMovement = when (direction) {
            Direction.TOP -> {
                Vector2(0f, this.y - this.screen.uiCamera.viewportHeight)
            }
            Direction.RIGHT -> {
                Vector2(this.x - this.screen.uiCamera.viewportWidth, 0f)
            }
            Direction.BOTTOM -> {
                Vector2(0f, this.y + this.height - this.screen.uiCamera.viewportHeight)
            }
            Direction.LEFT -> {
                Vector2(this.x + this.width - this.screen.uiCamera.viewportWidth, 0f)
            }
            Direction.POP -> {
                this.setScale(1f)
                Vector2(this.location.x, this.location.y)
            }
        }
        val startingPosition = Vector2(this.x, this.y)
        var elapsedTime = 0f
        this.addAction(object : Action() {
            override fun act(delta: Float): Boolean {
                elapsedTime = minOf(time, elapsedTime + delta)
                setPosition(startingPosition.x + elapsedTime / time * necessaryMovement.x, startingPosition.y + elapsedTime / time * necessaryMovement.y)
                if (direction == Direction.POP) {
                    setScale(1f - elapsedTime / time)
                }
                if (elapsedTime == time) {
                    removeAction(this)
                    canBeUsed = true
                    isVisible = false
                    return true
                }
                return false
            }
        })
    }

}