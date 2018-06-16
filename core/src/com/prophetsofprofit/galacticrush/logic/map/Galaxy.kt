package com.prophetsofprofit.galacticrush.logic.map

import java.lang.Math.pow
import java.lang.Math.sqrt
import kotlin.math.pow

/**
 * A class that is basically the map that the game is played on
 * Contains a bunch of planets which are essentially the game 'tiles'
 * Planets are connected as a graph rather than sequentially
 */
class Galaxy(numPlanets: Int) {

    //The planets that are in the galaxy: serve as 'tiles' of the game, but are connected as a graph
    lateinit var planets: Array<Planet>

    /**
     * Galaxy constructor generates all the planets and terrain and values and such
     * While this isn't a world, a good term for this would be 'worldgen'
     */
    init {
        var xCoords = MutableList(0, {0})
        var yCoords = MutableList(0, {0})
        fun distance(x1: Int, y1: Int, x2: Int, y2: Int): Double {
            return sqrt((((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)).toDouble()))
        }
        fun closeToAll(x: Int, y: Int, xCoords: List<Int>, yCoords: List<Int>, worldSize: Int): Boolean {
            assert(xCoords.size == yCoords.size, { "Coordinate length mismatch" })
            if(xCoords.isEmpty()) return false
            return (0 until xCoords.size).any { distance(x, y, xCoords[it], yCoords[it]) < worldSize / xCoords.size }
        }
        //Grid size is 4 * numPlanets both ways
        val worldSize = 16 * numPlanets

        for(i in 0..numPlanets) {
            var xCoord = (Math.random() * worldSize).toInt()
            var yCoord = (Math.random() * worldSize).toInt()
            while(closeToAll(xCoord, yCoord, xCoords, yCoords, worldSize)) {
                xCoord = (Math.random() * worldSize).toInt()
                yCoord = (Math.random() * worldSize).toInt()
            }
            xCoords.add(xCoord)
            yCoords.add(yCoord)
        }
        planets = Array(numPlanets, { Planet(xCoords[it].toDouble() / worldSize, yCoords[it].toDouble() / worldSize) })
    }

}