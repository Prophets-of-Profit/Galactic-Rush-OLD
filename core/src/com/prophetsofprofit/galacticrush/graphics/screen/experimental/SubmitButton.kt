package com.prophetsofprofit.galacticrush.graphics.screen.experimental

import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import ktx.scene2d.Scene2DSkin

/**
 * A class that represents the button the player will use to submit their changes
 */
class SubmitButton(val gameScreen: GameScreen) : TextButton("SUBMIT", Scene2DSkin.defaultSkin) {

    /**
     * Initializes all of the button actions and behaviours
     */
    init {
        //Button is only enabled when the game is waiting for the player
        this.addAction(object : Action() {
            override fun act(delta: Float): Boolean {
                isDisabled = !gameScreen.mainGame.waitingOn.contains(gameScreen.player.id)
                return false
            }
        })

        //When clicked, changes get submitted and the button disables until the next phase
        this.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                gameScreen.player.submitCurrentChanges()
                gameScreen.mainGame.waitingOn.remove(gameScreen.player.id)
            }
        })

        this.setPosition(0.95f * this.gameScreen.uiCamera.viewportWidth, 0.05f * this.gameScreen.uiCamera.viewportHeight, Align.bottomRight)
    }

}