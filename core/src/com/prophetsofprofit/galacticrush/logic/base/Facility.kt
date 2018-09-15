package com.prophetsofprofit.galacticrush.logic.base

/**
 * A list of facilities or upgrades that a base can have
 */
enum class Facility(val label: String, val maxHealth: Int, val cost: Int) {
    HOME_BASE("Home Base", 100, Int.MAX_VALUE),
    CONSTRUCTION("Construction Facility", 50, 500),
    PROGRAMMING("Programming Facility", 50, 500)
}