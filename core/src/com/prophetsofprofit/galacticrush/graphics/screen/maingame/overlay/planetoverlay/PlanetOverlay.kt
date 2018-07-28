package com.prophetsofprofit.galacticrush.graphics.screen.maingame.overlay.planetoverlay

import com.badlogic.gdx.scenes.scene2d.Group
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.MainGameScreen
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.MovementHandler
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.overlay.planetoverlay.baseoverlay.BaseOverlay
import kotlin.math.abs

/**
 * Manages all actors that appear when a planet is selected
 */
class PlanetOverlay(val gameScreen: MainGameScreen, val yOffset: Float): Group() {

    //Displays a scrollable list of all the drones on the planet
    val dronesPanel = PlanetDronesPanel(this.gameScreen, this.gameScreen.game.camera.viewportWidth / 6, this.gameScreen.game.camera.viewportHeight / 3, this.yOffset)
    //Display the planet's attributes
    val attributesPanel = PlanetAttributesPanel(this.gameScreen, this.gameScreen.game.camera.viewportWidth / 6, this.gameScreen.game.camera.viewportHeight / 3, this.yOffset)
    //Display the base's information
    val baseOverlay = BaseOverlay(this.gameScreen, this.yOffset)
    //Whether the panels are inside the camera view
    var isInView = false
    //How fast the panels slide into view
    val zoomSpeed = 0.1f

    /**
     * Initializes the UI elements
     */
    init {
        this.addActor(this.dronesPanel)
        this.addActor(this.attributesPanel)
        this.addActor(this.baseOverlay)
    }

    /**
     * Ensures the UI elements are only visible when a planet is selected
     */
    fun update() {
        if (this.gameScreen.selectedPlanet != null) {
            //If the overlay is becoming visible
            if (!this.isInView) {
                //Ensure that there is not already a move command for the panels in place
                MovementHandler.currentlyMoving.remove(this.attributesPanel)
                MovementHandler.currentlyMoving.remove(this.dronesPanel)
                //Start moving the attributes panel
                MovementHandler.add(this.attributesPanel,
                        - this.attributesPanel.x - this.attributesPanel.baseLabel.width,
                        0f,
                        //Time is proportional to distance to move
                        //If there is no shift, time should equal zoomSpeed
                        this.zoomSpeed * abs(- this.attributesPanel.x - this.attributesPanel.baseLabel.width) / this.attributesPanel.baseLabel.width)
                //Start moving the drone panel
                MovementHandler.add(this.dronesPanel,
                        - this.dronesPanel.x - this.attributesPanel.baseLabel.width,
                        0f,
                        this.zoomSpeed * abs(- this.dronesPanel.x - this.dronesPanel.baseLabel.width) / this.dronesPanel.baseLabel.width)
            }
            //Update the information for all the overlays
            this.dronesPanel.update()
            this.attributesPanel.update()
            this.isInView = true
        } else {
            if (this.isInView) {
                //Ensure that there is not already a move command for the panels in place
                MovementHandler.currentlyMoving.remove(this.attributesPanel)
                MovementHandler.currentlyMoving.remove(this.dronesPanel)
                //Start moving the attributes panel
                MovementHandler.add(this.attributesPanel,
                        - this.attributesPanel.x,
                        0f,
                        this.zoomSpeed * abs( - this.attributesPanel.x) / this.attributesPanel.baseLabel.width)
                //Start moving the drone panel
                MovementHandler.add(this.dronesPanel,
                        - this.dronesPanel.x,
                        0f,
                        this.zoomSpeed * abs( - this.dronesPanel.x) / this.dronesPanel.baseLabel.width)
            }
            //If the overlay is becoming invisible
            this.isInView = false
        }
        //Update this separately because it also controls its own visibility
        this.baseOverlay.update()
    }

}
