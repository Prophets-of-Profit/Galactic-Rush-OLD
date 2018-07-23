package com.prophetsofprofit.galacticrush.logic.facility

/**
 * Denotes the user's base locationId; destroy this to win the game!
 */
class HomeBase(ownerId: Int, locationId: Int) : Facility(ownerId, locationId, 100) {

    /**
     * Empty constructor for serialization
     */
    constructor() : this(-1, -1)

    /**
     * Displays the facility as a string
     */
    override fun toString(): String {
        return "Home Base (${this.ownerId})"
    }
}