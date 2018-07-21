package com.prophetsofprofit.galacticrush.graphics.screen.maingame.overlay

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
class PlanetDronesPanel(val gameScreen: MainGameScreen): Group() {

    val panelLabel = Label("Drones", Scene2DSkin.defaultSkin, "ui")
    //The list where each element is the string representation of the drone on the selected planet
    val dronesList = List<String>(Scene2DSkin.defaultSkin)
    //The scroll panel that has the list
    val droneListContainer = ScrollPane(this.dronesList)

    /**
     * Initializes the panel's drones list and panel positionings
     */
    init {
        this.panelLabel.width = this.gameScreen.game.camera.viewportWidth / 8
        this.panelLabel.setPosition(this.gameScreen.game.camera.viewportWidth - this.panelLabel.width,
                this.gameScreen.game.camera.viewportHeight / 3)
        this.panelLabel.setAlignment(Align.top)
        this.droneListContainer.width = this.gameScreen.game.camera.viewportWidth / 8
        this.droneListContainer.height = this.gameScreen.game.camera.viewportHeight / 3 - this.panelLabel.height
        this.droneListContainer.setPosition(this.gameScreen.game.camera.viewportWidth - this.droneListContainer.width,
                this.gameScreen.game.camera.viewportHeight / 3)
        this.dronesList.width = this.gameScreen.game.camera.viewportWidth / 8
        this.dronesList.setAlignment(Align.center)
        this.panelLabel.height = this.gameScreen.game.camera.viewportHeight / 3
        this.addActor(this.panelLabel)
        this.addActor(this.droneListContainer)
        this.isVisible = false
    }

    /**
     * Updates all of the drones on the game screen's selected planet
     */
    fun updateInformation() {
        if (this.gameScreen.selectedPlanet != null) {
            this.isVisible = true
            this.dronesList.setItems(Array(this.gameScreen.selectedPlanet!!.drones.map { it.toString() }.toTypedArray()))
        } else {
            this.isVisible = false
        }
    }

}