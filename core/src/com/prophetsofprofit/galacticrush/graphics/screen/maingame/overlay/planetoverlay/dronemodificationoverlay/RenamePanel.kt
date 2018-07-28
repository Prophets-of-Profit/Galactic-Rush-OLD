package com.prophetsofprofit.galacticrush.graphics.screen.maingame.overlay.planetoverlay.dronemodificationoverlay

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.utils.DragListener
import com.prophetsofprofit.galacticrush.logic.drone.Drone
import ktx.scene2d.Scene2DSkin

/**
 * Allows for renaming a drone
 */
class RenamePanel: Group() {

    //Which drone is being renamed at the moment
    var drone: Drone? = null
    //Test
    val textField = TextField("", Scene2DSkin.defaultSkin)

    init {
        this.textField.addListener(object: DragListener() {
            override fun touchDragged(event: InputEvent?, x: Float, y: Float, pointer: Int) {
                textField.moveBy(x, y)
            }
        })
        this.textField.setPosition(800f, 500f)
        this.addActor(textField)
    }

    /**
     * Updates the panel's information
     */
    fun update() {
        this.textField.text = this.drone!!.name
    }

    /**
     * Executes the changes to the drone
     */
    fun confirm() {
        this.drone!!.name = this.textField.text
    }

}