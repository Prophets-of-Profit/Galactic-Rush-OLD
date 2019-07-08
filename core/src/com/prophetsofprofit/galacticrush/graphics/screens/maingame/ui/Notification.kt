package com.prophetsofprofit.galacticrush.graphics.screens.maingame.ui

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.utils.Align
import com.prophetsofprofit.galacticrush.graphics.move
import kotlin.math.min

/**
 * An image with corresponding description that notifies the player of actions that have been taken or they can take
 */
abstract class Notification(width: Float, height: Float, image: Drawable, text: String, skin: Skin, var important: Boolean = false) : Button(image) {

    //The parent NotificationStack
    var parent: NotificationStack? = null
    //The notification above this
    var nextNotification: Notification? = null

    init {
        this.setSize(width, height)
        this.isTransform = true
        this.addListener(object : ClickListener() {

            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                this@Notification.action()
            }

            override fun enter(event: InputEvent?, x: Float, y: Float, pointer: Int, fromActor: Actor?) {
                this@Notification.hover()
            }

            override fun exit(event: InputEvent?, x: Float, y: Float, pointer: Int, toActor: Actor?) {
                this@Notification.stopHover()
            }

        })
    }

    /**
     * How the notification appears
     */
    fun appear() {
        this.setPosition(this.parent!!.width, this.parent!!.height, Align.topRight)
        this.setScale(0f)
        this.parent!!.addActor(this)
        println(this.parent!!.topNotification.second)
        this.move(this.parent!!.width, this.parent!!.notificationHeight * min(this.parent!!.topNotification.second, this.parent!!.maxNotifications) - 1, this.parent!!.notificationHeight, this.parent!!.notificationHeight, 0.2f, Align.bottomRight)
    }

    /**
     * Drop the notification by one notification height, and drop all displayed notifications above it
     */
    fun drop() {
        this.move(this.x, this.y - this.height, this.width, this.height, 0.2f)
        if (this.nextNotification == null)
            return
        if (this.parent!!.isDisplaying(this.nextNotification)) {
            this.nextNotification!!.drop()
            return
        }
        this.nextNotification!!.appear()
    }

    /**
     * How the notification disappears--if important, don't let the user disappear it
     */
    fun disappear() {
        if (important)
            return
        this.move(this.x, this.y, 0f, 0f, 0.2f, {
            if (this.nextNotification != null) {
                if (this.parent!!.isDisplaying(this.nextNotification)) {
                    this.nextNotification!!.drop()
                } else {
                    this.nextNotification!!.appear()
                }
            }
            this.remove()
        })
    }

    /**
     * When the notification is hovered over, display the description
     */
    fun hover() {}

    /**
     * When the notification is no longer being hovered over, stop displaying the description
     */
    fun stopHover(){}

    /**
     * Do this when the notification is clicked
     */
    abstract fun action()

}