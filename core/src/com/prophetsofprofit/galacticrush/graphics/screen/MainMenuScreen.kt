package com.prophetsofprofit.galacticrush.graphics.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.prophetsofprofit.galacticrush.Main
import com.prophetsofprofit.galacticrush.Networker
import com.prophetsofprofit.galacticrush.graphics.screen.loading.HostLoadingScreen
import com.prophetsofprofit.galacticrush.graphics.screen.networkinguserinterface.FindHostScreen
import com.prophetsofprofit.galacticrush.graphics.screen.networkinguserinterface.WaitForClientScreen
import com.prophetsofprofit.galacticrush.logic.drone.instructions.giveInstructions
import com.prophetsofprofit.galacticrush.logic.player.LocalPlayer
import ktx.app.use
import ktx.scene2d.Scene2DSkin

/**
 * The screen that handles all main menu actions and display and layout and such
 */
class MainMenuScreen(game: Main) : GalacticRushScreen(game, arrayOf("meta/TheIntergalactic.mp3")) {

    val backgroundTexture = Texture("meta/Background.png")
    val titleTexture = Texture("meta/Title.png")
    var timeSpent = 0f

    init {
        val hostGameButton = TextButton("Host a Game", Scene2DSkin.defaultSkin)
        val joinGameButton = TextButton("Join a Game", Scene2DSkin.defaultSkin)
        val optionsButton = TextButton("Options", Scene2DSkin.defaultSkin)
        val exitGameButton = TextButton("Exit", Scene2DSkin.defaultSkin)
        hostGameButton.setPosition(this.uiContainer.width * 0.1f, this.uiContainer.height * 0.4f, Align.center)
        joinGameButton.setPosition(this.uiContainer.width * 0.1f, this.uiContainer.height * 0.3f, Align.center)
        optionsButton.setPosition(this.uiContainer.width * 0.1f, this.uiContainer.height * 0.2f, Align.center)
        exitGameButton.setPosition(this.uiContainer.width * 0.1f, this.uiContainer.height * 0.1f, Align.center)
        this.uiContainer.addActor(hostGameButton)
        this.uiContainer.addActor(joinGameButton)
        this.uiContainer.addActor(optionsButton)
        this.uiContainer.addActor(exitGameButton)
        hostGameButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                game.screen = WaitForClientScreen(game)
                dispose()
            }
        })
        joinGameButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                game.screen = FindHostScreen(game)
                dispose()
            }
        })
        optionsButton.addListener(object : ClickListener() {
            //TODO: make options screen able to switch back to the existing screen
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                this@MainMenuScreen.options.isVisible = true
            }
        })
        exitGameButton.addListener(object: ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                Gdx.app.exit()
            }
        })
        Networker.reset()

        val tempDemoButton = TextButton("DEV", Scene2DSkin.defaultSkin)
        tempDemoButton.setPosition(this.uiContainer.width * 0.9f, this.uiContainer.height * 0.15f, Align.center)
        tempDemoButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                game.screen = HostLoadingScreen(game, arrayOf(LocalPlayer(0)))
                giveInstructions(arrayOf(0))
                dispose()
            }
        })
        this.uiContainer.addActor(tempDemoButton)
    }

    /**
     * The periodic update/draw method
     * Passes in the amount of time since last render
     */
    override fun draw(delta: Float) {
        timeSpent += delta
        this.game.batch.use {
            it.draw(backgroundTexture, -200f * (this.timeSpent % 8f), 0f, 1600f, 900f)
            it.draw(backgroundTexture, -200f * (this.timeSpent % 8f) + 1600, 0f, 1600f, 900f)
            it.draw(titleTexture, 0f, 400f, 1600f, 500f)
        }
    }

    /**
     * Disposes of held textures and stops the music player upon screen leave
     */
    override fun leave() {
        this.titleTexture.dispose()
        this.backgroundTexture.dispose()
    }

}
