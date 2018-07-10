package com.prophetsofprofit.galacticrush.logic

import com.prophetsofprofit.galacticrush.logic.drone.Drone
import com.prophetsofprofit.galacticrush.logic.map.Galaxy
import com.prophetsofprofit.galacticrush.logic.player.Player
import java.io.Serializable

/**
 * The main game object
 * Handles attributes of the current game, and is serialized for networking
 */
class Game(val players: Array<Player>, galaxySize: Int) : Serializable {

    //The board or map on which this game is played
    val galaxy = Galaxy(galaxySize, players.map {it -> it.id})
    //The amount of turns that have passed since the game was created
    var turnsPlayed = 0
    //The players who need to submit their changes for the drones to commence
    val waitingOn = mutableListOf<Int>()
    //The drones that currently exist in the game
    //Should be ordered in order of creation
    val drones = mutableListOf<Drone>()

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
     * Performs one action per drone for all drones that can perform an action; won't be callable until game is ready
     */
    fun doDroneTurn() {
        //If waiting on players don't do anything
        if (waitingOn.isNotEmpty()) {
            return
        }
        //Complete the actions of all the drones who can do actions in the queue
        this.drones.filterNot { it.queueFinished }.forEach { it.completeAction() }
        //If all the drones are now finished, wait for players and reset drones
        if (this.drones.all { it.queueFinished }) {
            this.drones.forEach { it.resetQueue() }
            players.mapTo(waitingOn) { it.id }
        }
    }

}
