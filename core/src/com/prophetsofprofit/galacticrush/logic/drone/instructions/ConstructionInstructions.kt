package com.prophetsofprofit.galacticrush.logic.drone.instructions

import com.badlogic.gdx.graphics.g2d.Sprite
import com.prophetsofprofit.galacticrush.logic.drone.Drone
import com.prophetsofprofit.galacticrush.logic.facility.ConstructionFacility

//TODO: Add sprites for everything

/**
 * Constructs a drone construction facility
 */
class BuildFacility(location: Int, drone: Drone):
        Instruction(2, 5, InstructionType.CONSTRUCTION, location, Sprite(), drone) {

    /**
     * Activates every turn
     */
    override fun act(): Boolean {
        this.drone.location.facilities.add(ConstructionFacility(this.drone.ownerId))
        return true
    }
}