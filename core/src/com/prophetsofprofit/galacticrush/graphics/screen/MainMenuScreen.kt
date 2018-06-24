package com.prophetsofprofit.galacticrush.graphics.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.prophetsofprofit.galacticrush.Main
import ktx.app.KtxScreen
import ktx.app.use

/**
 * The screen that handles all main menu actions and display and layout and such
 */
class MainMenuScreen(val game: Main) : KtxScreen {

    //Music to play in the main menu
    val music = Gdx.audio.newMusic(Gdx.files.internal("music/TheIntergalactic.mp3"))

    init {
        this.game.batch.projectionMatrix = this.game.camera.combined
        music.isLooping = true
        music.play()
    }

    /**
     * The periodic update/draw method
     * Passes in the amount of time since last render
     */
    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 1f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        this.game.batch.use {
            this.game.textDrawer.draw(this.game.batch, "Do you approve of this architecture?", 100f, 100f)
        }
        if (Gdx.input.isTouched) {
            music.stop()
            game.screen = GalaxyLoadingScreen(game)
        }
    }

}
