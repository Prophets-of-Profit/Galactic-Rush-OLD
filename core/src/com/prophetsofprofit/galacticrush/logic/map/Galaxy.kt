package com.prophetsofprofit.galacticrush.logic.map

/**
 * A class that is basically the map that the game is played on
 * Contains a bunch of planets which are essentially the game 'tiles'
 * Planets are connected as a graph rather than sequentially
 */
class Galaxy(numPlanets: Int) {

    //The planets that are in the galaxy: serve as 'tiles' of the game, but are connected as a graph
    val planets = mutableListOf<Planet>()

    /**
     * Galaxy constructor generates all the planets and terrain and values and such
     * While this isn't a world, a good term for this would be 'worldgen'
     */
    init {
        fun cumSum(array: Array<Double>): Array<Double> {
            val sums = array.clone()
            for(i in 1 until array.size) {
                sums[i] += sums[i - 1]
            }
            return sums
        }
        fun ManhattanDistance(x1: Int, y1: Int, x2: Int, y2:Int): Int {
            return Math.abs(x2 - x1 + y2 - y1)
        }
        val worldSize = 4 * numPlanets
        val probabilities = Array(worldSize * worldSize, { 1.0 })
        for(i in 0 until numPlanets) {
            val total = probabilities.sum()
            val number = Math.random() * total
            val cumulativeProbabilities = cumSum(probabilities)
            val index = cumulativeProbabilities.indexOfFirst { it > number }
            probabilities[index] = 0.0
            for(j in 0 until probabilities.size) {
                probabilities[j] = if(probabilities[j] != 0.0) probabilities[j] + ManhattanDistance(j / worldSize, j % worldSize, index / worldSize, index % worldSize) else 0.0
            }
        }
        (0 until probabilities.size)
                .filter { probabilities[it] == 0.0 }
                .mapTo(planets) { Planet((it / worldSize).toDouble() / worldSize + 0.5 / worldSize, (it % worldSize).toDouble() / worldSize + 0.5 / worldSize) }

    }

}