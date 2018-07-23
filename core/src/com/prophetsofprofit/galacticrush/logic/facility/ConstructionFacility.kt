package com.prophetsofprofit.galacticrush.logic.facility

/**
 * Allows the user to construct new drones here!
 */
class ConstructionFacility(ownerId: Int, locationId: Int) : Facility(ownerId, locationId, 20) {

    /**
     * Empty constructor for serialization
     */
    constructor() : this(-1, -1)

    /**
     * Displays the type of facility as a string
     */
    override fun toString(): String {
        return "Construction Facility"
    }
}