package com.prophetsofprofit.galacticrush.graphics

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align

/**
 * An extension function that adds a change listener to a button
 */
fun Actor.onClick(action: () -> Unit) {
    this.addListener(object : ChangeListener() {
        override fun changed(event: ChangeEvent?, actor: Actor?) {
            action()
        }
    })
}

/**
 * An extension function that adds an action to an actor that periodically happens
 */
fun Actor.act(action: (delta: Float) -> Unit) {
    this.addAction(object : Action() {
        override fun act(delta: Float): Boolean {
            action(delta)
            return false
        }
    })
}

/**
 * Animate the actor's movement to the target location
 * Linear for now
 */
fun Actor.move(x: Float, y: Float, width: Float, height: Float, time: Float, align: Int = Align.bottomLeft) {
    val move = MoveToAction()
    move.setPosition(x, y, align)
    move.duration = time
    val scale = ScaleToAction()
    scale.duration = time
    scale.setScale(width / this.width, height / this.height)
    this.addAction(ParallelAction(move, scale))
}

/**
 * Animate the actor's movement to the target location rectangle
 * Linear for now
 */
fun Actor.move(location: Rectangle, time: Float) {
    this.move(location.x, location.y, location.width, location.height, time)
}

/**
 * Animate the actor's movement to the target location, executing a command on ending
 */
fun Actor.move(x: Float, y: Float, width: Float, height: Float, time: Float, action: () -> Unit, align: Int = Align.bottomLeft) {
    val move = MoveToAction()
    move.setPosition(x, y, align)
    move.duration = time
    val scale = ScaleToAction()
    scale.duration = time
    scale.setScale(width / this.width, height / this.height)
    this.addAction(ParallelAction(move, scale, EndAction(time, action)))
}

/**
 * Animate the actor's movement to the target location rectangle, executing a command on ending
 */
fun Actor.move(location: Rectangle, time: Float, action: () -> Unit) {
    this.move(location.x, location.y, location.width, location.height, time, action)
}

/**
 * An Action that waits for the given time and then performs the input action
 */
class EndAction(var time: Float, val action: () -> Unit): Action() {

    override fun act(delta: Float): Boolean {
        this.time -= delta
        if (this.time <= 0) {
            this.action()
            this.actor.removeAction(this)
            return true
        }
        return false
    }

}