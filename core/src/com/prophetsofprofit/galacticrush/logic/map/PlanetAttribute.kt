package com.prophetsofprofit.galacticrush.logic.map

import kotlin.math.pow

/**
 * Names for arbitrary attributes
 * stringValue converts a value from 0 to 1 into suitable units
 */
enum class PlanetAttribute(private val displayString: String, val stringValue: (Double) -> String) {

    MASS("Mass", { val string = "%e".format((10.0.pow(22) * Math.E.pow(9.21 * it))); string.slice(0 until 4) + string.slice(string.length - 5 until string.length) + " kg" }),
    TEMPERATURE("Surface Temperature", { "%.4f".format(50 + it * 700) + " K" }),
    ATMOSPHERE("Atmospheric Density", { "%.4f".format(10.0.pow(2 * it)) + " atm" }),
    WATER("Humidity", { "%.4f".format(it * 100) + "%" }),
    SOLIDITY("Solidity", { "%.4f".format(it * 100) + "%" });

    /**
     * Each attribute displays as it's displayString
     */
    override fun toString(): String {
        return displayString
    }
}