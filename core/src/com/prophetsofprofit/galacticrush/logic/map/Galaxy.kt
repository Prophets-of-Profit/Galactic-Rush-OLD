package com.prophetsofprofit.galacticrush.logic.map

/**
 * A class that is basically the map that the game is played on
 * Contains a bunch of planets which are essentially the game 'tiles'
 * Planets are connected as a graph rather than sequentially
 */
class Galaxy(numPlanets: Int) {

    //The planets that are in the galaxy: serve as 'tiles' of the game, but are connected as a graph
    val planets = mutableListOf<Planet>()
    //The cosmic highways that are in the galaxy: serve as the 'paths' or 'connections' of the game
    val highways = mutableListOf<CosmicHighway>()
    //TODO: documentation
    val worldSize = 4 * numPlanets

    /**
     * Galaxy constructor generates all the planets and terrain and values and such
     * While this isn't a world, a good term for this would be 'worldgen'
     */
    init {
        val probabilities = Array(worldSize * worldSize, { 1.0 })
        for (i in 0 until numPlanets) {
            val chosenCumulative = Math.random() * probabilities.sum()
            val cumulativeProbabilities = probabilities.clone()
            for (j in 1 until probabilities.size) {
                cumulativeProbabilities[j] += cumulativeProbabilities[j - 1]
            }
            val chosenLocation = cumulativeProbabilities.indexOfFirst { it > chosenCumulative }
            probabilities[chosenLocation] = 0.0
            for (j in 0 until probabilities.size) {
                probabilities[j] = if (probabilities[j] != 0.0) probabilities[j] + Math.abs(j / worldSize - j % worldSize + chosenLocation / worldSize - chosenLocation % worldSize) else 0.0
            }
        }
        (0 until probabilities.size)
                .filter { probabilities[it] == 0.0 }
                .mapTo(planets) { Planet((it / worldSize).toDouble() / worldSize + 0.5 / worldSize, (it % worldSize).toDouble() / worldSize + 0.5 / worldSize, ((0.15 + Math.random() * 0.35) / worldSize).toFloat()) }
        //TODO: temporary; makes highways for each planet to a random other planet
        this.planets.mapTo(highways) { CosmicHighway(it, planets.shuffled().first()) }
    }

}