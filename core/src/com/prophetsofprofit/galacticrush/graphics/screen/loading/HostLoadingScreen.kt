package com.prophetsofprofit.galacticrush.graphics.screen.loading

import com.prophetsofprofit.galacticrush.Main
import com.prophetsofprofit.galacticrush.Networker
import com.prophetsofprofit.galacticrush.graphics.screen.MainGameScreen
import com.prophetsofprofit.galacticrush.logic.Game
import com.prophetsofprofit.galacticrush.logic.player.LocalPlayer
import com.prophetsofprofit.galacticrush.logic.player.NetworkPlayer
import com.prophetsofprofit.galacticrush.logic.player.Player

/**
 * The screen that handles loading and initializing the game
 * Should be called from host, as only host should actually be constructing the game
 */
class HostLoadingScreen(game: Main, val players: Array<Player>) : LoadingScreen(game) {

    //The game that the host is constructing
    var mainGame: Game? = null

    /**
     * Constructs the galaxy
     */
    override fun load() {
        this.mainGame = Game(this.players.map { it.id }.toTypedArray(), 100)
        this.players.forEach {
            it.game = this.mainGame!!
            if (it is NetworkPlayer) {
                Networker.getServer().sendToTCP(it.connectionId, it)
            }
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
