package com.prophetsofprofit.galacticrush.logic.facility

/**
 * Allows the user to modify drone instructions here!
 */
class ProgrammingFacility(ownerId: Int, locationId: Int) : Facility(ownerId, 20) {

    /**
     * Empty constructor for serialization
     */
    constructor() : this(-1, -1)

    /**
     * Displays the type of facility as a string
     */
    override fun toString(): String {
        return "Programming Facility"
    }
}
