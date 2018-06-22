package com.prophetsofprofit.galacticrush.graphics.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.prophetsofprofit.galacticrush.Main
import ktx.app.KtxScreen
import ktx.app.use

/**
 * The screen that first shows up that shows the user who made this game (US!)
 */
class Splash(val game: Main): KtxScreen {

    //How long to wait before going to MainMenu
    var wait = 5f
    //The TTS sound that will play on opening
    val sound = Gdx.audio.newSound(Gdx.files.internal("meta/ProphetsOfProfitTTS.mp3"))
    //The prophets of profit logo
    val logo = Sprite(Texture("meta/CapitalMan.png"))

    //Initializes all assets
    init {
        this.sound.play()
        //TODO: center logo here
    }

    /**
     * TODO: doc
     */
    override fun render(delta: Float) {
        this.wait -= delta
        if (this.wait <= 0) {
            this.game.screen = MainMenu(this.game)
        }
        this.game.batch.use {
            this.logo.draw(it)
        }
    }

}