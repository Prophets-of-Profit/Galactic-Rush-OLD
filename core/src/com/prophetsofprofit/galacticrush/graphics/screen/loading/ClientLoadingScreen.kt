package com.prophetsofprofit.galacticrush.graphics.screen.loading

import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.prophetsofprofit.galacticrush.Main
import com.prophetsofprofit.galacticrush.Networker
import com.prophetsofprofit.galacticrush.graphics.screen.MainGameScreen
import com.prophetsofprofit.galacticrush.logic.Game
import com.prophetsofprofit.galacticrush.logic.player.NetworkPlayer

/**
 * The screen that handles loading and initializing the galaxy
 * Should be called from host, as only host should actually be constructing galaxy
 */
class ClientLoadingScreen(game: Main) : LoadingScreen(game) {

    //The player that this client is; will be received from host
    var player: NetworkPlayer? = null

    /**
     * Waits for the connection to receive the player, and then adds a listener to set the game of the player whenever a game is received
     */
    override fun load() {
        Networker.getClient().addListener(object : Listener() {
            override fun received(connection: Connection?, obj: Any?) {
                if (obj !is NetworkPlayer) {
                    return
                }
                player = obj
                Networker.getClient().addListener(object : Listener() {
                    override fun received(connection: Connection?, obj: Any?) {
                        if (obj !is Game) {
                            return
                        }
                        player!!.game = obj
                    }
                })
                Networker.getClient().removeListener(this)
            }
        })
        while (this.player == null) {
            Thread.sleep(250)
        }
    }

    /**
     * Moves the game to the MainGameScreen
     */
    override fun onLoad() {
        this.game.screen = MainGameScreen(this.game, this.player!!)
    }

}
