package com.prophetsofprofit.galacticrush.graphics.screen.experimental

import com.badlogic.gdx.scenes.scene2d.Action
import com.prophetsofprofit.galacticrush.logic.GamePhase

/**
 * An action that handles animating drone turns
 */
class DroneTurnAnimator(val gameScreen: GameScreen) : Action() {

    /**
     * Checks if the game is in the droneturn phase and then tries animating it
     */
    override fun act(delta: Float): Boolean {
        if (this.gameScreen.phase != GamePhase.DRONE_PHASE) {
            return false
        }

        return false
    }

}