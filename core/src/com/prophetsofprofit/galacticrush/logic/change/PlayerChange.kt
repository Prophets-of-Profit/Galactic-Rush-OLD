package com.prophetsofprofit.galacticrush.logic.change

import com.prophetsofprofit.galacticrush.logic.drone.instruction.Instruction

/**
 * A class that represents a change in a player
 */
class PlayerChange(val ownerId: Int) : Change() {

    /**
     * Empty constructor for serialization
     */
    constructor() : this(-1)

    //A list of gained instructions
    val gainedInstructions = mutableListOf<Instruction>()

}