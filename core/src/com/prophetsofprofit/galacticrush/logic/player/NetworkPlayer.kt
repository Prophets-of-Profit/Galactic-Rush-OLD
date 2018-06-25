package com.prophetsofprofit.galacticrush.logic.player

import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.prophetsofprofit.galacticrush.Networker
import com.prophetsofprofit.galacticrush.logic.Game

/**
 * A player that plays on a different machine on the network
 * Can never be the host player, but will be initialized on host player's machine
 */
class NetworkPlayer(id: Int, val connectionId: Int): Player(id) {

    /**
     * A method that gets called form the clientside that sends a change object to the game
     * After sending change, listens for an updated game to set as this player's game
     */
    override fun submitChanges() {
        Networker.getClient().sendTCP(this.currentChanges)
        //TODO: just have listener on permanently instead of continuously adding and removing it
        Networker.getClient().addListener(object: Listener() {
            override fun received(connection: Connection?, obj: Any?) {
                if (connection?.id == connectionId && obj is Game) {
                    game = obj
                }
                Networker.getClient().removeListener(this)
            }
        })
    }

    /**
     * A method that gets called from the serverside that sends an updated game to the client
     */
    override fun receiveNewGameState(newGame: Game) {
        this.game = newGame
        Networker.getServer().sendToTCP(this.connectionId, this)
    }

}