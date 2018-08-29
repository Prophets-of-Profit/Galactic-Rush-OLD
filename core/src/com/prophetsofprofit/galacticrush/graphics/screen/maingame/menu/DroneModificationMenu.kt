package com.prophetsofprofit.galacticrush.graphics.screen.maingame.menu

import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.prophetsofprofit.galacticrush.graphics.Direction
import com.prophetsofprofit.galacticrush.graphics.ModalWindow
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.MainGameScreen
import com.prophetsofprofit.galacticrush.logic.drone.DroneId
import ktx.scene2d.Scene2DSkin

/**
 * The menu where the user can modify their drone
 */
class DroneModificationMenu(gameScreen: MainGameScreen) : ModalWindow(gameScreen, "Drone Modification", gameScreen.uiCamera.viewportWidth / 2, gameScreen.uiCamera.viewportHeight / 2) {

    /**
     * Initializes all relevant components and handles appearing and disappearing
     */
    init {

        //The drone currently being edited
        var editingId: DroneId? = null

        //The field that controls the drone's name
        val nameField = TextField("", Scene2DSkin.defaultSkin)

        //Formats all of the actions
        val actionsTable = Table().also {
            it.add(nameField)
        }
        this.add(actionsTable)

        //The action that controls the modal's visibility
        this.addAction(object : Action() {
            override fun act(delta: Float): Boolean {
                if (!canBeUsed) {
                    return false
                }
                if (editingId != gameScreen.selectedDroneId) {
                    nameField.text = gameScreen.selectedDrone?.name ?: ""
                    editingId = gameScreen.selectedDroneId
                }
                if (!isVisible && gameScreen.programming) {
                    children.forEach { it.act(0f) }
                    editingId = gameScreen.selectedDroneId
                    appear(Direction.POP, 1f)
                } else if (isVisible && !gameScreen.programming) {
                    if (gameScreen.selectedDrone != null) {
                        editingId = null
                        gameScreen.selectedDrone!!.name = nameField.text
                        //gameScreen.selectedDrone!!.instructions.clear()
                        //gameScreen.selectedDrone!!.instructions.addAll(TODO)
                    }
                    disappear(Direction.POP, 1f)
                }
                return false
            }
        })
    }

}