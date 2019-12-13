package com.prophetsofprofit.galacticrush.graphics.screens.maingame.ui

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
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
    //The label associated with this notification
    var label = Label(text, skin, "title")

    init {
        this.setSize(width, height)
        this.isTransform = true
        //Add label
        this.label.setSize(height * 3, height)
        println("x ${this.label.x}, y ${this.label.y} this ${this.x} ${this.y}")
        //Listen for hovering and clicks
        this.addListener(object : ClickListener() {

            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                this@Notification.action()
            }

            override fun enter(event: InputEvent?, x: Float, y: Float, pointer: Int, fromActor: Actor?) {
                this@Notification.hover()
            }

            override fun exit(event: InputEvent?, x: Float, y: Float, pointer: Int, toActor: Actor?) {
                val localMousePosition = this@Notification.label.stageToLocalCoordinates(
                        this@Notification.localToStageCoordinates(Vector2(x, y))
                )
                if (this@Notification.label.hit(localMousePosition.x, localMousePosition.y, false) != null) return
                this@Notification.stopHover()
            }

        })
        this.label.addListener(object : ClickListener() {

            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                this@Notification.action()
            }

            override fun exit(event: InputEvent?, x: Float, y: Float, pointer: Int, toActor: Actor?) {
                val localMousePosition = this@Notification.stageToLocalCoordinates(
                        this@Notification.label.localToStageCoordinates(Vector2(x, y))
                )
                if (this@Notification.hit(localMousePosition.x, localMousePosition.y, false) != null) return
                this@Notification.stopHover()
            }

        })
    }

    /**
     * How the notification appears
     */
    fun appear() {
        this.setPosition(this.parent!!.width, this.parent!!.height, Align.topRight)
        this.parent!!.addActor(this)
        this.parent!!.addActor(this.label)
        this.move(this.parent!!.width,
                this.parent!!.notificationHeight * min(this.parent!!.topNotification.second,
                this.parent!!.maxNotifications) - 1,
                this.parent!!.notificationHeight,
                this.parent!!.notificationHeight,
                0.2f,
                Align.bottomRight)
        println("${this.x + this.width}, ${this.y}")
        this.label.setPosition(this.x + this.width, this.y, Align.bottomLeft)
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
        if (this.important)
            return
        this.move(this.x + this.width / 2, this.y + this.height / 2, 1f, 1f, 0.2f, {
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
    fun hover() {
        println("enter")
        this.label.move(this.x, this.y, this.height * 3, this.height, 1f, Align.bottomRight)
    }

    /**
     * When the notification is no longer being hovered over, stop displaying the description
     */
    fun stopHover(){
        println("exit")
        this.label.move(this.x * 5, this.y, this.height * 3, this.height, 1f, {}, Align.bottomRight)
    }

    /**
     * Do this when the notification is clicked
     */
    abstract fun action()

}