package com.prophetsofprofit.galacticrush.graphics

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener

/**
 * An extension function that adds a change listener to a button
 */
fun Button.onClick(action: () -> Unit) {
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
 * Animate the window's movement to the target location
 * Linear for now
 */
fun Actor.move(x: Float, y: Float, width: Float, height: Float, time: Float) {
    val move = MoveToAction()
    move.setPosition(x, y)
    move.duration = time
    val scale = ScaleToAction()
    scale.duration = time
    scale.setScale(width / this.width, height / this.height)
    this.addAction(ParallelAction(move, scale))
}

/**
 * Animate the window's movement to the target location rectangle
 * Linear for now
 */
fun Actor.move(location: Rectangle, time: Float) {
    this.move(location.x, location.y, location.width, location.height, time)
}
