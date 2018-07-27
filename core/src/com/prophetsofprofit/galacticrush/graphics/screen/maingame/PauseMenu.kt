package com.prophetsofprofit.galacticrush.graphics.screen.maingame

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import ktx.scene2d.Scene2DSkin

/**
 * The actor group which makes up the game menu which handles saving,
 * quitting, and options
 */
class PauseMenu(gameScreen: MainGameScreen): Panel(gameScreen, "", gameScreen.uiCamera.viewportWidth / 5, gameScreen.uiCamera.viewportHeight / 2) {

    //The button which resumes the game from the menu
    val menuResume = TextButton("Resume", Scene2DSkin.defaultSkin)
    //The button which opens the options for the game
    val menuOptions = TextButton("Options", Scene2DSkin.defaultSkin)
    //The button which saves the game
    val menuSave = TextButton("Save", Scene2DSkin.defaultSkin)
    //The button which quits the game
    val menuQuit = TextButton("Quit", Scene2DSkin.defaultSkin)

    init {
        this.menuResume.setPosition(this.screen.uiCamera.viewportWidth / 2,
                this.screen.uiCamera.viewportHeight / 2 + 3 * this.menuResume.height, Align.center)
        this.menuOptions.setPosition(this.screen.uiCamera.viewportWidth / 2,
                this.screen.uiCamera.viewportHeight / 2 + this.menuOptions.height, Align.center)
        this.menuSave.setPosition(this.screen.uiCamera.viewportWidth / 2,
                this.screen.uiCamera.viewportHeight / 2 - this.menuSave.height, Align.center)
        this.menuQuit.setPosition(this.screen.uiCamera.viewportWidth / 2,
                this.screen.uiCamera.viewportHeight / 2 - 3 * this.menuSave.height, Align.center)
        this.isVisible = false
        this.menuResume.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                isVisible = false
            }
        })
        this.menuOptions.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                gameScreen.options.isVisible = true
                isVisible = false
            }
        })
        this.menuSave.addListener(object : ClickListener() {
            //TODO
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
            }
        })
        this.menuQuit.addListener(object : ClickListener() {
            //TODO
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                gameScreen.quitConfirmation.isVisible = true
                isVisible = false
            }
        })
        this.addActor(this.menuResume)
        this.addActor(this.menuOptions)
        this.addActor(this.menuSave)
        this.addActor(this.menuQuit)
    }

}