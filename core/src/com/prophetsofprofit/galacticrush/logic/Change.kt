package com.prophetsofprofit.galacticrush.logic

import com.prophetsofprofit.galacticrush.logic.drone.Drone

/**
 * A class that represents a change that a player wants to make to the game
 * Every player directly modifies changes and then sends the changes to the master copy of the game
 */
class Change(val ownerId: Int) {

    /**
     * Empty constructor for serialization
     */
    constructor() : this(-1)

    //What the player has changed in regards to drones
    val changedDrones = mutableListOf<Drone>()

}