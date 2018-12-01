package com.prophetsofprofit.galacticrush.graphics

import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Slider
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.utils.Align
import com.prophetsofprofit.galacticrush.Main
import com.prophetsofprofit.galacticrush.Options
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
         * Removes this if an options window is already up
         * Otherwise, places the options window in the center
         */
        if ((this.main.screen as GalacticRushScreen).uiContainer.actors.asSequence().any { actor -> actor is OptionsWindow }) {
            this.addAction(Actions.removeActor())
        } else {
            this.setPosition((this.main.screen as GalacticRushScreen).uiContainer.width / 2, (this.main.screen as GalacticRushScreen).uiContainer.height / 2, Align.center)
        }

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

        /*
         * CANCEL BUTTON
         */
        val cancelButton = TextButton("Cancel", Scene2DSkin.defaultSkin)
        this.add(cancelButton).padTop(10f)
        cancelButton.onClick { this.addAction(Actions.removeActor()) }

        /*
         * CONFIRM BUTTON
         */
        val confirmButton = TextButton("Confirm", Scene2DSkin.defaultSkin)
        this.add(confirmButton).padTop(10f)
        confirmButton.onClick {
            this.main.userOptions = Options(musicSlider.value, soundSlider.value)
            this.addAction(Actions.removeActor())
        }

        /*
         * OPTIONS WINDOW
         */
        this.pack()
        this.setSize(this.width + 10f, this.height + 10f)
    }

}