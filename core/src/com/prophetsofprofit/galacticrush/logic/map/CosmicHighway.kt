package com.prophetsofprofit.galacticrush.logic.map

import com.prophetsofprofit.galacticrush.logic.Drone

/**
 * The class that serves as a connection between two planets
 * Is basically an edge in the graph that is the galaxy, where the nodes are planets
 * The cosmic highway is the path between p0 and p1
 * These edges are not directional, so travel is possible between p0 to p1, and p1 to p0
 */
class CosmicHighway(val p0: Planet, val p1: Planet) {

    /**
     * Two hashes, with keys being drones and the values associated their distance along the highway
     * Distances are between zero and one, from start to completion respectively
     * travelling0 contains drones travelling to p0, travelling1 contains those travelling to p1
     */
    val dronesTravelling0 = mutableMapOf<Drone, Double>() 
    val dronesTravelling1 = mutableMapOf<Drone, Double>() 

    /**
     * Moves all contained drones in the proper direction
     * Uses the private moveDroneHash method for each direction
     */
    fun moveDrones() {
        moveDroneHash(dronesTravelling0, p0)
        moveDroneHash(dronesTravelling1, p1)
    }

    /**
     * Moves all of the drones in the given hash toward the given destination
     * If the drone reaches 1 distance or greater, remove it from the hash and set its location to be the destination
     * For internal use only
     */
    private fun moveDroneHash(travelling: MutableMap<Drone, Double>, destination: Planet) {
        for(drone in travelling.keys) {
            travelling[drone] += drone.warpSpeed;
            if(travelling[drone] >= 1.0) {
                travelling[drone].location = destination;
                travelling.remove(drone)
            }
        }
    }

    /**
     * Adds a drone to the highway, en route to the given planet
     * If the destination is not a planet connected to this highway, then nothing happens
     */
    fun addDrone(drone: Drone, origin: Planet) {
        if(origin === p0) {
            dronesTravelling1.put(drone, 0.0)
        } else if(origin === p1) {
            dronesTravelling0.put(drone, 0.0)
        }
    }

}