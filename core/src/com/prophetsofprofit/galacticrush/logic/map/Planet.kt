package com.prophetsofprofit.galacticrush.logic.map

import com.badlogic.gdx.graphics.Color
import java.util.Random

/**
 * A class that represents a node in the graph that is the galaxy
 * Planets are the 'tiles' of the game
 * Planets can be traversed to and from depending on the edges of the graph, or paths in the galaxy
 * Planets have arbitrary attributes that are used by other entities in the game
 * Takes in its location in 2d space
 */
class Planet(val x: Double,
             val y: Double,
             val radius: Float,
             val color: Color = Color(Math.random().toFloat(), Math.random().toFloat(), Math.random().toFloat(), 1f))