package com.prophetsofprofit.galacticrush.logic

/**
 * The game's player
 * Handles all the functions attributed to a human player
 */
abstract class Player(val id: Int) {

    //What the player has done and will submit to the game once their turn is over
    var currentChanges: Change? = null

    /**
     * The method that handles submitting the changes to the game: is different depending on the type of player
     */
    abstract fun submitChanges()

}