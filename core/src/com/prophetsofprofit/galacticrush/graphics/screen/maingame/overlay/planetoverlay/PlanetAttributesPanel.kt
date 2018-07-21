package com.prophetsofprofit.galacticrush.graphics.screen.maingame.overlay.planetoverlay

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.MainGameScreen
import com.prophetsofprofit.galacticrush.logic.map.Attribute
import ktx.scene2d.Scene2DSkin

/**
 * The actor group which displays planet attributes (such as temperature, mass, etc.) in a label
 */
class PlanetAttributesPanel(val gameScreen: MainGameScreen, val labelWidth: Float, val labelHeight: Float): com.badlogic.gdx.scenes.scene2d.Group() {

    //The label for the currently selected planet
    val planetLabel = com.badlogic.gdx.scenes.scene2d.ui.Label("Planet Stats", Scene2DSkin.defaultSkin, "ui")
    //The icons and labels for planet information
    val massIcon = com.badlogic.gdx.scenes.scene2d.ui.ImageButton(Scene2DSkin.defaultSkin, "mass")
    val temperatureIcon = com.badlogic.gdx.scenes.scene2d.ui.ImageButton(Scene2DSkin.defaultSkin, "temp")
    val atmosphereIcon = com.badlogic.gdx.scenes.scene2d.ui.ImageButton(Scene2DSkin.defaultSkin, "atm")
    val humidityIcon = com.badlogic.gdx.scenes.scene2d.ui.ImageButton(Scene2DSkin.defaultSkin, "humid")
    val solidityIcon = com.badlogic.gdx.scenes.scene2d.ui.ImageButton(Scene2DSkin.defaultSkin, "solid")
    val massLabel = com.badlogic.gdx.scenes.scene2d.ui.Label("", Scene2DSkin.defaultSkin, "small")
    val temperatureLabel = com.badlogic.gdx.scenes.scene2d.ui.Label("", Scene2DSkin.defaultSkin, "small")
    val atmosphereLabel = com.badlogic.gdx.scenes.scene2d.ui.Label("", Scene2DSkin.defaultSkin, "small")
    val humidityLabel = com.badlogic.gdx.scenes.scene2d.ui.Label("", Scene2DSkin.defaultSkin, "small")
    val solidityLabel = com.badlogic.gdx.scenes.scene2d.ui.Label("", Scene2DSkin.defaultSkin, "small")

    init {
        //Tweak the label position
        this.planetLabel.width = this.labelWidth
        this.planetLabel.height = this.labelHeight
        this.planetLabel.setPosition(this.gameScreen.game.camera.viewportWidth - this.labelWidth ,
                this.gameScreen.game.camera.viewportHeight - this.planetLabel.height)
        this.planetLabel.setAlignment(com.badlogic.gdx.utils.Align.top)

        //Set the position of all the labels
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

        //Add the icons as actors
        this.addActor(this.planetLabel)
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
        this.massLabel.setText(com.prophetsofprofit.galacticrush.logic.map.Attribute.MASS.stringValue(this.gameScreen.selectedPlanet!!.attributes[Attribute.MASS]!!))
        this.temperatureLabel.setText(com.prophetsofprofit.galacticrush.logic.map.Attribute.TEMPERATURE.stringValue(this.gameScreen.selectedPlanet!!.attributes[Attribute.TEMPERATURE]!!))
        this.atmosphereLabel.setText(com.prophetsofprofit.galacticrush.logic.map.Attribute.ATMOSPHERE.stringValue(this.gameScreen.selectedPlanet!!.attributes[Attribute.ATMOSPHERE]!!))
        this.humidityLabel.setText(com.prophetsofprofit.galacticrush.logic.map.Attribute.WATER.stringValue(this.gameScreen.selectedPlanet!!.attributes[Attribute.WATER]!!))
        this.solidityLabel.setText(com.prophetsofprofit.galacticrush.logic.map.Attribute.SOLIDITY.stringValue(this.gameScreen.selectedPlanet!!.attributes[Attribute.SOLIDITY]!!))
    }

}