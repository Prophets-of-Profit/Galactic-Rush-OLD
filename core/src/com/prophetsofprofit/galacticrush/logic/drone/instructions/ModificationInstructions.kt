package com.prophetsofprofit.galacticrush.logic.drone.instructions

import com.badlogic.gdx.graphics.g2d.Sprite
import com.prophetsofprofit.galacticrush.logic.drone.Drone
import com.prophetsofprofit.galacticrush.logic.map.Planet

//TODO: Add sprites

/**
 * If possible, forces the next instruction to select the drone with the lowest attack as of this instruction's action
 */
class SelectLowestAttack(drone: Drone) :
        Instruction(5, 2, InstructionType.MODIFICATION, Sprite(), drone) {

    /**
     * Empty constructor for serialization
     */
    constructor() : this(Drone())

    /**
     * Selects the drone with lowest attack if reachable.
     * Fails if there are no drones to select or if there are no instructions after this one
     */
    override fun act(): Boolean {
        if (this.location > this.drone.instructions.size - 2 || this.drone.location.drones.isEmpty()) return false
        this.drone.instructions[this.location + 1].selectedDrone = (this.drone.location.drones.sortedBy { it.attack })[0]
        return true
    }
}
