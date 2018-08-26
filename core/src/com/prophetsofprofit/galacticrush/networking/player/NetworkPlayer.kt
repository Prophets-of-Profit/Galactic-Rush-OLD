package com.prophetsofprofit.galacticrush.networking.player

import com.prophetsofprofit.galacticrush.logic.Game
import com.prophetsofprofit.galacticrush.logic.change.Change
import com.prophetsofprofit.galacticrush.networking.GalacticRushClient
import com.prophetsofprofit.galacticrush.networking.GalacticRushServer

/**
 * A player that plays on a different machine on the network
 * Can never be the host player, but will be initialized on host player's machine
 */
class NetworkPlayer(id: Int, val connectionId: Int) : Player(id) {

    /**
     * Empty constructor for serialization
     */
    constructor() : this(-1, -1)

    /**
     * A method that gets called form the clientside that sends a change object to the game
     * After sending change, listens for an updated game to set as this player's game
     */
    override fun submit(change: Change) {
        GalacticRushClient.sendTCP(change)
    }

    /**
     * A method that gets called from the serverside that sends an updated game to the client
     */
    override fun receiveNewGameState(newGame: Game) {
        this.game = newGame
        GalacticRushServer.sendToTCP(this.connectionId, newGame)
    }

}