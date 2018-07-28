package com.prophetsofprofit.galacticrush.logic.map

/**
 * The class that serves as a connection between two planets
 * Is basically an edge in the graph that is the galaxy, where the nodes are planets
 * The cosmic highway is the path between p0 and p1
 * These edges are not directional, so travel is possible between p0 to p1, and p1 to p0
 */
class CosmicHighway(val p0: Int, val p1: Int) {

    /**
     * Empty constructor for serialization
     */
    constructor() : this(-1, -1)

    /**
     * Returns null if the id is not in the highway, otherwise return the other id
     */
    fun connects(id: Int): Int? {
        return when (id) {
            p0 -> p1
            p1 -> p0
            else -> null
        }
    }

}