package com.prophetsofprofit.galacticrush.logic.drone.instructions

import com.badlogic.gdx.graphics.g2d.Sprite
import com.prophetsofprofit.galacticrush.logic.drone.Drone
import com.prophetsofprofit.galacticrush.logic.map.Planet

//TODO: Add sprites for everything

/**
 * Moves the user to a planet across a single cosmic highway
 * If no planet is targeted, choose a random reachable one
 */
class Move(location: Int = -1, drone: Drone = Drone()):
        Instruction(3, 1, InstructionType.MOVEMENT, location, Sprite(), drone) {

    /**
     * Activates every turn, provided there is a planet reachable from the drone's location
     * TODO: fix
     */
    override fun act(): Boolean {
//        if (this.drone.location.connectedPlanets.isEmpty()) return false
//        if (this.selectedPlanet == null) {
//            this.selectedPlanet = this.drone.location.connectedPlanets.shuffled()[0]
//        }
//        this.drone.location = this.selectedPlanet as Planet
//        return true
        return false
    }
}
