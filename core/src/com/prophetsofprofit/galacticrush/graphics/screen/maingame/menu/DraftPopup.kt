package com.prophetsofprofit.galacticrush.graphics.screen.maingame.menu

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.prophetsofprofit.galacticrush.graphics.Direction
import com.prophetsofprofit.galacticrush.graphics.ModalWindow
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.MainGameScreen
import com.prophetsofprofit.galacticrush.instructionSprites
import com.prophetsofprofit.galacticrush.logic.GamePhase
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
        //The top part of the panel that displays all of the draft options
        val instructionSprites = HorizontalGroup().also {
            val texturesOfCurrentDraft = mutableListOf<Texture>()
            it.addAction(object : Action() {
                override fun act(delta: Float): Boolean {
                    println(gameScreen.mainGame.currentDraft[gameScreen.player.id])
                    texturesOfCurrentDraft.clear()
                    texturesOfCurrentDraft.addAll(
                            gameScreen.mainGame.currentDraft[gameScreen.player.id]!!
                                    .map { instructionSprites[it]!! }
                                    .toMutableList()
                    )
                    it.clearChildren()
                    texturesOfCurrentDraft.forEach { texture -> it.addActor(Image(texture)) }
                    return false
                }
            })
        }
        this.add(instructionSprites)

        //The action that control's the modal's visibility
        this.addAction(object : Action() {
            override fun act(delta: Float): Boolean {
                if (!canBeUsed) {
                    return false
                }
                if (!isVisible && gameScreen.mainGame.phase == GamePhase.DRAFT_PHASE && gameScreen.mainGame.currentDraft[gameScreen.player.id]!!.isNotEmpty()) {
                    appear(Direction.POP, 1f)
                } else if (isVisible && gameScreen.mainGame.phase != GamePhase.DRAFT_PHASE) {
                    disappear(Direction.POP, 1f)
                }
                return false
            }
        })
    }

}