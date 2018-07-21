package com.prophetsofprofit.galacticrush.graphics.screen.maingame.overlay

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.MainGameScreen
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.overlay.planetoverlay.PlanetOverlay
import com.prophetsofprofit.galacticrush.logic.map.Attribute
import ktx.scene2d.Scene2DSkin

/**
 * The actor group which makes up the overlay of the main game
 * Must always be tied to a main game, should not be placed in another screen
 */
class Overlay(val gameScreen: MainGameScreen): Group() {

    //The button pressed to submit a player's turn
    val endTurnButton = TextButton("Submit", Scene2DSkin.defaultSkin)
    //The button which brings up the main game menu
    val gameMenuButton = TextButton("Game Menu", Scene2DSkin.defaultSkin)
    //The overlay that comes up when planets are selected
    val planetOverlay = PlanetOverlay(gameScreen)

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
        this.addActor(planetOverlay)
    }

}