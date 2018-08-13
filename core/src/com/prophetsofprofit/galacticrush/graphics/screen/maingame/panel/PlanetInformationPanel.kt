package com.prophetsofprofit.galacticrush.graphics.screen.maingame.panel

import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.utils.Align
import com.prophetsofprofit.galacticrush.graphics.Direction
import com.prophetsofprofit.galacticrush.graphics.Panel
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.MainGameScreen

/**
 * The panel that will allow the user to view information about the current planet
 */
class PlanetInformationPanel(gameScreen: MainGameScreen) : Panel(gameScreen, "Planet Information", gameScreen.uiCamera.viewportWidth, 0.9f * gameScreen.uiCamera.viewportHeight, 0.25f * gameScreen.uiCamera.viewportWidth, 0.45f * gameScreen.uiCamera.viewportHeight, Align.topRight) {

    /**
     * Initializes the UI components and their positionings
     */
    init {
        this.addAction(object : Action() {
            override fun act(delta: Float): Boolean {
                if (gameScreen.selectedPlanet != null) {
                    appear(Direction.RIGHT, 1f)
                } else {
                    disappear(Direction.RIGHT, 1f)
                }
                return false
            }
        })
    }

}