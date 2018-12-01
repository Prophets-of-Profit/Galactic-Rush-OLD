package com.prophetsofprofit.galacticrush.graphics

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Slider
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.prophetsofprofit.galacticrush.Main
import ktx.scene2d.Scene2DSkin

/**
 * A popup window for allowing the user to change/alter their options
 */
class OptionsWindow(val main: Main) : Window("Options", Scene2DSkin.defaultSkin) {

    /**
     * Adds all the controls for allowing changes to the options
     */
    init {
        /*
         * MUSIC VOLUME SLIDER
         */
        val musicSlider = Slider(0f, 1f, 0.05f, false, Scene2DSkin.defaultSkin)
        musicSlider.value = this.main.userOptions.musicVolume
        this.add(Label("Music Volulme", Scene2DSkin.defaultSkin)).fillX().padRight(5f)
        this.add(musicSlider).fillX().expandX().row()

        /*
         * SOUND VOLUME SLIDER
         */
        val soundSlider = Slider(0f, 1f, 0.05f, false, Scene2DSkin.defaultSkin)
        soundSlider.value = this.main.userOptions.soundVolume
        this.add(Label("Sound Volume", Scene2DSkin.defaultSkin)).fillX().padRight(5f)
        this.add(soundSlider).fillX().expandX().row()

        //TODO: save and exit buttons

        /*
         * OPTIONS WINDOW
         */
        this.pack()
        this.setSize(this.width + 10f, this.height + 10f)
    }

}