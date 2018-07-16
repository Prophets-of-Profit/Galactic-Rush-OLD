package com.prophetsofprofit.galacticrush.graphics.screen

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Slider
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.prophetsofprofit.galacticrush.Main
import com.prophetsofprofit.galacticrush.Options
import ktx.scene2d.Scene2DSkin

/**
 * The screen where the user can change their options
 */
class OptionsScreen(game: Main, val exitProcedure: () -> Unit) : GalacticRushScreen(game) {

    //The horizontal slider for controlling music musicVolume
    val musicVolumeSlider = Slider(0f, 1f, 0.05f, false, Scene2DSkin.defaultSkin)
    //The horizontal slider for controlling sound musicVolume
    val soundVolumeSlider = Slider(0f, 1f, 0.05f, false, Scene2DSkin.defaultSkin)

    /**
     * Initializes UI components
     */
    init {
        val backButton = TextButton("Back", Scene2DSkin.defaultSkin) //TODO: change for ImageButton with back icon
        backButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                exitScreen()
            }
        })
        //TODO: make reset to default options button
        val musicVolumeLabel = Label("Music Volume", Scene2DSkin.defaultSkin)
        val soundVolumeLabel = Label("Sound Volume", Scene2DSkin.defaultSkin)
        musicVolumeSlider.value = game.userOptions.musicVolume
        soundVolumeSlider.value = game.userOptions.soundVolume
        musicVolumeLabel.setPosition(this.uiContainer.width * 0.5f, this.uiContainer.height * 0.9f, Align.center)
        musicVolumeSlider.setPosition(this.uiContainer.width * 0.5f, this.uiContainer.height * 0.75f, Align.center)
        soundVolumeLabel.setPosition(this.uiContainer.width * 0.5f, this.uiContainer.height * 0.60f, Align.center)
        soundVolumeSlider.setPosition(this.uiContainer.width * 0.5f, this.uiContainer.height * 0.45f, Align.center)
        backButton.setPosition(this.uiContainer.width * 0.5f, this.uiContainer.height * 0.1f, Align.center)
        this.uiContainer.addActor(musicVolumeSlider)
        this.uiContainer.addActor(soundVolumeSlider)
        this.uiContainer.addActor(musicVolumeLabel)
        this.uiContainer.addActor(soundVolumeLabel)
        this.uiContainer.addActor(backButton)
    }

    /**
     * When the options screen is to be left, the new options are saved and the returnScreen is set as the game's screen
     */
    private fun exitScreen() {
        game.userOptions = Options(musicVolumeSlider.value, soundVolumeSlider.value)
        this.exitProcedure()
    }

    override fun draw(delta: Float) {}
    override fun leave() {}

}