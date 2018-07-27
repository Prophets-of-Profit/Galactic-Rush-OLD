package com.prophetsofprofit.galacticrush.graphics.screen.maingame.overlay.planetoverlay.baseoverlay

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.MainGameScreen
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.Panel
import ktx.scene2d.Scene2DSkin

/**
 * The actor group that handles displaying facilities that are on a selected planet
 */
class BaseFacilitiesPanel(gameScreen: MainGameScreen, labelWidth: Float, labelHeight: Float, yOffset: Float): Panel(gameScreen, "Facilities", labelWidth, labelHeight, 0f, gameScreen.uiCamera.viewportHeight - yOffset, Align.topLeft) {

    //The list where each element is the string representation of the drone on the selected planet
    val facilitiesList = com.badlogic.gdx.scenes.scene2d.ui.List<String>(Scene2DSkin.defaultSkin)
    //The scroll panel that has the list
    val facilityListContainer = ScrollPane(this.facilitiesList)

    /**
     * Initializes the panel's facilities list and panel positionings
     */
    init {
        this.facilityListContainer.width = labelWidth
        this.facilityListContainer.height = labelHeight - this.verticalTextOffset
        this.facilityListContainer.setPosition(0f,
                this.screen.uiCamera.viewportHeight / 3 - yOffset)
        this.facilitiesList.width = labelWidth
        this.facilitiesList.setAlignment(Align.center)
        this.addActor(this.facilityListContainer)
    }

    /**
     * Updates all of the drones on the game screen's selected planet
     */
    fun update() {
        this.facilitiesList.setItems(Array(this.screen.selectedPlanet!!.facilities.map { it.toString() }.toTypedArray()))
    }

}
