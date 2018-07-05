package com.prophetsofprofit.galacticrush.graphics.screen

import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.prophetsofprofit.galacticrush.Main
import com.prophetsofprofit.galacticrush.Networker

/**
 * The screen where you can find a host as a client
 */
class FindHostScreen(game: Main) : GalacticRushScreen(game) {

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

    override fun draw(delta: Float) {}
    override fun leave() {}

}