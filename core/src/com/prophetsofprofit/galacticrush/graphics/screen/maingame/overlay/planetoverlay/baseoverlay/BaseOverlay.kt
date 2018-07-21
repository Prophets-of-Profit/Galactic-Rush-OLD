package com.prophetsofprofit.galacticrush.graphics.screen.maingame.overlay.planetoverlay.baseoverlay

import com.badlogic.gdx.scenes.scene2d.Group
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.MainGameScreen

/**
 * Handles all actors that appear when a planet with a base on it is selected
 */
class BaseOverlay(val gameScreen: MainGameScreen): Group() {

    //Shows all facilities on the planet
    val facilitiesPanel = BaseFacilitiesPanel(this.gameScreen, this.gameScreen.game.camera.viewportWidth / 6, this.gameScreen.game.camera.viewportHeight / 3)

    init {
        this.addActor(facilitiesPanel)
        this.isVisible = false
    }

    /**
     * Become visible if the planet selected has a base
     */
    fun update() {
        this.facilitiesPanel.update()
        this.isVisible = this.gameScreen.selectedPlanet!!.facilities.isNotEmpty()
    }

}
