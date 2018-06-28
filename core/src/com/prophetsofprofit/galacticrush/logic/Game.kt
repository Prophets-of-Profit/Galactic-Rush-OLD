package com.prophetsofprofit.galacticrush.logic

import com.prophetsofprofit.galacticrush.logic.map.Galaxy
import com.prophetsofprofit.galacticrush.logic.player.Player
import java.io.Serializable

/**
 * The main game object
 * Handles attributes of the current game, and is serialized for networking
 */
class Game(val players: Array<Player>, galaxySize: Int) : Serializable {

    //The board or map on which this game is played
    val galaxy = Galaxy(galaxySize)
    //The amount of turns that have passed since the game was created
    var turnsPlayed = 0
    //The players who need to submit their changes for the drones to commence
    val waitingOn = mutableListOf<Int>()

    /**
     * Initializes the game by setting the game of all the players to this one
     */
    init {
        this.players.forEach { it.receiveNewGameState(this) }
        players.mapTo(waitingOn) { it.id }
    }

    /**
     * A method that collects changes, verifies their integrity, and then applies them to the game
     */
    fun collectChange(change: Change) {
        //TODO: make; instead of immediately replying with new gamestate, wait for all changes to happen and then send game states
        this.players.first { it.id == change.ownerId }.receiveNewGameState(this)
        if (waitingOn.contains(change.ownerId)) {
            waitingOn.remove(change.ownerId)
        }
        //TODO: call doDroneTurn here?
    }

    /**
     * The method that starts performing all the drone calculations for their turn
     */
    fun doDroneTurn() {
        if (waitingOn.size > 0) {
            return
        }
        players.mapTo(waitingOn) { it.id }
    }

}