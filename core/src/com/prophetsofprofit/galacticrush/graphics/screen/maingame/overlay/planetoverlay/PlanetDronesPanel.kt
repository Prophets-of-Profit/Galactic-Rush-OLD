package com.prophetsofprofit.galacticrush.graphics.screen.maingame.overlay.planetoverlay

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.MainGameScreen
import ktx.scene2d.Scene2DSkin

/**
 * The actor group that handles displaying drones that are on a selected planet
 */
class PlanetDronesPanel(val gameScreen: MainGameScreen, val labelWidth: Float, val labelHeight: Float, val yOffset: Float): Group() {

    val panelLabel = Label("Drones", Scene2DSkin.defaultSkin, "ui")
    //The list where each element is the string representation of the drone on the selected planet
    val dronesList = List<String>(Scene2DSkin.defaultSkin)
    //The scroll panel that has the list
    val droneListContainer = ScrollPane(this.dronesList)

    /**
     * Initializes the panel's drones list and panel positionings
     */
    init {
        this.panelLabel.width = this.labelWidth
        this.panelLabel.setPosition(this.gameScreen.game.camera.viewportWidth - this.labelWidth,
                this.gameScreen.game.camera.viewportHeight / 3 - this.yOffset)
        this.panelLabel.setAlignment(Align.top)
        this.droneListContainer.width = this.labelWidth
        this.droneListContainer.height = this.labelHeight - this.panelLabel.height
        this.droneListContainer.setPosition(this.gameScreen.game.camera.viewportWidth - this.labelWidth,
                this.gameScreen.game.camera.viewportHeight / 3 - this.yOffset)
        this.dronesList.width = this.labelWidth
        this.dronesList.setAlignment(Align.center)
        this.panelLabel.height = this.labelHeight
        this.addActor(this.panelLabel)
        this.addActor(this.droneListContainer)
    }

    /**
     * Updates all of the drones on the game screen's selected planet
     */
    fun update() {
        this.dronesList.setItems(Array(this.gameScreen.selectedPlanet!!.drones.map { it.toString() }.toTypedArray()))
    }

}