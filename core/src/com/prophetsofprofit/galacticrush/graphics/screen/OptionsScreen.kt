package com.prophetsofprofit.galacticrush.graphics.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Slider
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ScalingViewport
import com.prophetsofprofit.galacticrush.Main
import com.prophetsofprofit.galacticrush.Options
import ktx.app.KtxScreen
import ktx.scene2d.Scene2DSkin

/**
 * The screen where the user can change their options
 * TODO: this screen errors right now because it uses sliders that error because there is no default-horizontal slider style
 */
class OptionsScreen(val game: Main, val returnScreen: KtxScreen): KtxScreen {

    //The container for all of the UI elements
    val uiContainer = Stage(ScalingViewport(Scaling.stretch, this.game.camera.viewportWidth, this.game.camera.viewportHeight))
    //The horizontal slider for controlling music volume
    val musicVolumeSlider = Slider(0f, 100f, 1f, false, Scene2DSkin.defaultSkin)
    //The horizontal slider for controlling sound volume
    val soundVolumeSlider = Slider(0f, 100f, 1f, false, Scene2DSkin.defaultSkin)

    /**
     * Initializes UI components
     */
    init {
        this.game.batch.projectionMatrix = this.game.camera.combined
        Gdx.input.inputProcessor = this.uiContainer
        val backButton = TextButton("Back", Scene2DSkin.defaultSkin) //TODO: change for ImageButton with back icon
        backButton.addListener(object: ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                exitScreen()
            }
        })
        //TODO: make reset to default options button
        val musicVolumeLabel = Label("Music Volume", Scene2DSkin.defaultSkin)
        val soundVolumeLabel = Label("Sound Volume", Scene2DSkin.defaultSkin)
        musicVolumeSlider.value = game.userOptions.musicVolume.toFloat()
        soundVolumeSlider.value = game.userOptions.soundVolume.toFloat()
        musicVolumeLabel.setPosition(this.uiContainer.width * 0.5f, this.uiContainer.height * 0.9f, Align.center)
        musicVolumeSlider.setPosition(this.uiContainer.width * 0.5f, this.uiContainer.height * 0.75f, Align.center)
        soundVolumeLabel.setPosition(this.uiContainer.width * 0.5f, this.uiContainer.height * 0.60f, Align.center)
        soundVolumeSlider.setPosition(this.uiContainer.width * 0.5f, this.uiContainer.height * 0.45f, Align.center)
        backButton.setPosition(this.uiContainer.width * 0.5f, this.uiContainer.height * 0.1f, Align.center)
    }

    /**
     * When the options screen is to be left, the new options are saved and the returnScreen is set as the game's screen
     */
    private fun exitScreen() {
        game.userOptions = Options(musicVolumeSlider.value.toInt(), soundVolumeSlider.value.toInt())
        game.screen = returnScreen
    }

    /**
     * Drawing just clears the screen and draws all the UI components
     */
    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        this.uiContainer.draw()
    }

    /**
     * Resizing just alerts the uiContainer of the new window dimensions
     */
    override fun resize(width: Int, height: Int) {
        this.uiContainer.viewport.update(width, height)
    }

}