package com.prophetsofprofit.galacticrush.graphics.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.prophetsofprofit.galacticrush.Main
import ktx.app.KtxScreen
import ktx.app.use
import ktx.scene2d.Scene2DSkin
import kotlin.math.min

/**
 * The screen that first shows up that shows the user who made this game (the Prolific Prophets of Profit)
 * Displays for a short time and plays a quick jingle before procceding to the main menu
 */
class SplashScreen(val game: Main) : KtxScreen {

    //How long to wait before going to MainMenuScreen
    val minWait = 5f
    //How long the user has waited
    var currentWait = 0f
    //The sound that will play on opening
    val sound = Gdx.audio.newSound(Gdx.files.internal("meta/ProphetsOfProfit.mp3"))!!
    //The prophets of profit logo
    val logo = Sprite(Texture("meta/CapitalMan.png"))
    //The prophets of profit title
    val titleLabel = Label("Prophets\nof\nProfit", Scene2DSkin.defaultSkin)

    /**
     * Initializes splash assets
     */
    init {
        this.game.batch.projectionMatrix = this.game.camera.combined
        this.logo.setCenter(this.game.camera.viewportWidth / 2, this.game.camera.viewportHeight / 2)
        this.logo.scale(2f)
        this.titleLabel.setPosition(this.game.camera.viewportWidth / 2, this.game.camera.viewportHeight / 1.25f, Align.center)
        this.titleLabel.setAlignment(Align.center, Align.center)
        //TODO: sound doesn't play on Android for some reason
        this.sound.play()
    }

    /**
     * Draws the splash screen
     * Updates how long the user has waited, and will move to menu screen once user has waited long enough
     * Renders the prophets of profits logo with increasing visibility with maximum visibility after the screen has appeared for half of its duration
     */
    override fun render(delta: Float) {
        //Color background in black
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        //Update wait time or go to next screen if wait is enough
        this.currentWait += delta
        if (this.currentWait >= this.minWait) {
            this.sound.dispose()
            this.game.screen = MainMenuScreen(this.game)
        }
        //Update sprite alpha and draw sprite and text
        this.logo.setAlpha(min(1f, 2 * this.currentWait / this.minWait))
        this.game.batch.use {
            this.logo.draw(it)
            this.titleLabel.draw(it, 1f)
        }
    }

}