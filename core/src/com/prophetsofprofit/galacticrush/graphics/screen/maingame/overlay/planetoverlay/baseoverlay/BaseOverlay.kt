package com.prophetsofprofit.galacticrush.graphics.screen.maingame.overlay.planetoverlay.baseoverlay

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.MainGameScreen
import ktx.scene2d.Scene2DSkin

/**
 * Handles all actors that appear when a planet with a base on it is selected
 */
class BaseOverlay(val gameScreen: MainGameScreen): Group() {

    //Shows all facilities on the planet
    val facilitiesPanel = BaseFacilitiesPanel(this.gameScreen, this.gameScreen.game.camera.viewportWidth / 6, 2 * this.gameScreen.game.camera.viewportHeight / 3)
    //Handles all the buttons for facility actions
    val actionsPanel = BaseActionsPanel(this.gameScreen,
            this.facilitiesPanel.panelLabel.x,
            this.facilitiesPanel.panelLabel.y,
            this.facilitiesPanel.labelWidth,
            this.facilitiesPanel.labelHeight / 3
            )

    /**
     * Sets up all the buttons and panels
     */
    init {
        this.addActor(this.facilitiesPanel)
        this.addActor(this.actionsPanel)
        this.isVisible = false
    }

    /**
     * Become visible if the planet selected has a base
     */
    fun update() {
        this.facilitiesPanel.update()
        this.actionsPanel.update()
        this.isVisible = this.gameScreen.selectedPlanet!!.facilities.any { it.ownerId == this.gameScreen.player.id }
    }

}
