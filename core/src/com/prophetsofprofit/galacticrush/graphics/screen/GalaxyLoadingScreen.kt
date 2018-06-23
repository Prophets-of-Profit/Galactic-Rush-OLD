package com.prophetsofprofit.galacticrush.graphics.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.prophetsofprofit.galacticrush.Main
import com.prophetsofprofit.galacticrush.logic.Game
import com.prophetsofprofit.galacticrush.logic.Player
import com.prophetsofprofit.galacticrush.logic.map.Galaxy
import ktx.app.KtxScreen
import ktx.app.use

/**
 * The screen that handles loading the galaxy and displaying a fancy animation to show that the program isn't broken
 * TODO: Make this universally applicable, and not just load the galaxy
 */
class GalaxyLoadingScreen(val game: Main): KtxScreen {

    //Measures time spent loading
    var timeSpent = 0f
    //Defines how long a frame should be on screen
    val framesTime = 2.25f
    //The index of the image that is being drawn;
    var image = 0
    //A list of all the frames
    val frames = Array(28) { Texture("animations/loading/LoadingScreen$it.png" ) }
    //The world being initiated
    var mainGame: Game? = null

    /**
     * Starts initializing the galaxy and sets the screen coordinate system with the camera
     */
    init {
        Thread {
            mainGame = Game(Galaxy(100), Array(0, { Player() }))
        }.start()
        this.game.camera.setToOrtho(false, 1600f, 900f)
        this.game.batch.projectionMatrix = this.game.camera.combined
    }

    /**
     * How the screen is drawn
     * Draws an image of the animation every frame
     */
    override fun render(delta: Float) {
        //Clears the screen
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        //Checks if the world is done loading and changes screen once the world is ready
        if (mainGame != null) {
            this.game.screen = MainGameScreen(this.game, mainGame!!)
        }
        //Updates the time waited and updates the image used in the loading screen for the animation
        this.timeSpent += delta
        this.image = ((this.timeSpent % this.framesTime) / 2.25 * 28).toInt()
        this.game.batch.use {
            it.draw(frames[image], 500f, 150f, 600f, 600f, 0, 0, 64, 64, false, false)
        }
    }

}
