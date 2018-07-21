package com.prophetsofprofit.galacticrush.graphics.screen.maingame

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

    //The label for the currently selected planet
    val planetLabel = Label("Planet Stats", Scene2DSkin.defaultSkin, "ui")
    //The label for the drones on the currently selected planet
    val dronesList = Label("Drones", Scene2DSkin.defaultSkin, "ui")
    //The button pressed to submit a player's turn
    val endTurnButton = TextButton("Submit", Scene2DSkin.defaultSkin)
    //The button which brings up the main game menu
    val gameMenuButton = TextButton("Game Menu", Scene2DSkin.defaultSkin)
    //The icons and labels for planet information
    val massIcon = ImageButton(Scene2DSkin.defaultSkin, "mass")
    val temperatureIcon = ImageButton(Scene2DSkin.defaultSkin, "temp")
    val atmosphereIcon = ImageButton(Scene2DSkin.defaultSkin, "atm")
    val humidityIcon = ImageButton(Scene2DSkin.defaultSkin, "humid")
    val solidityIcon = ImageButton(Scene2DSkin.defaultSkin, "solid")
    val massLabel = Label("", Scene2DSkin.defaultSkin, "small")
    val temperatureLabel = Label("", Scene2DSkin.defaultSkin, "small")
    val atmosphereLabel = Label("", Scene2DSkin.defaultSkin, "small")
    val humidityLabel = Label("", Scene2DSkin.defaultSkin, "small")
    val solidityLabel = Label("", Scene2DSkin.defaultSkin, "small")

    init {
        this.dronesList.width = this.gameScreen.game.camera.viewportWidth / 8
        this.dronesList.height = this.gameScreen.game.camera.viewportHeight / 3
        this.dronesList.setPosition(this.gameScreen.game.camera.viewportWidth - this.dronesList.width,
                this.gameScreen.game.camera.viewportHeight / 3)
        this.dronesList.setAlignment(Align.top)
        this.planetLabel.width = this.gameScreen.game.camera.viewportWidth / 8
        this.planetLabel.height = this.gameScreen.game.camera.viewportHeight / 3
        this.planetLabel.setPosition(this.gameScreen.game.camera.viewportWidth - this.dronesList.width,
                this.gameScreen.game.camera.viewportHeight - this.planetLabel.height)
        this.planetLabel.setAlignment(Align.top)

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

        this.massIcon.setPosition(this.planetLabel.x + this.massIcon.width / 5f, this.planetLabel.y + 9f * this.massIcon.height)
        this.temperatureIcon.setPosition(this.planetLabel.x + this.temperatureIcon.width / 5f, this.planetLabel.y + 7f * this.temperatureIcon.height)
        this.atmosphereIcon.setPosition(this.planetLabel.x + this.atmosphereIcon.width / 5f, this.planetLabel.y + 5f * this.atmosphereIcon.height)
        this.humidityIcon.setPosition(this.planetLabel.x + this.humidityIcon.width / 5f, this.planetLabel.y + 3f * this.humidityIcon.height)
        this.solidityIcon.setPosition(this.planetLabel.x + this.solidityIcon.width / 5f, this.planetLabel.y + 1f * this.solidityIcon.height)
        this.massLabel.setPosition(this.planetLabel.x + 2f * this.massIcon.width, this.planetLabel.y + 9.5f * this.massIcon.height)
        this.temperatureLabel.setPosition(this.planetLabel.x + 2f * this.temperatureIcon.width, this.planetLabel.y + 7.5f * this.temperatureIcon.height)
        this.atmosphereLabel.setPosition(this.planetLabel.x + 2f * this.atmosphereIcon.width, this.planetLabel.y + 5.5f * this.atmosphereIcon.height)
        this.humidityLabel.setPosition(this.planetLabel.x + 2f * this.humidityIcon.width, this.planetLabel.y + 3.5f * this.humidityIcon.height)
        this.solidityLabel.setPosition(this.planetLabel.x + 2f * this.solidityIcon.width, this.planetLabel.y + 1.5f * this.solidityIcon.height)

        this.addActor(this.planetLabel)
        this.addActor(this.dronesList)
        this.addActor(this.massIcon)
        this.addActor(this.temperatureIcon)
        this.addActor(this.atmosphereIcon)
        this.addActor(this.humidityIcon)
        this.addActor(this.solidityIcon)
        this.addActor(this.massLabel)
        this.addActor(this.temperatureLabel)
        this.addActor(this.atmosphereLabel)
        this.addActor(this.humidityLabel)
        this.addActor(this.solidityLabel)
        this.addActor(this.endTurnButton)
        this.addActor(this.gameMenuButton)
    }

    /**
     * Updates all of the information found on the overlay
     * Uses the game screen's selected planet
     * TODO: Implement drone information and editing pieces
     */
    fun updateInformation() {
        if (this.gameScreen.selectedPlanet != null) {
            this.massLabel.setText(Attribute.MASS.stringValue(this.gameScreen.selectedPlanet!!.attributes[Attribute.MASS]!!))
            this.temperatureLabel.setText(Attribute.TEMPERATURE.stringValue(this.gameScreen.selectedPlanet!!.attributes[Attribute.TEMPERATURE]!!))
            this.atmosphereLabel.setText(Attribute.ATMOSPHERE.stringValue(this.gameScreen.selectedPlanet!!.attributes[Attribute.ATMOSPHERE]!!))
            this.humidityLabel.setText(Attribute.WATER.stringValue(this.gameScreen.selectedPlanet!!.attributes[Attribute.WATER]!!))
            this.solidityLabel.setText(Attribute.SOLIDITY.stringValue(this.gameScreen.selectedPlanet!!.attributes[Attribute.SOLIDITY]!!))
        } else {
            this.massLabel.setText("")
            this.temperatureLabel.setText("")
            this.atmosphereLabel.setText("")
            this.humidityLabel.setText("")
            this.solidityLabel.setText("")
        }
    }

}