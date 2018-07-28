package com.prophetsofprofit.galacticrush.graphics.screen.maingame

import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Actor

/**
 * Handles moving actors from place to place over a time
 */
object MovementHandler {

    val currentlyMoving = mutableListOf<Actor>()

    /**
     * Adds an actor to the queue
     */
    fun add(actor: Actor, dx: Float, dy: Float, time: Float) {
        if (this.currentlyMoving.contains(actor)) {
            return
        }
        currentlyMoving.add(actor)
        var timeRemaining = time
        actor.addAction(object: Action() {
            override fun act(delta: Float): Boolean {
                if (!currentlyMoving.contains(actor)) {
                    return true
                }
                val elapsedTime = minOf(delta, timeRemaining)
                actor.moveBy(dx * elapsedTime / time, dy * elapsedTime / time)
                timeRemaining -= delta
                if (timeRemaining <= 0) {
                    currentlyMoving.remove(actor)
                    return true
                }
                return false
            }
        })
    }

}
