package com.prophetsofprofit.galacticrush.graphics.screen.maingame.overlay.planetoverlay.baseoverlay

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.MainGameScreen
import com.prophetsofprofit.galacticrush.logic.base.Facility
import com.prophetsofprofit.galacticrush.logic.drone.Drone
import ktx.scene2d.Scene2DSkin

/**
 * A container for a group of buttons that control what the player can do at the
 */
class BaseActionButtons(val gameScreen: MainGameScreen, val bottomLeftX: Float, val bottomLeftY: Float, val labelWidth: Float, val labelHeight: Float): Group() {

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
        this.modifyDroneButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (gameScreen.overlay.planetOverlay.dronesPanel.dronesList.selectedIndex == -1)
                    return
                gameScreen.overlay.planetOverlay.baseOverlay.droneModificationOverlay.isVisible = !gameScreen.overlay.planetOverlay.baseOverlay.droneModificationOverlay.isVisible
                if (gameScreen.overlay.planetOverlay.baseOverlay.droneModificationOverlay.isVisible) {
                    gameScreen.overlay.planetOverlay.baseOverlay.droneModificationOverlay.update()
                    modifyDroneButton.setText("Confirm")
                } else {
                    gameScreen.overlay.planetOverlay.baseOverlay.droneModificationOverlay.confirm()
                    modifyDroneButton.setText("Modify Drone")
                }
            }
        })
        this.addActor(this.createDroneButton)
        this.addActor(this.modifyDroneButton)
    }

    /**
     * Makes buttons visible depending on what facilities the selected planet has
     */
    fun update() {
        this.createDroneButton.isVisible = Facility.CONSTUCTION in this.gameScreen.selectedPlanet!!.base!!.facilityHealths
        this.modifyDroneButton.isVisible = Facility.PROGRAMMING in this.gameScreen.selectedPlanet!!.base!!.facilityHealths
    }

}
