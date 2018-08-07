package com.prophetsofprofit.galacticrush.graphics

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Slider
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.prophetsofprofit.galacticrush.Main
import com.prophetsofprofit.galacticrush.Options
import com.prophetsofprofit.galacticrush.graphics.screen.GalacticRushScreen
import ktx.scene2d.Scene2DSkin

/**
 * The screen where the user can change their options
 */
class OptionsMenu(val game: Main, screen: GalacticRushScreen) : ModalWindow(screen, "Options", screen.uiCamera.viewportWidth / 2, screen.uiCamera.viewportHeight / 2) {

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
                applyOptions()
                isVisible = false
            }
        })
        //TODO: make reset to default options button
        val musicVolumeLabel = Label("Music Volume", Scene2DSkin.defaultSkin)
        val soundVolumeLabel = Label("Sound Volume", Scene2DSkin.defaultSkin)
        musicVolumeSlider.value = game.userOptions.musicVolume
        soundVolumeSlider.value = game.userOptions.soundVolume
        this.add(musicVolumeLabel).pad(10f)
        this.add(this.musicVolumeSlider)
        this.row()
        this.add(soundVolumeLabel).pad(10f)
        this.add(this.soundVolumeSlider)
        this.row()
        this.add(backButton).colspan(2).center().padTop(10f)
        this.isVisible = false
    }

    /**
     * When the options screen is to be left, the new options are saved and the returnScreen is set as the game's screen
     */
    private fun applyOptions() {
        this.game.userOptions = Options(musicVolumeSlider.value, soundVolumeSlider.value)
    }

}