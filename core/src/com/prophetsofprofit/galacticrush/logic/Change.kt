package com.prophetsofprofit.galacticrush.logic

import com.prophetsofprofit.galacticrush.logic.drone.Drone
import com.prophetsofprofit.galacticrush.logic.drone.instructions.Instruction
import com.prophetsofprofit.galacticrush.logic.drone.instructions.InstructionMaker

/**
 * A class that represents a change that a player wants to make to the game
 * Every player directly modifies changes and then sends the changes to the master copy of the game
 */
class Change(val ownerId: Int) {

    /**
     * Empty constructor for serialization
     */
    constructor() : this(-1)

    //What the player has changed in regards to drones
    val changedDrones = mutableListOf<Drone>()
    //What instruction the user has gained
    val gainedInstruction = mutableListOf<InstructionMaker>()

}