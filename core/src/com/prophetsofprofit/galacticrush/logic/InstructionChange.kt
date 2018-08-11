package com.prophetsofprofit.galacticrush.logic

import com.prophetsofprofit.galacticrush.logic.drone.instruction.Instruction

/**
 * A listy of instructions sent to and from the server and associated to an owner
 * Use this for
 */
class InstructionChange(val ownerId: Int, val gainedInstruction: Instruction) {

    /**
     * Empty constructor for serialization
     */
    constructor() : this(-1, Instruction.NONE)

}