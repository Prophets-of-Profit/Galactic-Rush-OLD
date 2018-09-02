package com.prophetsofprofit.galacticrush.graphics.screen.maingame.menu

import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.prophetsofprofit.galacticrush.graphics.Direction
import com.prophetsofprofit.galacticrush.graphics.ModalWindow
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.instructiondisplays.InstructionCardDisplay
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.MainGameScreen
import com.prophetsofprofit.galacticrush.logic.GamePhase
import com.prophetsofprofit.galacticrush.logic.change.PlayerChange
import com.prophetsofprofit.galacticrush.logic.drone.instruction.Instruction
import ktx.scene2d.Scene2DSkin

/**
 * The popup that will appear when a draft is happening for the current player
 * Allows them to see their options and choose an instruction
 */
class DraftPopup(gameScreen: MainGameScreen) : ModalWindow(gameScreen, "Draft Options", gameScreen.uiCamera.viewportWidth / 2, gameScreen.uiCamera.viewportHeight / 2) {

    /**
     * Initializes all of the components of the draft popup and positions them
     */
    init {
        val optionsWhenSubmitted = mutableListOf<Instruction>()

        //The top part of the panel that displays all of the draft options TODO: change
        val instructionImageContainer = Table().also {
            it.addAction(object : Action() {
                override fun act(delta: Float): Boolean {
                    if (isVisible) {
                        return false
                    }
                    it.clearChildren()
                    gameScreen.mainGame.currentDraft[gameScreen.player.id]!!.forEach { instruction ->
                        it.add(Button(InstructionCardDisplay(instruction), Scene2DSkin.defaultSkin).also {
                            it.addListener(object : ChangeListener() {
                                override fun changed(event: ChangeEvent, actor: Actor) {
                                    optionsWhenSubmitted.clear()
                                    optionsWhenSubmitted.addAll(gameScreen.mainGame.currentDraft[gameScreen.player.id]!!)
                                    gameScreen.player.submit(PlayerChange(gameScreen.player.id).also {
                                        it.gainedInstructions.add(instruction)
                                    })
                                    disappear(Direction.POP, 1f)
                                }
                            })
                            //TODO: Maintain aspect ratio
                        }).expand().fill().pad(5f)
                    }
                    return false
                }
            })
        }
        this.add(instructionImageContainer).expand().fill()

        //The action that control's the modal's visibility
        this.addAction(object : Action() {
            override fun act(delta: Float): Boolean {
                if (!canBeUsed) {
                    return false
                }
                if (!isVisible &&
                        gameScreen.mainGame.phase == GamePhase.DRAFT_PHASE &&
                        gameScreen.mainGame.currentDraft[gameScreen.player.id]!!.isNotEmpty() &&
                        gameScreen.mainGame.currentDraft[gameScreen.player.id]!! != optionsWhenSubmitted &&
                        gameScreen.mainGame.droneTurnChanges.isEmpty() &&
                        gameScreen.turnAnimationHandler.currentlyMoving.isEmpty()
                ) {
                    children.forEach { it.act(0f) }
                    appear(Direction.POP, 1f)
                } else if (isVisible && gameScreen.mainGame.phase != GamePhase.DRAFT_PHASE) {
                    disappear(Direction.POP, 1f)
                }
                return false
            }
        })
    }

}