package com.prophetsofprofit.galacticrush.logic.facility

import com.prophetsofprofit.galacticrush.logic.map.Planet

/**
 * A base that exists on a planet
 * Marks planet ownership
 */
abstract class Facility(val ownerId: Int) {

    //Where the facility is; TODO: get location based on ownerID
    var location: Planet = Planet(-1.0f, -1.0f, -1.0f)

}
