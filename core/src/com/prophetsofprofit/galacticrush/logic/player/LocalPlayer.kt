package com.prophetsofprofit.galacticrush.logic.player

import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.prophetsofprofit.galacticrush.Networker
import com.prophetsofprofit.galacticrush.kryo
import com.prophetsofprofit.galacticrush.logic.Change
import com.prophetsofprofit.galacticrush.logic.DroneTurnChange
import com.prophetsofprofit.galacticrush.logic.Game

/**
 * The class that represents a player that is on the host machine: a local player
 * This type of player doesn't need to do any networking or anything because it can directly communicate with the main game
 */
class LocalPlayer(id: Int) : Player(id) {

    lateinit var hostedGame: Game

    /**
     * Empty constructor for serialization
     */
    constructor() : this(-1)

    /**
     * When a LocalPlayer is made, networking logic is initialized to receive changes and send the game
     */
    init {
        //TODO: below line is for dev version: remove on release
        if (Networker.isClient == false) {
            Networker.getServer().addListener(object : Listener() {
                override fun received(connection: Connection?, obj: Any?) {
                    if (obj is Change) {
                        hostedGame.collectChange(obj)
                    }
                }
            })
            Thread {
                //TODO: exit condition
                while (true) {
                    try {
                        if (!this.hostedGame.gameChanged) {
                            Thread.sleep(50)
                            continue
                        }
                        this.hostedGame.gameChanged = false
                        Networker.getServer().sendToAllTCP(this.hostedGame)
                        this.receiveNewGameState(this.hostedGame)
                        while (this.hostedGame.drones.any { !it.queueFinished }) {
                            this.hostedGame.doDroneTurn()
                        }
                        Networker.getServer().sendToAllTCP(this.hostedGame.droneTurnChanges)
                        this.receiveDroneTurnChanges(this.hostedGame.droneTurnChanges)
                    } catch (ignored: Exception) {
                    }
                }
            }.start()
        }
    }

    /**
     * The local player directly gives the local game the changes to submit
     */
    override fun submitChanges() {
        this.hostedGame.collectChange(this.currentChanges)
        this.currentChanges = Change(this.id)
    }

    /**
     * The local player directly just sets its own game to the new game
     * It doesn't even really need to set the game to a new game because the local game shouldn't be a different object
     */
    override fun receiveNewGameState(newGame: Game) {
        this.game = kryo.copy(newGame)
    }

    /**
     * The game stores drone changes
     */
    override fun receiveDroneTurnChanges(changes: MutableList<DroneTurnChange>) {
        this.game.droneTurnChanges = changes
    }

}