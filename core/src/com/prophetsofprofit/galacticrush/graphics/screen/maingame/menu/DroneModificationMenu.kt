package com.prophetsofprofit.galacticrush.graphics.screen.maingame.menu

import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.prophetsofprofit.galacticrush.graphics.Direction
import com.prophetsofprofit.galacticrush.graphics.ModalWindow
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.MainGameScreen
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.instructiondisplays.DroneQueueDisplay
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.instructiondisplays.InstructionCardDisplay
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.instructiondisplays.InstructionInventoryDisplay
import com.prophetsofprofit.galacticrush.logic.drone.DroneId
import com.prophetsofprofit.galacticrush.logic.drone.instruction.InstructionInstance
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

        //The instructions that the drone will have if the submit button is pressed
        val instructionsCopy = mutableListOf<InstructionInstance>()

        //The field that controls the drone's name
        val nameField = TextField("", Scene2DSkin.defaultSkin)

        //The table that displays the drone's instructions
        //TODO: take another look at scrolling
        val droneInstructionsQueue = ScrollPane(DroneQueueDisplay(50, instructionsCopy))

        //The table that displays the user's owned instructions
        val ownedInstructionsPanel = ScrollPane(InstructionInventoryDisplay(5, gameScreen) { instruction ->
            instructionsCopy.add(InstructionInstance(instruction))
            (droneInstructionsQueue.actor as DroneQueueDisplay).update()
            droneInstructionsQueue.scrollTo(droneInstructionsQueue.actor.width - droneInstructionsQueue.width, 0f, droneInstructionsQueue.width, droneInstructionsQueue.height)
        })

        //Formats all of the actions
        this.add(nameField).expand().height(50f).top().row()
        this.add(ownedInstructionsPanel).expand().fill().prefWidth(this.width).row()
        this.add(droneInstructionsQueue).expand().height(50f).fill().prefHeight(this.height)

        //The action that controls the modal's visibility
        this.addAction(object : Action() {
            override fun act(delta: Float): Boolean {
                if (!canBeUsed) {
                    return false
                }
                if (editingId != gameScreen.selectedDroneId) {
                    nameField.text = gameScreen.selectedDrone?.name ?: ""
                    instructionsCopy.clear()
                    instructionsCopy.addAll(gameScreen.selectedDrone?.instructions
                            ?: mutableListOf<InstructionInstance>())
                    editingId = gameScreen.selectedDroneId
                }
                if (!isVisible && gameScreen.programming) {
                    children.forEach { it.act(0f) }
                    editingId = gameScreen.selectedDroneId
                    (ownedInstructionsPanel.actor as InstructionInventoryDisplay).update()
                    (droneInstructionsQueue.actor as DroneQueueDisplay).update()
                    appear(Direction.POP, 1f)
                } else if (isVisible && !gameScreen.programming || gameScreen.selectedDrone == null) {
                    if (gameScreen.selectedDrone != null) {
                        editingId = null
                        gameScreen.selectedDrone!!.name = nameField.text
                        gameScreen.selectedDrone!!.instructions.clear()
                        gameScreen.selectedDrone!!.instructions.addAll(instructionsCopy)
                        gameScreen.player.currentChanges.changedDrones.add(gameScreen.selectedDrone!!)
                    } else {
                        editingId = null
                        gameScreen.programming = false
                    }
                    instructionsCopy.clear()
                    disappear(Direction.POP, 1f)
                }
                return false
            }
        })
    }

}