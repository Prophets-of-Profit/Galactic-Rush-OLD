package com.prophetsofprofit.galacticrush.logic.map

import com.badlogic.gdx.graphics.Color

/**
 * Names for arbitrary attributes
 */
enum class Attributes(private val displayString: String) {

    MASS("mass"),
    TEMPERATURE("temperature"),
    OXYGEN("oxygen");

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
 * Takes in its location in 2d space
 */
class Planet(var x: Float,
             var y: Float,
             val radius: Float,
             val color: Color = Color(Math.random().toFloat(), Math.random().toFloat(), Math.random().toFloat(), 1f)) {
    //Arbitrary attributes that determine drone behaviour: can be changed, but each start out as a random number between 0 and 1
    val attributes = Attributes.values().map { it to Math.random() }.toMap()
}