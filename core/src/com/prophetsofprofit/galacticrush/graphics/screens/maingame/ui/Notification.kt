package com.prophetsofprofit.galacticrush.graphics.screens.maingame.ui

import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.utils.Align
import com.prophetsofprofit.galacticrush.graphics.move
import com.prophetsofprofit.galacticrush.graphics.onClick

/**
 * An image with corresponding description that notifies the player of actions that have been taken or they can take
 */
abstract class Notification(width: Float, height: Float, image: Drawable, text: String, skin: Skin, var important: Boolean = false) : Button(image) {

    //The parent NotificationStack
    var parent: NotificationStack? = null

    init {
        this.setSize(width, height)
        this.onClick {
            println("Clicked")
            action()
        }
    }

    /**
     * How the notification appears
     */
    fun appear() {
        this.setPosition(this.parent!!.width / 2, this.parent!!.height, Align.top)
        //this.setScale(0f)
        //this.move(this.parent!!.width / 2, this., 100f, 100f, 5f)
    }

    /**
     * Drop the notification by n notification heights
     */
    fun drop(n: Int) {
        this.move(this.x, this.y - n * this.height, this.width, this.height, 2f * n)
    }

    /**
     * How the notification disappears--if important, don't let the user disappear it
     */
    fun disappear() {
        if (important)
            return
        this.move(this.x, this.y, 0f, 0f, 0.2f, { this.remove() })
    }

    /**
     * When the notification is hovered over, display the description
     */
    fun hover() {}

    /**
     * Do this when the notification is clicked
     */
    abstract fun action()

}