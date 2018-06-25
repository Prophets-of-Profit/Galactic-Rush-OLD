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
class ClientLoadingScreen(game: Main, val connectionId: Int) : LoadingScreen(game) {

    var mainGame: Game? = null

    /**
     * Waits for the connection to receive the game
     */
    override fun load() {
        Networker.getClient().addListener(object : Listener() {
            override fun received(connection: Connection?, obj: Any?) {
                if (connection?.id == connectionId && obj is Game) {
                    mainGame = obj
                }
                Networker.getClient().removeListener(this)
            }
        })
        while (this.mainGame == null) {
            Thread.sleep(250)
        }
    }

    /**
     * Moves the game to the MainGameScreen
     */
    override fun onLoad() {
        this.game.screen = MainGameScreen(this.game, this.mainGame!!.players.first { it is NetworkPlayer && it.connectionId == this.connectionId })
    }

}
