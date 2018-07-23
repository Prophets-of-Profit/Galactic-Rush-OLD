package com.prophetsofprofit.galacticrush.logic.facility

import com.prophetsofprofit.galacticrush.logic.map.Planet

/**
 * A base that exists on a planet
 * Marks planet ownership
 */
abstract class Facility(val ownerId: Int, val locationId:Int, var health: Int) {

    //Where the facility is
    var location: Planet = Planet(-1.0f, -1.0f, -1.0f, -1)
    //All derived classes must define a toString method for the UI display
    abstract override fun toString(): String

    /**
     * Gets the locationId of the drone among a list of planets, or null if it does not exist
     */
    fun getLocationAmong(planets: Array<Planet>): Planet? {
        return planets.firstOrNull { it.id == this.locationId }
    }

}
