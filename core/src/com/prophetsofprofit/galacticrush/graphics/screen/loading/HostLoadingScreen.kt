package com.prophetsofprofit.galacticrush.graphics.screen.loading

import com.prophetsofprofit.galacticrush.Main
import com.prophetsofprofit.galacticrush.graphics.screen.MainGameScreen
import com.prophetsofprofit.galacticrush.logic.Game
import com.prophetsofprofit.galacticrush.logic.player.Player

/**
 * The screen that handles loading and initializing the game
 * Should be called from host, as only host should actually be constructing the game
 */
class HostLoadingScreen(game: Main, val players: Array<Player>): LoadingScreen(game) {

    //The game that the host is constructing
    var mainGame: Game? = null

    /**
     * Constructs the galaxy
     */
    override fun load() {
        this.mainGame = Game(this.players, 100)
    }

    /**
     * Moves host to MainGameScreen
     */
    override fun onLoad() {
        this.game.screen = MainGameScreen(this.game, this.mainGame!!)
    }


}
