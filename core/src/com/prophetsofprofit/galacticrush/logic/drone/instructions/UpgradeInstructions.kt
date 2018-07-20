package com.prophetsofprofit.galacticrush.logic.drone.instructions

import com.badlogic.gdx.graphics.g2d.Sprite
import com.prophetsofprofit.galacticrush.logic.drone.Drone

/**
 * Provides a modification to attack for the drone
 * based on the given modification value
 */
class AttackModification(drone: Drone, val modification: Int):
        Instruction(3, 1, InstructionType.UPGRADE, Sprite(), drone) {

    /**
     * Empty constructor for serialization
     */
    constructor() : this(Drone(), 0)

    //TODO: Handle if the parent add or remove methods return false
    /**
     * When this instruction joins a drone's queue, that drone gains + 2 attack.
     */
    override fun add(): Boolean {
        this.drone.attack += this.modification
        return super.add()
    }

    /**
     * When this instruction leaves the queue, the drone's attack returns to normal
     */
    override fun remove(): Boolean {
        this.drone.attack -= this.modification
        return super.remove()
    }

    /**
     * Returns the representation of this instruction,
     * with the instruction's modification value
     */
    override fun getDisplayString(): String {
        return "Attack " + if(this.modification > 0) "+" else "" + this.modification
    }

}

