package com.prophetsofprofit.galacticrush.logic.map

/**
 * The class that serves as a connection between two planets
 * Is basically an edge in the graph that is the galaxy, where the nodes are planets
 * The cosmic highway is the path between p1 and p2
 * These edges are not directional, so travel is possible between p1 to p2, and p2 to p1
 */
class CosmicHighway(val p1: Planet, val p2: Planet)