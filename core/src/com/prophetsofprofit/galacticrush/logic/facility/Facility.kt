package com.prophetsofprofit.galacticrush.logic.facility

import com.prophetsofprofit.galacticrush.logic.map.Planet

/**
 * A base that exists on a planet
 * Marks planet ownership
 */
abstract class Facility(val ownerId: Int) {

    //Where the facility is
    var location: Planet = Planet(-1.0f, -1.0f, -1.0f)
    //All derived classes must define a toString method for the UI display
    abstract override fun toString(): String

}
