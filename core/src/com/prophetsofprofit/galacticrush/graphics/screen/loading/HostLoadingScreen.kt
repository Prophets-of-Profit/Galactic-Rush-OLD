package com.prophetsofprofit.galacticrush.graphics.screen.loading

import com.prophetsofprofit.galacticrush.Main
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.MainGameScreen
import com.prophetsofprofit.galacticrush.logic.GameCreationOptions
import com.prophetsofprofit.galacticrush.networking.GalacticRushServer
import com.prophetsofprofit.galacticrush.networking.player.LocalPlayer
import com.prophetsofprofit.galacticrush.networking.player.Player

/**
 * The screen that handles loading and initializing the game
 * Should be called from host, as only host should actually be constructing the game
 */
class HostLoadingScreen(game: Main, val players: Array<Player>, var options: GameCreationOptions) : LoadingScreen(game) {

    /**
     * Constructs the galaxy
     */
    override fun load() {
        Thread.sleep(50) //Necessary to ensure that this.players isn't null
        Thread {
            GalacticRushServer.runGame(this.players, this.options)
        }.start()
        while (GalacticRushServer.hostedGame == null) {
            Thread.sleep(50)
        }
    }

    /**
     * Moves host to MainGameScreen
     */
    override fun onLoad() {
        //Assuming that there is only one local player
        this.game.screen = MainGameScreen(this.game, this.players.first { it is LocalPlayer })
    }

}
