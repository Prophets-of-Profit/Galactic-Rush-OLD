package com.prophetsofprofit.galacticrush.graphics.screen

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ScalingViewport
import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.prophetsofprofit.galacticrush.Main
import com.prophetsofprofit.galacticrush.Networker
import ktx.app.KtxScreen

/**
 * The screen where the host of the game can wait for clients and set the game settings
 */
class WaitForClientScreen(val game: Main): KtxScreen {

    //The stage that contains all of the UI components
    val uiContainer = Stage(ScalingViewport(Scaling.stretch, this.game.camera.viewportWidth, this.game.camera.viewportHeight))

    /**
     * Initializes the networker as a host
     */
    init {
        Networker.init(false)
        Networker.getServer().addListener(object: Listener() {
            /**
             * What happens when someone tries to connect to the host
             */
            override fun connected(connection: Connection?) {
                //TODO: make
            }
        })
    }

    /**
     * Draws the ui of the screen
     */
    override fun render(delta: Float) {
        this.uiContainer.draw()
    }

    /**
     * Updates the uiContainer on window resize
     */
    override fun resize(width: Int, height: Int) {
        this.uiContainer.viewport.update(width, height)
    }

}