package com.prophetsofprofit.galacticrush.logic.drone.instructions

import com.badlogic.gdx.graphics.g2d.Sprite
import com.prophetsofprofit.galacticrush.logic.drone.Drone

/**
 * Forces the drone to repeat the previous instruction
 */
class RepeatPrevious(drone: Drone) :
        Instruction(3, 1, InstructionType.ORDER, Sprite(), drone) {

    /**
     * Empty constructor for serialization
     */
    constructor() : this(Drone())

    /**
     * Calls the previous instruction's act method again.
     */
    override fun act(): Boolean {
        if (this.location == 0) return false
        this.drone.instructions[this.location - 1].act()
        return super.act()
    }

    override fun getDisplayString(): String {
        return "Repeat"
    }

}