package com.prophetsofprofit.galacticrush.graphics.screen.maingame

import com.badlogic.gdx.scenes.scene2d.Actor

/**
 * Handles moving actors from place to place over a time
 */
class MovementHandler {

    //Stores a list of actors and their movement directions, in the form [dx, dy, time remaining, total time]
    val queue = mutableMapOf<Actor, Array<Float>>()

    /**
     * Adds an actor to the queue
     */
    fun add(actor: Actor, dx: Float, dy: Float, time: Float) {
        if (actor in this.queue.keys) return
        this.queue[actor] = arrayOf(dx, dy, time, time)
    }

    /**
     * Updates the positions of all the actors and decrements their times
     * If the times go to zero, remove them from the queue
     */
    fun update(delta: Float) {
        val toRemove = mutableListOf<Actor>()
        this.queue.keys.forEach {
            it.moveBy(this.queue[it]!![0] * minOf(delta, this.queue[it]!![2]) / this.queue[it]!![3], this.queue[it]!![1] * minOf(delta, this.queue[it]!![2]) / this.queue[it]!![3])
            this.queue[it]!![2] -= delta
            if (this.queue[it]!![2] <= 0) toRemove.add(it)
        }
        toRemove.forEach { this.queue.remove(it) }
    }

}
