package com.prophetsofprofit.galacticrush.logic.base

/**
 * A list of facilities or upgrades that a base can have
 */
enum class Facility(val label: String, val maxHealth: Int, val cost: Int) {
    HOME_BASE("Home Base", 1000, Int.MAX_VALUE),
    BASE("Additional Base", 250, 750),
    CONSTRUCTION("Construction Facility", 50, 200),
    PROGRAMMING("Programming Facility", 50, 350)
}