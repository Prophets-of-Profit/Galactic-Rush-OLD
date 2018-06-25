package com.prophetsofprofit.galacticrush.logic

import com.prophetsofprofit.galacticrush.logic.map.Galaxy
import com.prophetsofprofit.galacticrush.logic.player.Player
import java.io.Serializable

/**
 * The main game object
 * Handles attributes of the current game, and is serialized for networking
 */
class Game(val players: Array<Player>, galaxySize: Int): Serializable {

    //The board or map on which this game is played
    val galaxy = Galaxy(galaxySize)
    //The amount of turns that have passed since the game was created
    var turnsPlayed = 0

    /**
     * Initializes the game by setting the game of all the players to this one
     */
    init {
        this.players.forEach { it.receiveNewGameState(this) }
    }

    /**
     * A method that collects changes, verifies their integrity, and then applies them to the game
     */
    fun collectChange(change: Change) {
        //TODO: make; instead of immediately replying with new gamestate, wait for all changes to happen and then send game states
        this.players.first { it.id == change.ownerId }.receiveNewGameState(this)
    }

}