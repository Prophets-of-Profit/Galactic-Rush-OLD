package com.prophetsofprofit.galacticrush.graphics.screen.experimental

import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.prophetsofprofit.galacticrush.graphics.Panel
import com.prophetsofprofit.galacticrush.logic.GamePhase
import ktx.scene2d.Scene2DSkin

/**
 * A class that represents the panel at the top which shows general game information
 */
class GeneralInformationPanel(gameScreen: GameScreen) : Panel(gameScreen, "General Information", 0f, gameScreen.uiCamera.viewportHeight, gameScreen.uiCamera.viewportWidth, gameScreen.uiCamera.viewportHeight * 0.075f, Align.topLeft) {

    /**
     * Initializes the general information panel with all of the actors and positionings
     */
    init {
        //Game Phase display
        this.add(Label("Game Phase ", Scene2DSkin.defaultSkin))
        this.add(Label("", Scene2DSkin.defaultSkin, "ui").also {
            it.addAction(object : Action() {
                override fun act(delta: Float): Boolean {
                    it.setText(when (gameScreen.phase) {
                        GamePhase.DRAFT_PHASE -> "Draft"
                        GamePhase.DRONE_PHASE -> "Autonomous"
                        GamePhase.PLAYER_FREE_PHASE -> "Player"
                    })
                    return false
                }
            })
        })
    }

}