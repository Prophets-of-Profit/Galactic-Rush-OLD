package com.prophetsofprofit.galacticrush.graphics

import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Actor
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
fun Actor.act(action: () -> Unit) {
    this.addAction(object : Action() {
        override fun act(delta: Float): Boolean {
            action()
            return false
        }
    })
}
