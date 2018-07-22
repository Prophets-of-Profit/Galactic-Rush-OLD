package com.prophetsofprofit.galacticrush.graphics.screen.maingame.overlay.planetoverlay.baseoverlay

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.MainGameScreen
import com.prophetsofprofit.galacticrush.logic.drone.Drone
import com.prophetsofprofit.galacticrush.logic.facility.ConstructionFacility
import com.prophetsofprofit.galacticrush.logic.facility.ProgrammingFacility
import ktx.scene2d.Scene2DSkin

/**
 * A container for a group of buttons that control what the player can do at the
 */
class BaseActionsPanel(val gameScreen: MainGameScreen, val bottomLeftX: Float, val bottomLeftY: Float, val labelWidth: Float, val labelHeight: Float): Group() {

    //The button which creates a drone on the facility, provided there is a ConstructionFacility on the selected planet
    val createDroneButton = TextButton("Create Drone", Scene2DSkin.defaultSkin)
    //The button which modifies a drone on the facility, provided there is a ProgrammingFacility on the selected planet
    val modifyDroneButton = TextButton("Modify Drone", Scene2DSkin.defaultSkin)

    /**
     * Centers the buttons in the panel
     * TODO: Center height as well, and add backing?
     */
    init {
        this.createDroneButton.setPosition(
                this.bottomLeftX + (this.labelWidth - this.createDroneButton.width) / 2 ,
                this.bottomLeftY + this.createDroneButton.height / 4)
        this.modifyDroneButton.setPosition(
                this.bottomLeftX + (this.labelWidth - this.modifyDroneButton.width) / 2 ,
                this.createDroneButton.y + this.createDroneButton.height + this.modifyDroneButton.height / 4)
        this.createDroneButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                val newDrone = Drone(gameScreen.player.id, gameScreen.selectedPlanet!!.id)
                gameScreen.selectedPlanet!!.drones.add(newDrone)
                gameScreen.player.currentChanges.changedDrones.add(newDrone)
            }
        })
        this.addActor(this.createDroneButton)
        this.addActor(this.modifyDroneButton)
    }

    /**
     * Makes buttons visible depending on what facilities the selected planet has
     */
    fun update() {
        this.createDroneButton.isVisible = this.gameScreen.selectedPlanet!!.facilities.firstOrNull { it is ConstructionFacility } != null
        this.modifyDroneButton.isVisible = this.gameScreen.selectedPlanet!!.facilities.firstOrNull { it is ProgrammingFacility } != null
    }

}
