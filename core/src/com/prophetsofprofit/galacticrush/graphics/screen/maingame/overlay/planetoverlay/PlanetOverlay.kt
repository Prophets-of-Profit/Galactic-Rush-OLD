package com.prophetsofprofit.galacticrush.graphics.screen.maingame.overlay.planetoverlay

import com.badlogic.gdx.scenes.scene2d.Group
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.MainGameScreen
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.overlay.planetoverlay.baseoverlay.BaseOverlay

/**
 * Manages all actors that appear when a planet is selected
 */
class PlanetOverlay(val gameScreen: MainGameScreen): Group() {

    //Displays a scrollable list of all the drones on the planet
    val dronesPanel = PlanetDronesPanel(this.gameScreen, this.gameScreen.game.camera.viewportWidth / 6, this.gameScreen.game.camera.viewportHeight / 3)
    //Display the planet's attributes
    val attributesPanel = PlanetAttributesPanel(this.gameScreen, this.gameScreen.game.camera.viewportWidth / 6, this.gameScreen.game.camera.viewportHeight / 3)
    //Display the base's information
    val baseOverlay = BaseOverlay(this.gameScreen)

    /**
     * Initializes the UI elements
     */
    init {
        this.addActor(this.dronesPanel)
        this.addActor(this.attributesPanel)
        this.addActor(this.baseOverlay)
        this.isVisible = false
    }

    /**
     * Ensures the UI elements are only visible when a planet is selected
     */
    fun update() {
        if (this.gameScreen.selectedPlanet != null) {
            this.isVisible = true
            this.dronesPanel.update()
            this.attributesPanel.update()
            this.baseOverlay.update()
        } else {
            this.isVisible = false
        }
    }

}
