package com.prophetsofprofit.galacticrush.logic.change

import com.prophetsofprofit.galacticrush.logic.drone.instruction.Instruction

/**
 * A listy of instructions sent to and from the server and associated to an owner
 * Use this for
 */
class InstructionChange(val ownerId: Int, val gainedInstruction: Instruction) : Change() {

    /**
     * Empty constructor for serialization
     */
    constructor() : this(-1, Instruction.NONE)

}