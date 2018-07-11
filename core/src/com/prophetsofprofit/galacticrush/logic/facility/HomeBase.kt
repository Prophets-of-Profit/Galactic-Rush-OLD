package com.prophetsofprofit.galacticrush.logic.facility

/**
 * Denotes the user's base location; destroy this to win the game!
 */
class HomeBase(ownerID: Int): Facility(ownerID) {

    /**
     * Empty constructor for serialization
     */
    constructor(): this(-1)

}