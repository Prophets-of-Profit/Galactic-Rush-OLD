package com.prophetsofprofit.galacticrush.logic.base

import com.prophetsofprofit.galacticrush.defaultBaseNames

/**
 * A class that represents a player's base
 * Each planet can only contain one player's base
 * Bases can have different facilities with different capabilities
 */
class Base(val ownerId: Int, val locationId: Int, facilities: Array<Facility>) {

    //A map of each contained facility to its remaining health
    val facilityHealths = mutableMapOf<Facility, Int>()
    //The name of the base
    var name = defaultBaseNames.toMutableList().shuffled().first()

    /**
     * Adds each of the given facilities
     */
    init {
        facilities.forEach { this.addFacility(it) }
    }

    /**
     * Empty constructor for serialization
     */
    constructor() : this(-1, -1, arrayOf())

    //A convenience getter for how much total health the base has
    val health: Int
        get() = this.facilityHealths.values.sum()

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
     * Damage prioritizes facilities that aren't BASE or HOME_BASE
     * BASE and HOME_BASE should only get damaged after all the other facilities are destroyed
     */
    fun takeDamage(damage: Int) {
        val extraFacilities = (this.facilityHealths.keys - setOf(Facility.HOME_BASE, Facility.BASE)).toList()
        val extraFacilitiesHealth = extraFacilities.sumBy { this.facilityHealths[it]!! }
        if (damage > extraFacilitiesHealth) {
            extraFacilities.forEach { this.facilityHealths.remove(it) }
            this.facilityHealths.forEach { facility, health -> this.facilityHealths[facility] = health - damage + extraFacilitiesHealth }
        } else {
            extraFacilities.forEach { this.facilityHealths[it] = this.facilityHealths[it]!! - damage / extraFacilities.size }
            for (i in 0..damage % extraFacilities.size) {
                this.facilityHealths[extraFacilities[i]] = this.facilityHealths[extraFacilities[i]]!! - 1
            }
        }
        this.facilityHealths.filter { it.value <= 0 }.keys.forEach { this.facilityHealths.remove(it) }

    }

    /**
     * Gets the string representation of the base
     */
    override fun toString(): String {
        return "${this.name} (${this.ownerId})"
    }

}