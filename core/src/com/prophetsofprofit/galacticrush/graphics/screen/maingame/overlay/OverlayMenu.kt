package com.prophetsofprofit.galacticrush.graphics.screen.maingame.overlay

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.MainGameScreen
import com.prophetsofprofit.galacticrush.logic.map.Attribute
import ktx.scene2d.Scene2DSkin

/**
 * The actor group which makes up the overlay of the main game
 * Must always be tied to a main game, should not be placed in another screen
 */
class OverlayMenu(val gameScreen: MainGameScreen): Group() {

    //The label for the drones on the currently selected planet
    val dronesList = Label("Drones", Scene2DSkin.defaultSkin, "ui")
    //The button pressed to submit a player's turn
    val endTurnButton = TextButton("Submit", Scene2DSkin.defaultSkin)
    //The button which brings up the main game menu
    val gameMenuButton = TextButton("Game Menu", Scene2DSkin.defaultSkin)

    init {
        this.dronesList.width = this.gameScreen.game.camera.viewportWidth / 8
        this.dronesList.height = this.gameScreen.game.camera.viewportHeight / 3
        this.dronesList.setPosition(this.gameScreen.game.camera.viewportWidth - this.dronesList.width,
                this.gameScreen.game.camera.viewportHeight / 3)
        this.dronesList.setAlignment(Align.top)

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

        this.addActor(this.dronesList)

        this.addActor(this.endTurnButton)
        this.addActor(this.gameMenuButton)
    }

}