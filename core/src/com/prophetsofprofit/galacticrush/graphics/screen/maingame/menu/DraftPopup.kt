package com.prophetsofprofit.galacticrush.graphics.screen.maingame.menu

import com.badlogic.gdx.scenes.scene2d.Action
import com.prophetsofprofit.galacticrush.graphics.Direction
import com.prophetsofprofit.galacticrush.graphics.ModalWindow
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.MainGameScreen
import com.prophetsofprofit.galacticrush.logic.GamePhase

/**
 * The popup that will appear when a draft is happening for the current player
 * Allows them to see their options and choose an instruction
 */
class DraftPopup(gameScreen: MainGameScreen) : ModalWindow(gameScreen, "Draft Options", gameScreen.uiCamera.viewportWidth / 2, gameScreen.uiCamera.viewportHeight / 2) {

    /**
     * Initializes all of the components of the draft popup and positions them
     */
    init {
        this.addAction(object : Action() {
            override fun act(delta: Float): Boolean {
                if (!canBeUsed) {
                    return false
                }
                if (!isVisible && gameScreen.mainGame.phase == GamePhase.DRAFT_PHASE && gameScreen.mainGame.currentDraft[gameScreen.player.id]!!.isNotEmpty()) {
                    appear(Direction.POP, 1f)
                } else if (isVisible) {
                    disappear(Direction.POP, 1f)
                }
                return false
            }
        })
    }

}