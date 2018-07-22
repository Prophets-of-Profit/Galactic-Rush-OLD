package com.prophetsofprofit.galacticrush.logic.map

import com.badlogic.gdx.graphics.Color
import com.prophetsofprofit.galacticrush.logic.drone.Drone
import com.prophetsofprofit.galacticrush.logic.facility.Facility
import kotlin.math.pow

/**
 * Names for arbitrary attributes
 * stringValue converts a value from 0 to 1 into suitable units
 */
enum class Attribute(private val displayString: String, val stringValue: (Double) -> String) {

    MASS("Mass", { val string = "%e".format((10.0.pow(22) * Math.E.pow(9.21 * it))); string.slice(0 until 4) + string.slice(string.length - 5 until string.length) + " kg" }),
    TEMPERATURE("Surface Temperature", { "%.4f".format(50 + it * 700) + " K" }),
    ATMOSPHERE("Atmospheric Density", { "%.4f".format(10.0.pow(2 * it)) + " atm" }),
    WATER("Humidity", { "%.4f".format(it * 100) + "%" } ),
    SOLIDITY("Solidity", { "%.4f".format(it * 100) + "%" });

    /**
     * Each attribute displays as it's displayString
     */
    override fun toString(): String {
        return displayString
    }
}

/**
 * A class that represents a node in the graph that is the galaxy
 * Planets are the 'tiles' of the game
 * Planets can be traversed to and from depending on the edges of the graph, or paths in the galaxy
 * Planets have arbitrary attributes that are used by other entities in the game
 * Takes in its locationId in 2d space between 0 and 1
 */
class Planet(var x: Float,
             var y: Float,
             val radius: Float,
             val id: Int,
             val color: Color = Color(Math.random().toFloat(), Math.random().toFloat(), Math.random().toFloat(), 1f)) {

    /**
     * Empty constructor for serialization
     */
    constructor() : this(-1f, -1f, -1f, -1)

    //Arbitrary attributes that determine drone behaviour: can be changed, but each start out as a random number between 0 and 1
    val attributes = Attribute.values().map { it to Math.random() }.toMap().toMutableMap()
    //All the drones on the planet
    val drones = mutableListOf<Drone>()
    //All the facilities on the planet
    val facilities = mutableListOf<Facility>()

}
