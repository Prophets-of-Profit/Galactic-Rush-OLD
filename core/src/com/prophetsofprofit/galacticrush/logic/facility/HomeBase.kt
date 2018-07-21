package com.prophetsofprofit.galacticrush.logic.facility

/**
 * Denotes the user's base location; destroy this to win the game!
 */
class HomeBase(ownerId: Int) : Facility(ownerId) {

    /**
     * Empty constructor for serialization
     */
    constructor() : this(-1)

    /**
     * Displays the facility as a string
     */
    override fun toString(): String {
        return "Home Base (${this.ownerId})"
    }
}