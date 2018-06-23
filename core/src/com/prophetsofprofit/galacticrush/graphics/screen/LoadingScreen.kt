package com.prophetsofprofit.galacticrush.graphics.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.prophetsofprofit.galacticrush.Main
import com.prophetsofprofit.galacticrush.logic.map.Galaxy
import ktx.app.KtxScreen
import ktx.app.use

/**
 * The screen that handles distracting the players
 * TODO: Make this universally applicable
 */
class LoadingScreen(val game: Main): KtxScreen {

    var wait = 0
    //Measures time spent loading
    var timeSpent = 0f
    //Defines how long a frame should be on screen
    val framesTime = 2.25f
    //The index of the image that is being drawn;
    var image = 0
    //A list of all the frames
    val frames = Array(28, { Texture("animations/loading/LoadingScreen$it.png" ) })
    //The world being initiated
    var world: Galaxy? = null

    //Initializes all assets
    init {
        Thread {
            world = Galaxy(50)
        }.start()
        this.game.camera = OrthographicCamera()
        this.game.camera.setToOrtho(false, 1600f, 900f)
        this.game.batch.projectionMatrix = this.game.camera.combined
    }

    /**
     * How the screen is drawn
     * Draws a frame of the animation every frame
     */
    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        if(world != null) {
            this.game.screen = MainGame(this.game, world!!)
        }
        this.timeSpent += delta
        this.image = ((this.timeSpent % this.framesTime) / 2.25 * 28).toInt()
        this.game.batch.use {
            it.draw(frames[image], 500f, 150f, 600f, 600f, 0, 0, 64, 64, false, false)
        }
    }

}
