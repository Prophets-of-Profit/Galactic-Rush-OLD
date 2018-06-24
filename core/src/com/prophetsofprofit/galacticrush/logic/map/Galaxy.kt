package com.prophetsofprofit.galacticrush.logic.map

import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Vector2
import kotlin.math.*

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

    /**
     * Galaxy constructor generates all the planets and terrain and values and such
     * Can make use of one of several implemented construction methods
     */
    init {
        distanceConstructor(numPlanets)
    }

    /**
     * Builds the galaxy by branching off planets from others that already exist
     * but have not yet been branched at a random distance and angle away
     */
    private fun randomBranchConstructor(numPlanets: Int) {
        //Begin with one starting planet
        var planetsCreated = 1
        //Shows the extreme values of planet locations; Order: max x, min x, max y, min y
        val extrema = Array<Float>(4, { 0f })
        //Begin with one starting planet
        planets.add(Planet(0f, 0f, (0.015 + Math.random() * 0.035).toFloat() / numPlanets))
        //A temporary list of planets which contains all planets from which no branches have already been generated
        val tempPlanets = mutableListOf<Planet>()
        tempPlanets.add(planets[0])
        //Until there exist the desired number of planets,
        while (planetsCreated < numPlanets) {
            //Choose a random planet from which to branch
            val originPlanet = tempPlanets.shuffled().first()
            //Get the number of iterations from a random generation (between two and four)
            var i = (Math.random() * 3).toInt() + 1
            while (i > 0) {
                //Creates a planet at a random distance and angle from the origin
                val angle = PI * 2 * Math.random()
                val distance = 0.1 + 0.5 * Math.random()
                val planet = Planet((originPlanet.x + distance * cos(angle)).toFloat(), (originPlanet.y + distance * sin(angle)).toFloat(), (0.015 + Math.random() * 0.035).toFloat() / numPlanets)
                //Check if a highway about to be created intersects any others
                if(!highways.any {
                            doSegmentsIntersect(planet.x, planet.y, originPlanet.x, originPlanet.y, it.p0.x, it.p0.y, it.p1.x, it.p1.y) || //Highways crosses existing highway
                                    (it.p0 == planet && it.p1 == originPlanet) || (it.p0 == originPlanet && it.p1 == planet) //Highway already exists but with p0 and p1 switched around
                        }) {
                    //If there are no intersections, add a planet and a highway
                    highways.add(CosmicHighway(originPlanet, planet))
                    planets.add(planet)
                    tempPlanets.add(planet)
                    planetsCreated++
                    //Update extrema if necessary
                    if (planet.x > extrema[0]) extrema[0] = planet.x
                    if (planet.x < extrema[1]) extrema[1] = planet.x
                    if (planet.y > extrema[2]) extrema[2] = planet.y
                    if (planet.y < extrema[3]) extrema[3] = planet.y
                    i--
                }
            }
            //Remove the origin planet from the temp planets so that it will not be chosen again to be branched
            tempPlanets.remove(originPlanet)
        }
        //Bind planets to be in coordinates such that they are between 0 and 1
        for (planet in planets) {
            planet.x -= extrema[1]
            planet.y -= extrema[3]
            planet.x /= (extrema[0] - extrema[1])
            planet.y /= (extrema[2] - extrema[3])
        }
    }

    /**
     * Constructs the galaxy recursively by starting at a single point
     * From that point it branches off to a random number of other planets
     * which themselves branch off until there are numPlanets planets
     */
    private fun recursiveConstructor(numPlanets: Int) {
        //Runs until this reaches zero; is an array in order to pass by reference
        val numPlanetsRemaining = Array<Int>(1, { numPlanets })
        //Shows the extreme values of planet locations; Order: max x, min x, max y, min y
        val extrema = Array<Float>(4, { 0f })
        //Define the function for recursively adding planets
        fun addPlanet(originPlanet: Planet, planetsToBeCreated: Array<Int>, extrema: Array<Float>, numPlanets: Int) {
            for (i in 0..(Math.random() * 5).toInt() + 3) { //Makes a random number of branches
                //If there are no more planets to be made, don't run the iteration
                if (planetsToBeCreated[0] <= 0) {
                    return
                }
                //Generate random angle between 0 and 2 * PI
                val angle = PI * 2 * Math.random()
                //Generate a random distance; numbers generated are arbitrary and will be modified to fit screen afterward
                val distance = 0.25 + 0.5 * Math.random()
                //Creates a new planet the generated distance away at the angle, creates a highway between it and origin planet, and adds it to the planet list
                val planet = Planet((originPlanet.x + distance * cos(angle)).toFloat(), (originPlanet.y + distance * sin(angle)).toFloat(), (0.015 + Math.random() * 0.035).toFloat() / numPlanets)
                highways.add(CosmicHighway(originPlanet, planet))
                planets.add(planet)
                //Updates the extrema if relevant
                if (planet.x > extrema[0]) extrema[0] = planet.x
                if (planet.x < extrema[1]) extrema[1] = planet.x
                if (planet.y > extrema[2]) extrema[2] = planet.y
                if (planet.y < extrema[3]) extrema[3] = planet.y
                //One less planet to be created
                planetsToBeCreated[0] -= 1
                //Run the function again from the created planet
                addPlanet(planet, planetsToBeCreated, extrema, numPlanets)
            }
        }
        //Begin the recursive function with a generic planet
        addPlanet(Planet(0f, 0f, (0.015 + Math.random() * 0.035).toFloat() / numPlanets), numPlanetsRemaining, extrema, numPlanets)
        //Modifies all the planets' locations such that they will have coordinates between 0 and 1
        for (planet in planets) {
            planet.x -= extrema[1]
            planet.y -= extrema[3]
            planet.x /= (extrema[0] - extrema[1])
            planet.y /= (extrema[2] - extrema[3])
        }
    }

    /**
     * The algorithm attempts to maximize the spread of the planets probabilistically:
     *  We start with equal probability for any setting in space
     *  For a certain number of cycles, a planet is generated based on the probability of each tile being chosen
     *  Then we can update the probabilities of the tiles to make those close to the new planet less favorable
     *  Probabilities are relative, so we can just add some scalar to each tile's probability based on its distance from
     *  the planet every step
     * While this isn't a world, a good term for this would be 'worldgen'
     */
    private fun distanceConstructor(numPlanets: Int) {
        /*
        * The planets are generated by choosing from a finite set of locations arranged in a square grid
        * The side length of that square is worldSize
        * Changing the scalar multiplier in this variable will affect amount of possible locations for planets
        * As a result, the spacing of the planets will change
        * For instance, decreasing the scalar multiplier will make the planets closer together by decreasing the amount of possible locations
        */
        val worldSize = 8 * numPlanets
        //The grid is a flattened array where every worldSize elements represent a row
        val probabilities = Array(worldSize * worldSize) { 1.0 }
        for (i in 0 until numPlanets) {
            //The following code chooses a new location for a planet
            //Create an array where each element is the sum of the first n elements of probabilities
            val cumulativeProbabilities = probabilities.clone()
            for (j in 1 until probabilities.size) {
                cumulativeProbabilities[j] += cumulativeProbabilities[j - 1]
            }
            //Choose a random number between 0 and the sum of all the probabilities
            val chosenCumulative = Math.random() * probabilities.sum()
            //Then the first number greater than that number is the at the index of the chosen planet
            val chosenLocation = cumulativeProbabilities.indexOfFirst { it > chosenCumulative }
            //Because bigger probabilities result in bigger gaps between adjacent elements in the cumulative
            //probabilities list, the chance that a location is chosen is proportional to the fraction of the total sum
            //its probability takes up

            //Setting the probability of that location to 0 ensures we cannot generate new planets there, and it is
            //also useful for determining the final locations of the planets
            probabilities[chosenLocation] = 0.0

            //Update probabilities
            for (j in 0 until probabilities.size) {
                //j / worldSize - j % worldSize is a conversion from the flattened list to a square grid coordinate system where
                //j / worldSize is row and j % worldSize is column
                //We add a scalar factor based on the Manhattan distance between each tile and the new planet to its probability
                //If the tile in question is already a planet, do nothing
                probabilities[j] = if (probabilities[j] != 0.0) probabilities[j] + Math.pow((Math.abs(j / worldSize - chosenLocation / worldSize) + Math.abs(j % worldSize  - chosenLocation % worldSize)).toDouble(), 1.0) else 0.0
            }
        }
        //When we're finished, every coordinate with a zero probability is market for becoming a planet
        //For each one, add a planet to the list of planets after scaling coordinates to be between 0 and 1
        //0.5 / worldSize accounts for the planet being at the center of the square defined by coordinates instead of its corner
        (0 until probabilities.size)
                .filter { probabilities[it] == 0.0 }
                .mapTo(planets) { Planet((it / worldSize).toFloat() / worldSize + 0.5f / worldSize, (it % worldSize).toFloat() / worldSize + 0.5f / worldSize, ((0.15 + Math.random() * 0.35) / worldSize).toFloat()) }
        /**
         * Edge generation:
         * Goes through each planet in a random order and creates cosmic highways (connections) between planets within .2 of world size distance
         * Doesn't create highways that cross other highways TODO: Doesn't work quite perfectly
         */
        for (p0 in planets.shuffled()) {
            var highwayChance = 1f
            for (p1 in planets) {
                //If the distance between the two planets is greater than .2, go to next planet
                if (sqrt((p0.x - p1.x).pow(2) + (p0.y - p1.y).pow(2)) >= 0.2 || Math.random() > highwayChance) {
                    continue
                }
                //If the current planets can have a path that doesn't intersect an existing highway, or already is an existing highway, or intersect a planet, make a highway
                if (!highways.any {
                            doSegmentsIntersect(p0.x, p0.y, p1.x, p1.y, it.p0.x, it.p0.y, it.p1.x, it.p1.y) || //Highways crosses existing highway
                            (it.p0 == p0 && it.p1 == p1) || (it.p0 == p1 && it.p1 == p0) //Highway already exists but with p0 and p1 switched around
                        } && !planets.filter { it != p0 && it != p1 }.any {
                            Intersector.distanceSegmentPoint(p0.x, p0.y, p1.x, p1.y, it.x, it.y) <= it.radius //Highway doesn't intersect planet
                        }) {
                    highways.add(CosmicHighway(p0, p1))
                    highwayChance *= 1f
                }
            }
        }
    }

    /**
     * Returns whether an intersection happens that isn't an intersection at the endpoints
     * Segments are p0 -> p1 and p2 -> p3
     */
    private fun doSegmentsIntersect(p0x: Float, p0y: Float, p1x: Float, p1y: Float, p2x: Float, p2y: Float, p3x: Float, p3y: Float): Boolean {
        val intersectionPoint = Vector2()
        val intersect = Intersector.intersectSegments(p0x, p0y, p1x, p1y, p2x, p2y, p3x, p3y, intersectionPoint)
        //Returns whether it intersects and that the intersection point isn't an endpoint
        return intersect && !(
                (intersectionPoint.x == p0x && intersectionPoint.y == p0y) ||
                (intersectionPoint.x == p1x && intersectionPoint.y == p1y) ||
                (intersectionPoint.x == p2x && intersectionPoint.y == p2y) ||
                (intersectionPoint.x == p3x && intersectionPoint.y == p3y)
        )
    }

}
