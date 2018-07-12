package com.prophetsofprofit.galacticrush.logic.player

import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.prophetsofprofit.galacticrush.Networker
import com.prophetsofprofit.galacticrush.logic.Change
import com.prophetsofprofit.galacticrush.logic.Game

/**
 * The class that represents a player that is on the host machine: a local player
 * This type of player doesn't need to do any networking or anything because it can directly communicate with the main game
 */
class LocalPlayer(id: Int) : Player(id) {

    /**
     * Empty constructor for serialization
     */
    constructor(): this(-1)

    /**
     * When a LocalPlayer is made, networking logic is initialized to receive changes and send the game
     */
    init {
        Networker.getServer().addListener(object: Listener() {
            override fun received(connection: Connection?, obj: Any?) {
                if (obj is Change) {
                    game.collectChange(obj)
                }
            }
        })
        Thread {
            //TODO: exit condition
            while (true) {
                if (!this.game.gameChanged) {
                    Thread.sleep(50)
                    continue
                }
                Networker.getServer().sendToAllTCP(this.game)
            }
        }.start()
    }

    /**
     * The local player directly gives the local game the changes to submit
     */
    override fun submitChanges() {
        this.game.collectChange(this.currentChanges ?: Change(this.id))
    }

    /**
     * The local player directly just sets its own game to the new game
     * It doesn't even really need to set the game to a new game because the local game shouldn't be a different object
     */
    override fun receiveNewGameState(newGame: Game) {
        this.game = newGame
    }

}