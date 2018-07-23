package com.prophetsofprofit.galacticrush.graphics.screen.maingame.overlay

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
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

    /**
     * Organizes the labels into respective positions
     */
    init {
        this.turnsLabel.width = this.labelWidth / 2
        this.turnsLabel.height = this.labelHeight
        this.turnsLabel.setPosition(this.gameScreen.game.camera.viewportWidth - this.turnsLabel.width,
                                    this.gameScreen.game.camera.viewportHeight - this.turnsLabel.height)
        this.moneyLabel.width = this.labelWidth / 2
        this.moneyLabel.height = this.labelHeight
        this.moneyLabel.setPosition(this.gameScreen.game.camera.viewportWidth - this.turnsLabel.width - this.moneyLabel.width,
                this.gameScreen.game.camera.viewportHeight - this.moneyLabel.height)
        this.winnerLabel.width = this.gameScreen.game.camera.viewportWidth
        this.winnerLabel.height = this.gameScreen.game.camera.viewportHeight
        this.winnerLabel.setPosition(0f, 0f)
        this.winnerLabel.setAlignment(Align.center)
        this.addActor(this.turnsLabel)
        this.addActor(this.moneyLabel)
        this.addActor(this.winnerLabel)
        this.winnerLabel.isVisible = false
    }

    /**
     * Updates the values of the information displayed in the overlay
     */
    fun update() {
        this.turnsLabel.setText("Turns Played: ${this.gameScreen.mainGame.turnsPlayed}")
        this.moneyLabel.setText("Credit: ${this.gameScreen.mainGame.money[this.gameScreen.player.id]}")
        //Declares a winner if the number of players is 1
        if (this.gameScreen.mainGame.players.size == 1) {
            this.winnerLabel.setText("The winner is player ${this.gameScreen.mainGame.players.first()}!!!")
            this.winnerLabel.isVisible = true
        }
        //Declares a tie if the number of players is 0
        if (this.gameScreen.mainGame.players.size == 0) {
            this.winnerLabel.setText("It's a tie!!!")
            this.winnerLabel.isVisible = true
        }
    }

}