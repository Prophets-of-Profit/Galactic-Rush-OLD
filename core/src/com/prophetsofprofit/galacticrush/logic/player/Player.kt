package com.prophetsofprofit.galacticrush.logic.player

import com.prophetsofprofit.galacticrush.logic.Change
import com.prophetsofprofit.galacticrush.logic.Game

/**
 * The game's player
 * Handles all the functions attributed to a human player
 */
abstract class Player(val id: Int) {

    //What the player has done and will submit to the game once their turn is over
    var currentChanges: Change = Change(this.id)
    //The game that the player is in
    var currentGameState: Game? = null
    //The game state that is the state of the game before drone turns
    lateinit var oldGameState: Game
    //Convenience getters and setters for the player's game
    var game
        get() = this.currentGameState ?: oldGameState
        set(value) {
            if (value.droneTurnChanges.isEmpty()) {
                this.oldGameState = value
                currentGameState = null
            } else {
                this.currentGameState = value
            }
        }

    /**
     * The method that handles submitting the changes to the game: is different depending on the type of player
     */
    abstract fun submitChanges()

    /**
     * The method that handles receiving an updated game
     */
    abstract fun receiveNewGameState(newGame: Game)

}