package com.prophetsofprofit.galacticrush.logic.map

import com.prophetsofprofit.galacticrush.logic.base.Base
import com.prophetsofprofit.galacticrush.logic.drone.Drone

/**
 * A class that represents a node in the graph that is the galaxy
 * Planets are the 'tiles' of the game
 * Planets can be traversed to and from depending on the edges of the graph, or paths in the galaxy
 * Planets have arbitrary attributes that are used by other entities in the game
 * Takes in its locationId in 2d space between 0 and 1
 */
class Planet(val id: Int) {

    /**
     * Empty constructor for serialization
     */
    constructor() : this(-1)

    //Planet x position from 0 to 1
    var x = Math.random().toFloat()
    //Planet y position from 0 to 1
    var y = Math.random().toFloat()
    //The radius of this circular planet
    val radius = 0.0003f + Math.random().toFloat() * 0.0003f
    //Arbitrary attributes that determine drone behaviour: can be changed, but each start out as a random number between 0 and 1
    val attributes = PlanetAttribute.values().map { it to Math.random() }.toMap().toMutableMap()
    //All the drones on the planet
    val drones = mutableListOf<Drone>()
    //The base that is on the planet
    var base: Base? = null
    //The planet's image path
    val imagePath = "image/planets/planet${(Math.random() * 5).toInt()}.png"

}
