package com.prophetsofprofit.galacticrush.logic.drone.instructions

import com.badlogic.gdx.graphics.g2d.Sprite
import com.prophetsofprofit.galacticrush.logic.drone.Drone
//TODO: Add sprites for everything

/**
 * Deals damage to the targeted drone according to the user's attack, and the user takes damage
 * according to half the target's attack
 * If no drone is targeted, choose a random one on the same planet
 */
class Attack(location: Int, drone: Drone):
        Instruction(5, 1, InstructionType.COMBAT, location, Sprite(), drone) {

    override fun act(): Boolean {
        if(this.selectedDrone == null) {
            this.selectedDrone = this.drone.location.drones.shuffled()[0]
        }
        (this.selectedDrone as Drone).takeDamage(this.drone.attack)
        this.drone.takeDamage((this.selectedDrone as Drone).attack / 2)
        return true
    }
}
