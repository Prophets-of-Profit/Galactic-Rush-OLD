package com.prophetsofprofit.galacticrush.logic.map

/**
 * A class that is basically the map that the game is played on
 * Contains a bunch of planets which are essentially the game 'tiles'
 * Planets are connected as a graph rather than sequentially
 */
class Galaxy(numPlanets: Int) {

    //The planets that are in the galaxy: serve as 'tiles' of the game, but are connected as a graph
    val planets = Array(numPlanets, { Planet(Math.random(), Math.random()) })

    /**
     * Galaxy constructor generates all the planets and terrain and values and such
     * While this isn't a world, a good term for this would be 'worldgen'
     */
    init {
        //TODO: make
    }

}