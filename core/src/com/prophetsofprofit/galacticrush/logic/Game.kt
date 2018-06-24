package com.prophetsofprofit.galacticrush.logic

import com.prophetsofprofit.galacticrush.logic.map.Galaxy
import com.prophetsofprofit.galacticrush.logic.player.Player
import java.io.Serializable

/**
 * The main game object
 * Handles attributes of the current game, and is serialized for networking
 */
class Game(val galaxy: Galaxy, val players: Array<Player>): Serializable {

    //The amount of turns that have passed since the game was created
    var turnsPlayed = 0

    /**
     * A method that collects changes, verifies their integrity, and then applies them to the game
     */
    fun collectChange(change: Change) {
        //TODO: make
    }

}