package com.prophetsofprofit.galacticrush.logic.map

import com.badlogic.gdx.graphics.Texture
import com.prophetsofprofit.galacticrush.logic.base.Base
import com.prophetsofprofit.galacticrush.logic.drone.Drone

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
             val id: Int) {

    /**
     * Empty constructor for serialization
     */
    constructor() : this(-1f, -1f, -1f, -1)

    //Arbitrary attributes that determine drone behaviour: can be changed, but each start out as a random number between 0 and 1
    val attributes = PlanetAttribute.values().map { it to Math.random() }.toMap().toMutableMap()
    //All the drones on the planet
    val drones = mutableListOf<Drone>()
    //The base that is on the planet
    var base: Base? = null
    //The planet's image path
    val imagePath = "image/planets/planet${(Math.random() * 5).toInt()}.png"
    //The texture used for the planet TODO: figure out better way to get texture rather than creating a new one each time it is requested
    val image: Texture
        get() = Texture(this.imagePath)

}
