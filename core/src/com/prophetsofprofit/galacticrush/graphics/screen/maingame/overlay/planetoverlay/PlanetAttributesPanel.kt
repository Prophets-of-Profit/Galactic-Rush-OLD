package com.prophetsofprofit.galacticrush.graphics.screen.maingame.overlay.planetoverlay

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.MainGameScreen
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.Panel
import com.prophetsofprofit.galacticrush.logic.map.Attribute
import ktx.scene2d.Scene2DSkin

/**
 * The actor group which displays planet attributes (such as temperature, mass, etc.) in a label
 */
class PlanetAttributesPanel(gameScreen: MainGameScreen, labelWidth: Float, labelHeight: Float, yOffset: Float): Panel(gameScreen, "Planet Stats", labelWidth, labelHeight, gameScreen.uiCamera.viewportWidth, gameScreen.uiCamera.viewportHeight - yOffset, Align.topLeft) {

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

    /**
     * Sets positionings and adds children
     */
    init {
        this.massIcon.setPosition(this.baseLabel.x + this.massIcon.width / 5f, this.baseLabel.y + 9f * this.massIcon.height)
        this.temperatureIcon.setPosition(this.baseLabel.x + this.temperatureIcon.width / 5f, this.baseLabel.y + 7f * this.temperatureIcon.height)
        this.atmosphereIcon.setPosition(this.baseLabel.x + this.atmosphereIcon.width / 5f, this.baseLabel.y + 5f * this.atmosphereIcon.height)
        this.humidityIcon.setPosition(this.baseLabel.x + this.humidityIcon.width / 5f, this.baseLabel.y + 3f * this.humidityIcon.height)
        this.solidityIcon.setPosition(this.baseLabel.x + this.solidityIcon.width / 5f, this.baseLabel.y + 1f * this.solidityIcon.height)
        this.massLabel.setPosition(this.baseLabel.x + 2f * this.massIcon.width, this.baseLabel.y + 9.5f * this.massIcon.height)
        this.temperatureLabel.setPosition(this.baseLabel.x + 2f * this.temperatureIcon.width, this.baseLabel.y + 7.5f * this.temperatureIcon.height)
        this.atmosphereLabel.setPosition(this.baseLabel.x + 2f * this.atmosphereIcon.width, this.baseLabel.y + 5.5f * this.atmosphereIcon.height)
        this.humidityLabel.setPosition(this.baseLabel.x + 2f * this.humidityIcon.width, this.baseLabel.y + 3.5f * this.humidityIcon.height)
        this.solidityLabel.setPosition(this.baseLabel.x + 2f * this.solidityIcon.width, this.baseLabel.y + 1.5f * this.solidityIcon.height)
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
    }

    /**
     * Updates all of the information found on the overlay
     * Uses the game screen's selected planet
     */
    fun update() {
        this.massLabel.setText(Attribute.MASS.stringValue(this.screen.selectedPlanet!!.attributes[Attribute.MASS]!!))
        this.temperatureLabel.setText(Attribute.TEMPERATURE.stringValue(this.screen.selectedPlanet!!.attributes[Attribute.TEMPERATURE]!!))
        this.atmosphereLabel.setText(Attribute.ATMOSPHERE.stringValue(this.screen.selectedPlanet!!.attributes[Attribute.ATMOSPHERE]!!))
        this.humidityLabel.setText(Attribute.WATER.stringValue(this.screen.selectedPlanet!!.attributes[Attribute.WATER]!!))
        this.solidityLabel.setText(Attribute.SOLIDITY.stringValue(this.screen.selectedPlanet!!.attributes[Attribute.SOLIDITY]!!))
    }

}