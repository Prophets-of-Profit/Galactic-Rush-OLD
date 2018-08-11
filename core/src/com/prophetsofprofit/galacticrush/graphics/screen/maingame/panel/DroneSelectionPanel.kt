package com.prophetsofprofit.galacticrush.graphics.screen.maingame.panel

import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import com.prophetsofprofit.galacticrush.graphics.Direction
import com.prophetsofprofit.galacticrush.graphics.Panel
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.MainGameScreen
import ktx.scene2d.Scene2DSkin

/**
 * The panel that will allow the user to select a drone on the current planet
 */
class DroneSelectionPanel(gameScreen: MainGameScreen) : Panel(gameScreen, "Drone Selection Panel", gameScreen.uiCamera.viewportWidth, 0.9f * gameScreen.uiCamera.viewportHeight, 0.25f * gameScreen.uiCamera.viewportWidth, 0.5f * gameScreen.uiCamera.viewportHeight, Align.topRight) {

    /**
     * Initializes panel components and alignments
     */
    init {
        //Adds the scroll pane with the list that will allow the user to select a drone
        this.add(ScrollPane(List<String>(Scene2DSkin.defaultSkin).also {
            it.setAlignment(Align.center)
            //Sets selection to the selected drone or no selection; updates all the drones on the selected planet
            it.addAction(object : Action() {
                override fun act(delta: Float): Boolean {
                    if (!canBeUsed) {
                        return false
                    }
                    it.setItems(Array(gameScreen.selectedPlanet?.drones?.map { "$it" }?.toTypedArray()
                            ?: arrayOf("")))
                    if (gameScreen.selectedDroneId != null) {
                        it.selectedIndex = gameScreen.mainGame.drones.indexOf(gameScreen.selectedDrone)
                    } else {
                        it.selection.clear()
                    }
                    return false
                }
            })
            //Allows user to select a drone from this pane
            it.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    if (it.selectedIndex < 0 || it.items.size == 0 || gameScreen.mainGame.drones.isEmpty()) {
                        gameScreen.selectedDroneId = null
                        it.selection.clear()
                        return
                    }
                    gameScreen.selectedDroneId = gameScreen.mainGame.drones[it.selectedIndex].id
                }
            })
        }, Scene2DSkin.defaultSkin)).expand().fill()
        //Adds the action that will control the visibility of this panel; panel is visible if selected planet has drones
        this.addAction(object : Action() {
            override fun act(delta: Float): Boolean {
                if (!canBeUsed) {
                    return false
                }
                if (gameScreen.selectedPlanet?.drones != null) {
                    //children.forEach { it.act(delta) }
                    appear(Direction.RIGHT, 1f)
                } else {
                    disappear(Direction.RIGHT, 1f)
                }
                return false
            }
        })
    }

}
