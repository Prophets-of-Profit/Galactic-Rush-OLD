package com.prophetsofprofit.galacticrush.logic.map

/**
 * The class that serves as a connection between two planets
 * Is basically an edge in the graph that is the galaxy, where the nodes are planets
 * The cosmic highway is the path between p0 and p1
 * These edges are not directional, so travel is possible between p0 to p1, and p1 to p0
 */
class CosmicHighway(val p0: Planet, val p1: Planet) {

    /**
     * Empty constructor for serialization
     */
    constructor(): this(Planet(), Planet())

}