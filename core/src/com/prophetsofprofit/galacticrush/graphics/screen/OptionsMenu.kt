package com.prophetsofprofit.galacticrush.graphics.screen

import com.badlogic.gdx.scenes.scene2d.Group
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
class OptionsMenu(val game: Main): Group() {

    //The horizontal slider for controlling music musicVolume
    val musicVolumeSlider = Slider(0f, 1f, 0.05f, false, Scene2DSkin.defaultSkin)
    //The horizontal slider for controlling sound musicVolume
    val soundVolumeSlider = Slider(0f, 1f, 0.05f, false, Scene2DSkin.defaultSkin)

    /**
     * Initializes UI components
     */
    init {
        val basePanel = Label("Options", Scene2DSkin.defaultSkin, "ui")
        basePanel.width = this.game.camera.viewportWidth * 0.5f
        basePanel.height = this.game.camera.viewportHeight * 0.9f
        basePanel.setAlignment(Align.top)
        basePanel.setPosition(this.game.camera.viewportWidth * 0.5f, this.game.camera.viewportHeight * 0.5f, Align.center)
        val backButton = TextButton("Back", Scene2DSkin.defaultSkin) //TODO: change for ImageButton with back icon
        backButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                applyOptions()
                isVisible = false
            }
        })
        //TODO: make reset to default options button
        val musicVolumeLabel = Label("Music Volume", Scene2DSkin.defaultSkin)
        val soundVolumeLabel = Label("Sound Volume", Scene2DSkin.defaultSkin)
        musicVolumeSlider.value = game.userOptions.musicVolume
        soundVolumeSlider.value = game.userOptions.soundVolume
        musicVolumeLabel.setPosition(this.game.camera.viewportWidth * 0.5f, this.game.camera.viewportHeight * 0.9f, Align.center)
        musicVolumeSlider.setPosition(this.game.camera.viewportWidth * 0.5f, this.game.camera.viewportHeight * 0.75f, Align.center)
        soundVolumeLabel.setPosition(this.game.camera.viewportWidth * 0.5f, this.game.camera.viewportHeight * 0.60f, Align.center)
        soundVolumeSlider.setPosition(this.game.camera.viewportWidth * 0.5f, this.game.camera.viewportHeight * 0.45f, Align.center)
        backButton.setPosition(this.game.camera.viewportWidth * 0.5f, this.game.camera.viewportHeight * 0.1f, Align.center)
        this.addActor(basePanel)
        this.addActor(musicVolumeSlider)
        this.addActor(soundVolumeSlider)
        this.addActor(musicVolumeLabel)
        this.addActor(soundVolumeLabel)
        this.addActor(backButton)
        this.isVisible = false
    }

    /**
     * When the options screen is to be left, the new options are saved and the returnScreen is set as the game's screen
     */
    private fun applyOptions() {
        game.userOptions = Options(musicVolumeSlider.value, soundVolumeSlider.value)
    }

}