package com.prophetsofprofit.galacticrush.graphics.screen.maingame.overlay

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.MainGameScreen
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.overlay.planetoverlay.PlanetOverlay
import ktx.scene2d.Scene2DSkin

/**
 * Handles displaying all of the actions the player can take at any time in the game
 * e.g. submit turns, pause menu
 */
class OverlayMenu(val gameScreen: MainGameScreen): Group() {

    //The button pressed to submit a player's turn
    val endTurnButton = TextButton("Submit", Scene2DSkin.defaultSkin)
    //The button which brings up the main game menu
    val gameMenuButton = TextButton("Game Menu", Scene2DSkin.defaultSkin)

    init {
        this.endTurnButton.setPosition(this.gameScreen.game.camera.viewportWidth - this.endTurnButton.width, 0f)
        this.gameMenuButton.setPosition(this.gameScreen.game.camera.viewportWidth - this.endTurnButton.width, this.endTurnButton.height)
        this.endTurnButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                gameScreen.submitConfirmation.isVisible = true
            }
        })
        this.gameMenuButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                gameScreen.gameMenu.isVisible = true
            }
        })
        this.addActor(this.endTurnButton)
        this.addActor(this.gameMenuButton)
    }

}
