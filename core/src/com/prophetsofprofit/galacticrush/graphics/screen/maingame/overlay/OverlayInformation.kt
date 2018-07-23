package com.prophetsofprofit.galacticrush.graphics.screen.maingame.overlay

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.MainGameScreen
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.Scene2dDsl

/**
 * Handles showing the information that's available to the player at all times
 * e.g. money, turns played
 */
class OverlayInformation(val gameScreen: MainGameScreen, val labelWidth: Float, val labelHeight: Float): Group() {

    val turnsLabel = Label("Turns Played", Scene2DSkin.defaultSkin, "ui")
    val moneyLabel = Label("Credits", Scene2DSkin.defaultSkin, "ui")

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
        this.addActor(this.turnsLabel)
        this.addActor(this.moneyLabel)
    }

    /**
     * Updates the values of the information displayed in the overlay
     */
    fun update() {
        this.turnsLabel.setText("Turns Played: ${this.gameScreen.mainGame.turnsPlayed}")
        this.moneyLabel.setText("Credit: ${this.gameScreen.mainGame.money[this.gameScreen.player.id]}")
    }

}