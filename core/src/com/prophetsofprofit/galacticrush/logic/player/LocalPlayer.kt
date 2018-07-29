package com.prophetsofprofit.galacticrush.logic.player

import com.prophetsofprofit.galacticrush.kryo
import com.prophetsofprofit.galacticrush.logic.Change
import com.prophetsofprofit.galacticrush.logic.Game
import com.prophetsofprofit.galacticrush.networking.GalacticRushServer

/**
 * The class that represents a player that is on the host machine: a local player
 * This type of player doesn't need to do any networking or anything because it can directly communicate with the main game
 */
class LocalPlayer(id: Int) : Player(id) {

    /**
     * Empty constructor for serialization
     */
    constructor() : this(-1)

    /**
     * The local player directly gives the local game the changes to submit
     */
    override fun submitChanges() {
        GalacticRushServer.hostedGame!!.collectChange(this.currentChanges)
        this.currentChanges = Change(this.id)
    }

    /**
     * The local player directly just sets its own game to the new game
     * It doesn't even really need to set the game to a new game because the local game shouldn't be a different object
     */
    override fun receiveNewGameState(newGame: Game) {
        this.game = kryo.copy(newGame)
    }

}