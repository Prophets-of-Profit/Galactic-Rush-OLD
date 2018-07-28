package com.prophetsofprofit.galacticrush.logic.player

import com.prophetsofprofit.galacticrush.logic.Change
import com.prophetsofprofit.galacticrush.logic.DroneTurnChange
import com.prophetsofprofit.galacticrush.logic.Game

/**
 * The game's player
 * Handles all the functions attributed to a human player
 */
abstract class Player(val id: Int) {

    //What the player has done and will submit to the game once their turn is over
    var currentChanges: Change = Change(this.id)
    //The game that the player is in
    lateinit var game: Game

    /**
     * The method that handles submitting the changes to the game: is different depending on the type of player
     */
    abstract fun submitChanges()

    /**
     * The method that handles receiving an updated game
     */
    abstract fun receiveNewGameState(newGame: Game)

    /**
     * The method that handles receiving changes that occur during the drone turns
     */
    abstract fun receiveDroneTurnChanges(changes: MutableList<DroneTurnChange>)

}