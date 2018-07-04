package com.prophetsofprofit.galacticrush.graphics.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ScalingViewport
import com.prophetsofprofit.galacticrush.Main
import com.prophetsofprofit.galacticrush.Networker
import com.prophetsofprofit.galacticrush.graphics.MusicPlayer
import com.prophetsofprofit.galacticrush.graphics.screen.loading.HostLoadingScreen
import com.prophetsofprofit.galacticrush.logic.player.LocalPlayer
import ktx.app.KtxScreen
import ktx.app.use
import ktx.scene2d.Scene2DSkin

/**
 * The screen that handles all main menu actions and display and layout and such
 */
class MainMenuScreen(val game: Main) : KtxScreen {

    //Music to play in the main menu
    val music = MusicPlayer(Array(1, { "meta/TheIntergalactic.mp3" }))
    val backgroundTexture = Texture("meta/Background.png")
    val titleTexture = Texture("meta/Title.png")
    var timeSpent = 0f
    val uiContainer = Stage(ScalingViewport(Scaling.stretch, this.game.camera.viewportWidth, this.game.camera.viewportHeight))

    init {
        this.game.batch.projectionMatrix = this.game.camera.combined
        val hostGameButton = TextButton("Host a Game", Scene2DSkin.defaultSkin)
        val joinGameButton = TextButton("Join a Game", Scene2DSkin.defaultSkin)
        val optionsButton = TextButton("Options", Scene2DSkin.defaultSkin)
        hostGameButton.setPosition(this.uiContainer.width * 0.1f, this.uiContainer.height * 0.3f, Align.center)
        joinGameButton.setPosition(this.uiContainer.width * 0.1f, this.uiContainer.height * 0.2f, Align.center)
        optionsButton.setPosition(this.uiContainer.width * 0.1f, this.uiContainer.height * 0.1f, Align.center)
        this.uiContainer.addActor(hostGameButton)
        this.uiContainer.addActor(joinGameButton)
        this.uiContainer.addActor(optionsButton)
        hostGameButton.addListener(object: ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                music.dispose()
                game.screen = HostLoadingScreen(game, Array(1) { LocalPlayer(0) })
            }
        })
        optionsButton.addListener(object: ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                music.dispose()
                game.screen = OptionsScreen(game, this@MainMenuScreen)
            }
        })
        Gdx.input.inputProcessor = this.uiContainer
        Networker.reset()
    }

    /**
     * The periodic update/draw method
     * Passes in the amount of time since last render
     */
    override fun render(delta: Float) {
        timeSpent += delta
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        this.uiContainer.act(delta)
        this.game.batch.use {
            it.draw(backgroundTexture, -200f * (this.timeSpent % 8f), 0f, 1600f, 900f)
            it.draw(backgroundTexture, -200f * (this.timeSpent % 8f) + 1600, 0f, 1600f, 900f)
            it.draw(titleTexture, 0f, 400f, 1600f, 500f)
        }
        this.uiContainer.draw()
        //Update the playing music
        this.music.update()
    }

    /**
     * Alerts the stage of changed window dimensions
     */
    override fun resize(width: Int, height: Int) {
        this.uiContainer.viewport.update(width, height)
    }

}
