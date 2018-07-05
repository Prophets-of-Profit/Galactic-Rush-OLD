package com.prophetsofprofit.galacticrush.graphics.screen

import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.prophetsofprofit.galacticrush.Main
import com.prophetsofprofit.galacticrush.Networker

/**
 * The screen where the host of the game can wait for clients and set the game settings
 */
class WaitForClientScreen(game: Main) : GalacticRushScreen(game) {

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

    override fun draw(delta: Float) {}
    override fun leave() {}

}