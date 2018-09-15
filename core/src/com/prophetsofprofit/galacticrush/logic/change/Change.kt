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

    /**
     * Adds a planet to the list of changed planets
     */
    fun add(planet: Planet) {
        if (changedPlanets.contains(planet)) {
            return
        }
        changedPlanets.add(planet)
    }

    /**
     * Adds a base to the list of changed planets
     */
    fun add(base: Base) {
        if (changedBases.contains(base)) {
            return
        }
        changedBases.add(base)
    }

    /**
     * Adds a planet to the list of changed planets
     */
    fun add(drone: Drone) {
        if (changedDrones.contains(drone)) {
            return
        }
        changedDrones.add(drone)
    }

    /**
     * Gives the change a pretty output string
     */
    override fun toString(): String {
        return "CHANGE:\nPlanets: ${this.changedPlanets}\nDrones: ${this.changedDrones}\nBases: ${this.changedBases}"
    }

}