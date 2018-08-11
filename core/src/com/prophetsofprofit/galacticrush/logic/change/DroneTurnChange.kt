package com.prophetsofprofit.galacticrush.logic.change

import com.prophetsofprofit.galacticrush.logic.drone.Drone
import com.prophetsofprofit.galacticrush.logic.map.Planet

/**
 * Stores changes to one object caused by a doDroneTurn call
 */
class DroneTurnChange(val changedDrones: MutableList<Drone>, val changedPlanets: MutableList<Planet>) : Change() {

    /**
     * Empty constructor for serialization
     */
    constructor() : this(mutableListOf(), mutableListOf())

}