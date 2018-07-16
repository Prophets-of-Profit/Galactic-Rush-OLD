package com.prophetsofprofit.galacticrush.logic.drone.instructions

import com.badlogic.gdx.graphics.g2d.Sprite
import com.prophetsofprofit.galacticrush.logic.drone.Drone

//TODO: Add sprites for everything

/**
 * Deals damage to the targeted drone according to the user's attack, and the user takes damage
 * according to half the target's attack
 * If no drone is targeted, choose a random one on the same planet
 */
class Attack(location: Int, drone: Drone) :
        Instruction(3, 1, InstructionType.COMBAT, location, Sprite(), drone) {

    /**
     * Empty constructor for serialization
     */
    constructor() : this(-1, Drone())

    /**
     * Activates every turn, provided there is a drone on the same planet as the user
     */
    override fun act(): Boolean {
        if (this.drone.location.drones.isEmpty()) return false
        if (this.selectedDrone == null) {
            this.selectedDrone = this.drone.location.drones.shuffled()[0]
        }
        (this.selectedDrone as Drone).takeDamage(this.drone.attack)
        this.drone.takeDamage((this.selectedDrone as Drone).attack / 2)
        return true
    }
}
