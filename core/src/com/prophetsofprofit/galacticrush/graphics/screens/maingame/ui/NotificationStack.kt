package com.prophetsofprofit.galacticrush.graphics.screens.maingame.ui

import com.badlogic.gdx.scenes.scene2d.Group
import kotlin.math.min

/**
 * Stores a number of notifications, handling their appearance and disappearance
 */
class NotificationStack(x: Float, y: Float, width: Float, height: Float, private val maxNotifications: Int, val notificationHeight: Float): Group() {

    //Notifications that are being processed are stored here
    val notificationsQueue = mutableListOf<Notification>()

    init {
        this.setPosition(x, y)
        this.setSize(width, height)
    }

    /**
     * Adds a notification to the queue, dropping it to the first available position if there is space
     */
    fun addNotification(notification: Notification) {
        notification.parent = this
        val index = notificationsQueue.size
        this.notificationsQueue.add(notification)
        this.addActor(notification)
        println("Add actor: index: $index")
        if (index < this.maxNotifications) {
            notification.appear()
            println("Appear: index: $index")
            notification.drop(this.maxNotifications - index)
        }
    }

    /**
     * Remove a notification that is being displayed, dropping all notifications above it
     */
    fun removeNotification(notification: Notification) {
        val index = this.notificationsQueue.indexOf(notification)
        notification.disappear()
        ((index + 1) until min(this.maxNotifications, this.notificationsQueue.size)).forEach { this.notificationsQueue[it].drop(1) }
        if (this.notificationsQueue.size > this.maxNotifications)
            this.notificationsQueue[this.maxNotifications].appear()
        this.notificationsQueue.remove(notification)
    }

}