package com.prophetsofprofit.galacticrush.graphics.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.prophetsofprofit.galacticrush.Main
import ktx.app.KtxScreen
import ktx.app.use
import kotlin.math.min

/**
 * The screen that first shows up that shows the user who made this game (the Prolific Prophets of Profit)
 * Displays for a short time and plays a quick jingle before procceding to the main menu 
 */
class Splash(val game: Main): KtxScreen {

    //How long to wait before going to MainMenu
    val maxWait = 5f
    //How long the user has waited
    var currentWait = 0f
    //The sound that will play on opening
    val sound = Gdx.audio.newSound(Gdx.files.internal("meta/ProphetsOfProfit.ogg"))
    //The prophets of profit logo
    val logo = Sprite(Texture("meta/CapitalMan.png"))

    //Initializes all assets
    init {
        this.sound.play()
        this.game.batch.projectionMatrix = this.game.camera.combined
        this.logo.setCenter(this.game.camera.viewportWidth / 2, this.game.camera.viewportHeight / 2)
    }

    /**
     * Draws the splash screen
     * Updates how long the user has waited, and will move to menu screen once user has waited long enough
     * Renders the prophets of profits logo with increasing visibility with maximum visibility after the screen has appeared for half of its duration
     */
    override fun render(delta: Float) {
        this.currentWait += delta
        if (this.currentWait >= this.maxWait) {
            this.game.screen = MainMenu(this.game)
        }
        this.logo.setAlpha(min(1f, 2 * this.currentWait / this.maxWait))
        this.game.batch.use {
            this.logo.draw(it)
        }
    }

}