package com.prophetsofprofit.galacticrush.graphics.screen.maingame.menu

import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.prophetsofprofit.galacticrush.graphics.Direction
import com.prophetsofprofit.galacticrush.graphics.ModalWindow
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.InstructionCardDisplay
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.MainGameScreen
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
        val displayedInstructions = mutableListOf<InstructionInstance>()

        //The field that controls the drone's name
        val nameField = TextField("", Scene2DSkin.defaultSkin)

        fun resetDroneButtons(table: Table): Boolean {
            table.clearChildren()
            instructionsCopy.forEach { instructionInstance ->
                table.add(Button(InstructionCardDisplay(instructionInstance.baseInstruction), Scene2DSkin.defaultSkin).also {
                    it.addListener(object : ChangeListener() {
                        override fun changed(event: ChangeEvent, actor: Actor) {
                            instructionsCopy.remove(instructionInstance)
                            displayedInstructions.clear()
                            displayedInstructions.addAll(instructionsCopy)
                            resetDroneButtons(table)
                        }
                    })
                    //TODO: Maintain aspect ratio
                }).expand().fill().pad(5f)
            }
            return false
        }

        //The table that contains the drone's instructions
        val droneInstructionTable = Table().also {
            //TODO: not this
            it.addAction(object : Action() {
                override fun act(delta: Float): Boolean {
                    if (isVisible) {
                        return false
                    }
                    return resetDroneButtons(it)
                }
            })
        }

        //The table that contains the player's unlocked instructions
        val ownedInstructionsTable = Table().also {
            it.addAction(object : Action() {
                override fun act(delta: Float): Boolean {
                    if (isVisible) {
                        return false
                    }
                    it.clearChildren()
                    gameScreen.mainGame.unlockedInstructions[gameScreen.player.id]!!.forEach { instruction ->
                        it.add(Button(InstructionCardDisplay(instruction), Scene2DSkin.defaultSkin).also {
                            it.addListener(object : ChangeListener() {
                                override fun changed(event: ChangeEvent, actor: Actor) {
                                    instructionsCopy.add(InstructionInstance(instruction))
                                    resetDroneButtons(droneInstructionTable)
                                }
                            })
                            //TODO: Maintain aspect ratio
                        }).expand().fill().pad(5f)
                    }
                    return false
                }
            })
        }

        //Formats all of the actions
        this.add(nameField).expand().height(50f).top().row()
        this.add(ownedInstructionsTable).expand().fill().row()
        this.add(droneInstructionTable).expand().fill().row()

        //The action that controls the modal's visibility
        this.addAction(object : Action() {
            override fun act(delta: Float): Boolean {
                if (!canBeUsed) {
                    return false
                }
                if (editingId != gameScreen.selectedDroneId) {
                    nameField.text = gameScreen.selectedDrone?.name ?: ""
                    instructionsCopy.clear()
                    instructionsCopy.addAll(gameScreen.selectedDrone?.instructions ?:mutableListOf<InstructionInstance>())
                    editingId = gameScreen.selectedDroneId
                }
                if (!isVisible && gameScreen.programming) {
                    children.forEach { it.act(0f) }
                    editingId = gameScreen.selectedDroneId
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