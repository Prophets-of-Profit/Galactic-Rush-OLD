package com.prophetsofprofit.galacticrush.logic.change

import com.prophetsofprofit.galacticrush.logic.base.Base
import com.prophetsofprofit.galacticrush.logic.drone.Drone
import com.prophetsofprofit.galacticrush.logic.map.Planet

/**
 * A class that represents a change in the game or what has been changed
 */
open class Change {

    //A list of any changed planets
    val changedPlanets = mutableListOf<Planet>()
    //A list of changed drones
    val changedDrones = mutableListOf<Drone>()
    //A list of changed bases
    val changedBases = mutableListOf<Base>()

}