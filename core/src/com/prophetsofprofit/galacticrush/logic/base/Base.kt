package com.prophetsofprofit.galacticrush.logic.base

import com.prophetsofprofit.galacticrush.logic.map.Planet

/**
 * A class that represents a player's base
 * Each planet can only contain one player's base
 * Bases can have different facilities with different capabilities
 */
class Base(val ownerId: Int, val locationId: Int, facilities: Array<Facility>) {

    //A map of each contained facility to its remaining health
    val facilityHealths = mutableMapOf<Facility, Int>()

    /**
     * Adds each of the given facilities
     */
    init {
        facilities.forEach { this.addFacility(it) }
    }

    /**
     * Empty constructor for serialization
     */
    constructor(): this(-1, -1, arrayOf())

    //A convenience getter for how much total health the base has
    val health: Int
        get() = this.facilityHealths.values.sum()

    /**
     * Gets the locationId of the base among a list of planets, or null if it does not exist
     */
    fun getLocationAmong(planets: Array<Planet>): Planet? {
        return planets.firstOrNull { it.id == this.locationId }
    }

    /**
     * Adds a facility to the base
     */
    fun addFacility(f: Facility) {
        if (f in this.facilityHealths) {
            return
        }
        this.facilityHealths[f] = f.maxHealth
    }

    /**
     * Spreads damage to all contained facilities evenly and removes dead facilities
     */
    fun takeDamage(damage: Int) {
        val damageToAll = damage / this.facilityHealths.size
        val numToReceieveExtra = damage % this.facilityHealths.size
        this.facilityHealths.forEach { it.value - damageToAll }
        this.facilityHealths.keys.toMutableList().subList(0, numToReceieveExtra).forEach { this.facilityHealths[it] = this.facilityHealths[it]!! - 1 }
        this.facilityHealths.filter { it.value <= 0 }.forEach { facility, _ -> this.facilityHealths.remove(facility) }
    }

}