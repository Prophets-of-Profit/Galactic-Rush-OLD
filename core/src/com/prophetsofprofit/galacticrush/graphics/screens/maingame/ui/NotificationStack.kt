package com.prophetsofprofit.galacticrush.graphics.screens.maingame.ui

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Group
import com.sun.org.apache.xpath.internal.operations.Bool

/**
 * Stores a number of notifications, handling their appearance and disappearance
 */
class NotificationStack(x: Float, y: Float, width: Float, height: Float, val maxNotifications: Int, val notificationHeight: Float): Group() {

    //Bottom notification - head of a singly linked list
    private var bottomNotification: Notification? = null
    //Last notification in the list and the list size--not necessarily displayed
    val topNotification: Pair<Notification?, Int>
        get() {
            if (this.bottomNotification == null) {
                return Pair(null, 0)
            }
            var currentNotification = this.bottomNotification
            var size = 1
            while (currentNotification!!.nextNotification != null) {
                currentNotification = currentNotification.nextNotification
                size++
            }
            return Pair(currentNotification, size)
        }

    init {
        this.debug = true

        this.setPosition(x, y)
        this.setSize(width, height)
    }

    /**
     * Adds a notification to the queue, dropping it to the first available position if there is space
     */
    fun addNotification(notification: Notification) {
        notification.parent = this
        if (this.bottomNotification == null) {
            this.bottomNotification = notification
            notification.appear()
            return
        }
        val top = this.topNotification
        top.first!!.nextNotification = notification
        if (top.second < this.maxNotifications) {
            notification.appear()
        }
    }

    /**
     * Remove a notification that is being displayed
     */
    fun removeNotification(notification: Notification) {
        if (this.bottomNotification == null) {
            return
        }
        if (this.bottomNotification == notification) {
            this.bottomNotification!!.disappear()
            this.bottomNotification = this.bottomNotification!!.nextNotification
            return
        }
        var currentNotification = this.bottomNotification
        while (currentNotification!!.nextNotification != notification) {
            currentNotification = currentNotification.nextNotification
            if (currentNotification == null) return
        }
        currentNotification.nextNotification!!.disappear()
        currentNotification.nextNotification = currentNotification.nextNotification!!.nextNotification
    }

    /**
     * Check if a given notification is being displayed
     */
    fun isDisplaying(notification: Notification?): Boolean {
        if (notification == null)
            return false
        return this.children.contains(notification)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        ShapeRenderer().also { it.setAutoShapeType(true); it.begin(); this.drawDebugBounds(it) }
    }

}