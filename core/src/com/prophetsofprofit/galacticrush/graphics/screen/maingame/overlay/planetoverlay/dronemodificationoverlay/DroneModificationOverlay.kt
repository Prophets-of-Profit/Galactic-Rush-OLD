package com.prophetsofprofit.galacticrush.graphics.screen.maingame.overlay.planetoverlay.dronemodificationoverlay

import com.badlogic.gdx.scenes.scene2d.Group
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.MainGameScreen

/**
 * Handles all actions of drone modification using draggable modal dialogs
 * Actions include renaming, ordering instructions, etc.
 */
class DroneModificationOverlay(val gameScreen: MainGameScreen): Group() {

    //The panel used to rename the drone
    val renamePanel = RenamePanel()

    init {
        this.addActor(renamePanel)
        this.isVisible = false
    }

    /**
     * Updates the information of all drone modification components
     */
    fun update() {
        if (this.gameScreen.overlay.planetOverlay.dronesPanel.dronesList.selectedIndex != -1) {
            this.renamePanel.drone = this.gameScreen.selectedPlanet!!.drones[this.gameScreen.overlay.planetOverlay.dronesPanel.dronesList.selectedIndex]
            this.renamePanel.update()
        }
    }

    /**
     * Applies all changes to drone
     */
    fun confirm() {
        this.renamePanel.confirm()
    }

}
