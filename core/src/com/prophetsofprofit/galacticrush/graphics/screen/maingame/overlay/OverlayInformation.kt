package com.prophetsofprofit.galacticrush.graphics.screen.maingame.overlay

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.MainGameScreen
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.Scene2dDsl

/**
 * Handles showing the information that's available to the player at all times
 * e.g. money, turns played
 */
class OverlayInformation(val gameScreen: MainGameScreen, val labelWidth: Float, val labelHeight: Float): Group() {

    //Displays the number of turns that have passed
    val turnsLabel = Label("Turns Played", Scene2DSkin.defaultSkin, "ui")
    //Displays the player's money
    val moneyLabel = Label("Credits", Scene2DSkin.defaultSkin, "ui")
    //Displays the winner, if there is one
    val winnerLabel = Label("Winner: ", Scene2DSkin.defaultSkin, "ui")
    //The box where the user can select a drone to see
//    val droneSelectBox = SelectBox<String>(Scene2DSkin.defaultSkin)
    //The box where the user can select a base to see
//    val baseSelectBox = SelectBox<String>(Scene2DSkin.defaultSkin)

    /**
     * Organizes the labels into respective positions
     */
    init {
        this.turnsLabel.width = this.labelWidth / 4
        this.turnsLabel.height = this.labelHeight
        this.turnsLabel.setPosition(this.gameScreen.game.camera.viewportWidth - this.turnsLabel.width,
                                    this.gameScreen.game.camera.viewportHeight - this.turnsLabel.height)
        this.turnsLabel.setAlignment(Align.center)
        this.moneyLabel.width = this.labelWidth / 4
        this.moneyLabel.height = this.labelHeight
        this.moneyLabel.setPosition(this.gameScreen.game.camera.viewportWidth - this.turnsLabel.width - this.moneyLabel.width,
                this.gameScreen.game.camera.viewportHeight - this.moneyLabel.height)
        this.moneyLabel.setAlignment(Align.center)
        this.winnerLabel.width = this.gameScreen.game.camera.viewportWidth
        this.winnerLabel.height = this.gameScreen.game.camera.viewportHeight
        this.winnerLabel.setPosition(0f, 0f)
        this.winnerLabel.setAlignment(Align.center)
//        this.droneSelectBox.width = this.labelWidth / 4
//        this.droneSelectBox.height = this.labelHeight
//        this.droneSelectBox.addListener(object: ChangeListener() {
//            override fun changed(event: ChangeEvent?, actor: Actor?) {
//                if (droneSelectBox.selectedIndex > 1) {
//                    //Selects the planet that corresponds to the selected drone
//                    gameScreen.selectPlanet(gameScreen.mainGame.drones[droneSelectBox.selectedIndex - 1].getLocationAmong(gameScreen.mainGame.galaxy.planets.toTypedArray())!!)
//                }
//            }
//        })
//        this.baseSelectBox.width = this.labelWidth / 4
//        this.baseSelectBox.height = this.labelHeight
//        this.baseSelectBox.addListener(object: ChangeListener() {
//            override fun changed(event: ChangeEvent?, actor: Actor?) {
//                if (baseSelectBox.selectedIndex > 1) {
//                    //Selects the planet that corresponds to the selected base
//                    gameScreen.selectPlanet(gameScreen.mainGame.facilities[baseSelectBox.selectedIndex - 1].getLocationAmong(gameScreen.mainGame.galaxy.planets.toTypedArray())!!)
//                }
//            }
//        })
        this.addActor(this.turnsLabel)
        this.addActor(this.moneyLabel)
        this.addActor(this.winnerLabel)
//        this.addActor(this.droneSelectBox)
//        this.addActor(this.baseSelectBox)
        this.winnerLabel.isVisible = false
    }

    /**
     * Updates the values of the information displayed in the overlay
     */
    fun update() {
        this.turnsLabel.setText("Turns Played: ${this.gameScreen.mainGame.turnsPlayed}")
        this.moneyLabel.setText("Credit: ${this.gameScreen.mainGame.money[this.gameScreen.player.id]}")
        //Updates drones and facilities dropdown
//        this.droneSelectBox.items = Array((mutableListOf("Select Drone:").also{ it.addAll(this.gameScreen.mainGame.drones.map { "$it" }) }).toTypedArray())
//        this.baseSelectBox.items = Array((mutableListOf("Select Facility:").also{ it.addAll(this.gameScreen.mainGame.facilities.map { "$it" }) }).toTypedArray())
        //Declares a winner if the number of players is 1
        if (this.gameScreen.mainGame.players.size == 1) {
            this.winnerLabel.setText("The winner is player ${this.gameScreen.mainGame.players.first()}!!!")
            this.winnerLabel.isVisible = true
        }
        //Declares a tie if the number of players is 0
        if (this.gameScreen.mainGame.players.isEmpty()) {
            this.winnerLabel.setText("It's a tie!!!")
            this.winnerLabel.isVisible = true
        }
    }

}