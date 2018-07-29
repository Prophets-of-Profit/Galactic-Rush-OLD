package com.prophetsofprofit.galacticrush.graphics.screen.loading

import com.prophetsofprofit.galacticrush.Main
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.MainGameScreen
import com.prophetsofprofit.galacticrush.networking.GalacticRushClient

/**
 * The screen that handles loading and initializing the galaxy
 * Should be called from host, as only host should actually be constructing galaxy
 */
class ClientLoadingScreen(game: Main) : LoadingScreen(game) {

    /**
     * Waits for the connection to receive the player
     */
    override fun load() {
        while (GalacticRushClient.player == null) {
            Thread.sleep(250)
        }
    }

    /**
     * Moves the game to the MainGameScreen
     */
    override fun onLoad() {
        this.game.screen = MainGameScreen(this.game, GalacticRushClient.player!!)
    }

}
