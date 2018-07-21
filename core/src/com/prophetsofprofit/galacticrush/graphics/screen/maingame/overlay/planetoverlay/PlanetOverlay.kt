package com.prophetsofprofit.galacticrush.graphics.screen.maingame.overlay.planetoverlay

import com.badlogic.gdx.scenes.scene2d.Group
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.MainGameScreen

/**
 * Manages all actors that appear when a planet is selected
 */
class PlanetOverlay(val gameScreen: MainGameScreen): Group() {

    //Displays a scrollable list of all the drones on the planet
    val dronesPanel = PlanetDronesPanel(this.gameScreen, this.gameScreen.game.camera.viewportWidth / 8, this.gameScreen.game.camera.viewportHeight / 3)
    //Display the planet's attributes
    val attributesPanel = PlanetAttributesPanel(this.gameScreen, this.gameScreen.game.camera.viewportWidth / 8, this.gameScreen.game.camera.viewportHeight / 3)

    /**
     * Initializes the UI elements
     */
    init {
        this.addActor(this.dronesPanel)
        this.addActor(this.attributesPanel)
        this.isVisible = false
    }

    /**
     * Ensures the UI elements are only visible when a planet is selected
     */
    fun updateInformation() {
        if (this.gameScreen.selectedPlanet != null) {
            this.isVisible = true
            this.dronesPanel.updateInformation()
            this.attributesPanel.updateInformation()
        } else {
            this.isVisible = false
        }
    }

}
