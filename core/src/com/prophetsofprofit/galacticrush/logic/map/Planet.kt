package com.prophetsofprofit.galacticrush.logic.map

import com.badlogic.gdx.graphics.Color
import com.prophetsofprofit.galacticrush.logic.drone.Drone

/**
 * Names for arbitrary attributes
 */
enum class Attribute(private val displayString: String) {

    MASS("mass"),
    TEMPERATURE("temperature"),
    ATMOSPHERE("atmosphere density"),
    WATER("humidity"),
    SOLIDITY("solidity");

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
 * Takes in its location in 2d space between 0 and 1
 */
class Planet(var x: Float,
             var y: Float,
             val radius: Float,
             val color: Color = Color(Math.random().toFloat(), Math.random().toFloat(), Math.random().toFloat(), 1f)) {
    //Arbitrary attributes that determine drone behaviour: can be changed, but each start out as a random number between 0 and 1
    val attributes = Attribute.values().map { it to Math.random() }.toMap()
    val connectedHighways = mutableListOf<CosmicHighway>()
    val drones = mutableListOf<Drone>()

    /*
     * The planets that are reachable within one move from here
     */
    val connectedPlanets: List<Planet>
        get() {
            return List(connectedHighways.size, { if (connectedHighways[it].p0 === this) connectedHighways[it].p1 else connectedHighways[it].p0 })
        }

}
