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
 * The screen where you can find a host as a client
 */
class FindHostScreen(val game: Main): KtxScreen {

    //The container for all of the ui components
    val uiContainer = Stage(ScalingViewport(Scaling.stretch, this.game.camera.viewportWidth, this.game.camera.viewportHeight))

    /**
     * Initializes the networker as a client
     */
    init {
        Networker.init(true)
        Networker.getClient().addListener(object: Listener() {
            override fun connected(connection: Connection?) {
                //TODO: make
            }
        })
    }

    /**
     * Draws all of the ui components
     */
    override fun render(delta: Float) {
        this.uiContainer.draw()
    }

    /**
     * Notifies the ui container of window resize
     */
    override fun resize(width: Int, height: Int) {
        this.uiContainer.viewport.update(width, height)
    }

}