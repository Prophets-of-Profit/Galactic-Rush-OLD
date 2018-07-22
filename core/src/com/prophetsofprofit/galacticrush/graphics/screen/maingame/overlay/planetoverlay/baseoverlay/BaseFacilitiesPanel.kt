package com.prophetsofprofit.galacticrush.graphics.screen.maingame.overlay.planetoverlay.baseoverlay

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.MainGameScreen
import ktx.scene2d.Scene2DSkin

/**
 * The actor group that handles displaying drones that are on a selected planet
 */
class BaseFacilitiesPanel(val gameScreen: MainGameScreen, val labelWidth: Float, val labelHeight: Float): Group() {

    val panelLabel = Label("Facilities", Scene2DSkin.defaultSkin, "ui")
    //The list where each element is the string representation of the drone on the selected planet
    val facilitiesList = com.badlogic.gdx.scenes.scene2d.ui.List<String>(Scene2DSkin.defaultSkin)
    //The scroll panel that has the list
    val facilityListContainer = ScrollPane(this.facilitiesList)

    /**
     * Initializes the panel's facilities list and panel positionings
     */
    init {
        this.panelLabel.width = this.labelWidth
        this.panelLabel.setPosition(0f,
                this.gameScreen.game.camera.viewportHeight / 3)
        this.panelLabel.setAlignment(Align.top)
        this.facilityListContainer.width = this.labelWidth
        this.facilityListContainer.height = this.labelHeight - this.panelLabel.height
        this.facilityListContainer.setPosition(0f,
                this.gameScreen.game.camera.viewportHeight / 3)
        this.facilitiesList.width = this.labelWidth
        this.facilitiesList.setAlignment(Align.center)
        this.panelLabel.height = this.labelHeight
        this.addActor(this.panelLabel)
        this.addActor(this.facilityListContainer)
    }

    /**
     * Updates all of the drones on the game screen's selected planet
     */
    fun update() {
        this.facilitiesList.setItems(Array(this.gameScreen.selectedPlanet!!.facilities.map { it.toString() }.toTypedArray()))
    }

}
