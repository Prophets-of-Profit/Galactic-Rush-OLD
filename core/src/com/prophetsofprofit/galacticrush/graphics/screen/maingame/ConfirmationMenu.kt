package com.prophetsofprofit.galacticrush.graphics.screen.maingame

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import ktx.scene2d.Scene2DSkin

/**
 * A menu which asks for confirmation from the user
 * Can perform a variety of actions after confirmation
 */
class ConfirmationMenu(gameScreen: MainGameScreen, prompt: String, val prevGroup: Group, val action: () -> Unit): Panel(gameScreen, prompt, gameScreen.uiCamera.viewportWidth / 3, gameScreen.uiCamera.viewportHeight / 7) {

    /**
     * Sets up the confirmation button buttons
     */
    init {
        //The button which causes the action to be performed
        val confirmationButtonYes = TextButton("Yes", Scene2DSkin.defaultSkin)
        //The button which causes the action to not be performed
        val confirmationButtonNo = TextButton("No", Scene2DSkin.defaultSkin)
        confirmationButtonYes.setPosition(this.screen.uiCamera.viewportWidth / 2 - confirmationButtonYes.width,
                this.screen.uiCamera.viewportHeight / 2 - confirmationButtonYes.height)
        confirmationButtonNo.setPosition(this.screen.uiCamera.viewportWidth / 2,
                this.screen.uiCamera.viewportHeight / 2 - confirmationButtonYes.height)
        confirmationButtonYes.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                action()
                isVisible = false
            }
        })
        confirmationButtonNo.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                prevGroup.isVisible = true
                isVisible = false
            }
        })
        this.addActor(confirmationButtonYes)
        this.addActor(confirmationButtonNo)
        this.isVisible = false
    }

}