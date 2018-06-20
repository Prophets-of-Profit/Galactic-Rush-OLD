package com.prophetsofprofit.galacticrush.logic.map

import com.prophetsofprofit.galacticrush.logic.Drone

/**
 * The class that serves as a connection between two planets
 * Is basically an edge in the graph that is the galaxy, where the nodes are planets
 * The cosmic highway is the path between p0 and p1
 * These edges are not directional, so travel is possible between p0 to p1, and p1 to p0
 * TODO: expectations for drone movement along highways: is it instant or does it take time? Current implementation is that it takes time and is really disgusting
 * TODO: clean this code: is very disgusting, courtesy of Saurabh Totey
 */
class CosmicHighway(val p0: Planet, val p1: Planet) {

    /**
     * Destination planets are mapped to a map of the drones travelling to the destination mapped with their distance along the highway
     * Distance along highways go from 0 to 1, where 0 is on starting planet, and 1 is at the destination planet
     */
    val destinationToDronesTravelling = mapOf<Planet, MutableMap<Drone, Double>>(p0 to mutableMapOf(), p1 to mutableMapOf())

    /**
     * Moves all contained drones in the proper direction based on their speeds and current locations
     */
    fun moveDrones() {
        for ((destination, dronesTravelling) in this.destinationToDronesTravelling) {
            for ((drone, distance) in dronesTravelling) {
                dronesTravelling[drone] = drone.warpSpeed + distance
                if (dronesTravelling[drone]!! >= 1.0) {
                    drone.location = destination
                    dronesTravelling.remove(drone)
                }
            }
        }
    }

    /**
     * Adds a drone to the highway, en route to the given planet
     */
    fun addDrone(drone: Drone, origin: Planet) {
        assert(p0 == origin || p1 == origin) { "Cannot add drone $drone to $this because it is set to start at $origin, which does not exist on this Cosmic Highway!" }
        (this.destinationToDronesTravelling[if (p0 == origin) p1 else p0])!![drone] = 0.0
    }

}