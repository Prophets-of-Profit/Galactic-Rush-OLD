package com.prophetsofprofit.galacticrush.graphics.screen.maingame.panel

import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import com.prophetsofprofit.galacticrush.graphics.Panel
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.MainGameScreen
import com.prophetsofprofit.galacticrush.logic.base.Facility
import ktx.scene2d.Scene2DSkin

/**
 * A class that displays general information of the game
 */
class GeneralInformationPanel(gameScreen: MainGameScreen) : Panel(gameScreen, "General Information", 0f, gameScreen.uiCamera.viewportHeight, gameScreen.uiCamera.viewportWidth, 0.1f * gameScreen.uiCamera.viewportHeight, Align.topLeft) {

    /**
     * Adds all the necessary UI components and actions
     */
    init {
        /*
         * Section for money display
         */
        val moneyLabel = Label("Money", Scene2DSkin.defaultSkin).also { it.setAlignment(Align.center) }
        val moneyDropdown = SelectBox<String>(Scene2DSkin.defaultSkin).also {
            it.setAlignment(Align.center)
            var shouldHandle = true
            //The money dropdown will always have the user's money as the selected money display, unless the user is viewing a different player's planet
            it.addAction(object : Action() {
                override fun act(delta: Float): Boolean {
                    shouldHandle = false
                    it.items = Array(gameScreen.mainGame.money.map { "${it.key}: ${it.value}" }.toTypedArray())
                    if (gameScreen.selectedPlanet?.base?.ownerId == it.selected.split(":").first().toInt()) {
                        shouldHandle = true
                        return false
                    }
                    it.selected = it.items.first {
                        it.split(":").first().toInt() == (gameScreen.selectedPlanet?.base?.ownerId
                                ?: gameScreen.player.id)
                    }
                    shouldHandle = true
                    return false
                }
            })
            //Upon selecting the money of a player, the user is moved to the home base of the player
            it.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    if (!shouldHandle) {
                        return
                    }
                    val selectedPlayerId = it.selected.split(":").first().toInt()
                    gameScreen.selectPlanet(gameScreen.mainGame.galaxy.planets.first { it.base?.facilityHealths?.containsKey(Facility.HOME_BASE) == true && it.base?.ownerId == selectedPlayerId })
                }
            })
        }

        /*
         * Section for base display
         */
        val baseLabel = Label("Bases", Scene2DSkin.defaultSkin).also { it.setAlignment(Align.center) }
        val baseDropdown = SelectBox<String>(Scene2DSkin.defaultSkin).also {
            it.setAlignment(Align.center)
            var shouldHandle = true
            //The base dropdown will always show the user's current home base, unless the user is viewing a planet with a base on it
            it.addAction(object : Action() {
                override fun act(delta: Float): Boolean {
                    shouldHandle = false
                    it.items = Array(arrayOf("Select a Base") + gameScreen.mainGame.bases.map { "$it" }.toTypedArray())
                    if (gameScreen.selectedPlanet?.base != null) {
                        it.selected = "${gameScreen.selectedPlanet?.base!!}"
                        shouldHandle = true
                        return false
                    }
                    it.selectedIndex = 0
                    shouldHandle = true
                    return false
                }
            })
            //Upon selecting a certain base, the user is moved to viewing the planet that contains the selected base
            it.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    val selectedIndex = it.selectedIndex - 1
                    if (!shouldHandle || it.selectedIndex == -1) {
                        return
                    }
                    gameScreen.selectPlanet(gameScreen.mainGame.galaxy.planets.first { gameScreen.mainGame.bases.indexOf(it.base) == selectedIndex })
                }
            })
        }

        /*
         * Section for drone display
         */
        val droneLabel = Label("Drones", Scene2DSkin.defaultSkin).also { it.setAlignment(Align.center) }
        val droneDropdown = SelectBox<String>(Scene2DSkin.defaultSkin).also {
            it.setAlignment(Align.center)
            var shouldHandle = true
            //The drone dropdown will always show one of the drones on the user's selected planet
            it.addAction(object : Action() {
                override fun act(delta: Float): Boolean {
                    shouldHandle = false
                    it.items = Array(arrayOf("Select a Drone") + gameScreen.mainGame.drones.map { "$it" })
                    it.selectedIndex = if (gameScreen.selectedDrone == null) 0 else gameScreen.mainGame.drones.indexOf(gameScreen.selectedDrone) + 1
                    shouldHandle = true
                    return false
                }
            })
            //Upon selecting a certain drone, the user is moved to viewing the planet that contains the selected drone
            it.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    if (!shouldHandle) {
                        return
                    }
                    if (it.selectedIndex == 0) {
                        gameScreen.selectedDroneId = null
                        return
                    }
                    val selectedDrone = gameScreen.mainGame.drones[it.selectedIndex - 1]
                    gameScreen.selectPlanet(gameScreen.mainGame.galaxy.getPlanetWithId(selectedDrone.locationId)!!)
                    gameScreen.selectedDroneId = selectedDrone.id
                }
            })
        }

        /*
         * Section for turn display
         * TODO: have selecting a turn open up an event log modal for that turn; having an open event log should make this action set the selected dropdown item to the turn of the event log of the opened modal
         */
        val turnLabel = Label("Turns", Scene2DSkin.defaultSkin).also { it.setAlignment(Align.center) }
        val turnDropdown = SelectBox<String>(Scene2DSkin.defaultSkin).also {
            it.setAlignment(Align.center)
            //The turn dropdown will always show the current turn
            it.addAction(object : Action() {
                override fun act(delta: Float): Boolean {
                    it.items = Array((0..gameScreen.mainGame.turnsPlayed).map { "$it" }.toTypedArray())
                    it.selected = "${gameScreen.mainGame.turnsPlayed}"
                    return false
                }
            })
            //Upon selecting a certain turn, an event log is opened: TODO
        }

        /*
         * Section for layout
         */
        this.add(moneyLabel)
        this.add(moneyDropdown)
        this.add(baseLabel)
        this.add(baseDropdown)
        this.add(droneLabel)
        this.add(droneDropdown)
        this.add(turnLabel)
        this.add(turnDropdown)
        this.cells.forEach { it.expandX().fillX().pad(15f) }
    }

}