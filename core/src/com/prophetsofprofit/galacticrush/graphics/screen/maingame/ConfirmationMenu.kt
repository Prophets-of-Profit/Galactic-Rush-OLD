package com.prophetsofprofit.galacticrush.graphics.screen.maingame

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import ktx.scene2d.Scene2DSkin

/**
 * A menu which asks for confirmation from the user
 * Can perform a variety of actions after confirmation
 */
class ConfirmationMenu(val gameScreen: MainGameScreen, prompt: String, val prevGroup: Group, val action: () -> Unit): Group() {

    //The background of the menu
    val confirmationLabel = Label(prompt, Scene2DSkin.defaultSkin, "ui")
    //The button which causes the action to be performed
    val confirmationButtonYes = TextButton("Yes", Scene2DSkin.defaultSkin)
    //The button which causes the action to not be performed
    val confirmationButtonNo = TextButton("No", Scene2DSkin.defaultSkin)

    init {
        this.confirmationLabel.setAlignment(Align.top)
        this.confirmationLabel.width = this.gameScreen.game.camera.viewportWidth / 3
        this.confirmationLabel.height = this.gameScreen.game.camera.viewportHeight / 7
        this.confirmationLabel.setPosition(this.gameScreen.game.camera.viewportWidth / 2,
                this.gameScreen.game.camera.viewportHeight / 2, Align.center)
        this.confirmationButtonYes.setPosition(this.gameScreen.game.camera.viewportWidth / 2 - this.confirmationButtonYes.width,
                this.gameScreen.game.camera.viewportHeight / 2 - this.confirmationButtonYes.height)
        this.confirmationButtonNo.setPosition(this.gameScreen.game.camera.viewportWidth / 2,
                this.gameScreen.game.camera.viewportHeight / 2 - this.confirmationButtonYes.height)
        this.confirmationButtonYes.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                action()
                isVisible = false
            }
        })
        this.confirmationButtonNo.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                prevGroup.isVisible = true
                isVisible = false
            }
        })
        this.addActor(confirmationLabel)
        this.addActor(confirmationButtonYes)
        this.addActor(confirmationButtonNo)
        this.isVisible = false
    }

}