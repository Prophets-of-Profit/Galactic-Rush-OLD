package com.prophetsofprofit.galacticrush.logic.drone.instructions

import com.badlogic.gdx.graphics.g2d.Sprite
import com.prophetsofprofit.galacticrush.logic.drone.Drone

/**
 * Provides a small (+2) boost to attack for the drone
 */
class MinorAttackBuff(location: Int, drone: Drone):
        Instruction(3, 1, InstructionType.UPGRADE, location, Sprite(), drone) {

    //TODO: Handle if the parent add or remove methods return false
    /**
     * When this instruction joins a drone's queue, that drone gains + 2 attack.
     */
    override fun add(): Boolean {
        this.drone.attack += 2
        return super.add()
    }

    /**
     * When this instruction leaves the queue, the drone's attack returns to normal
     */
    override fun remove(): Boolean {
        this.drone.attack -= 2
        return super.remove()
    }

}

