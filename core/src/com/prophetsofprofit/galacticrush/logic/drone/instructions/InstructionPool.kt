package com.prophetsofprofit.galacticrush.logic.drone.instructions

/**
 * A set of instructions that the players choose from when drafting
 * Contains a list of instruction maker functions
 */
class InstructionPool(val size: Int) {

    val instructions = mutableListOf<InstructionMaker>()

    /**
     * Fills the pool according to rarity;
     * Adds rarity * size instruction makers of each type
     */
    init {
        for (instruction in InstructionMaker.values()) {
            for (i in 0 until instruction.rarity * this.size) {
                this.instructions.add(instruction)
            }
        }
    }

    /**
     * Selects a number of random instructions from the pool with specified type(s)
     * and returns them in a list
     */
    fun pull(amount: Int, type: Int = 127): List<InstructionMaker> {
        val pool = this.instructions.filter { (it.type and type) != 0 }
        return pool.shuffled().slice(0 until amount)
    }
}
