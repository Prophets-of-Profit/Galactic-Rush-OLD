package com.prophetsofprofit.galacticrush.graphics.screen.maingame.overlay.planetoverlay

import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.MainGameScreen
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.Panel
import ktx.scene2d.Scene2DSkin

/**
 * The actor group that handles displaying drones that are on a selected planet
 */
class PlanetDronesPanel(gameScreen: MainGameScreen, labelWidth: Float, labelHeight: Float, yOffset: Float): Panel(gameScreen, "Drones", labelWidth, labelHeight, gameScreen.uiCamera.viewportWidth, gameScreen.uiCamera.viewportHeight - yOffset - labelHeight, Align.topLeft) {

    //The list where each element is the string representation of the drone on the selected planet
    val dronesList = List<String>(Scene2DSkin.defaultSkin)
    //The scroll panel that has the list
    val droneListContainer = ScrollPane(this.dronesList)

    /**
     * Initializes the panel's drones list and panel positionings
     */
    init {
        this.droneListContainer.width = labelWidth
        this.droneListContainer.height = labelHeight - this.verticalTextOffset
        this.droneListContainer.setPosition(this.screen.uiCamera.viewportWidth,
                this.screen.uiCamera.viewportHeight / 3 - yOffset)
        this.dronesList.width = labelWidth
        this.dronesList.setAlignment(Align.center)
        this.addActor(this.droneListContainer)
    }

    /**
     * Updates all of the drones on the game screen's selected planet
     */
    fun update() {
        this.dronesList.setItems(Array(this.screen.selectedPlanet!!.drones.map { "$it" }.toTypedArray()))
    }

}