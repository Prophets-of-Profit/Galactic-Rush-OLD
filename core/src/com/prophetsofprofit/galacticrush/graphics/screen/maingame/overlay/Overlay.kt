package com.prophetsofprofit.galacticrush.graphics.screen.maingame.overlay

import com.badlogic.gdx.scenes.scene2d.Group
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.MainGameScreen
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.overlay.planetoverlay.PlanetOverlay

/**
 * The actor group which makes up the overlay of the main game
 * Must always be tied to a main game, should not be placed in another screen
 */
class Overlay(val gameScreen: MainGameScreen): Group() {

    //The actor group that handles displaying the overlay's possible actions
    val overlayMenu = OverlayMenu(this.gameScreen)
    //The actor group that handles displaying the overlay information
    val overlayInformation = OverlayInformation(this.gameScreen, this.gameScreen.game.camera.viewportWidth, this.gameScreen.game.camera.viewportHeight / 16)
    //The overlay that comes up when planets are selected
    val planetOverlay = PlanetOverlay(gameScreen, this.gameScreen.game.camera.viewportHeight / 16)

    /**
     * Adds all of the sub overlays as children
     */
    init {
        this.addActor(this.overlayInformation)
        this.addActor(this.overlayMenu)
        this.addActor(planetOverlay)
    }

    /**
     * Updates all contained overlays
     */
    fun update() {
        this.overlayInformation.update()
        this.planetOverlay.update()
    }

}