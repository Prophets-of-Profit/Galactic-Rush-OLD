package com.prophetsofprofit.galacticrush.logic.drone.instructions

/**
 * A set of instructions that the players choose from when drafting
 */
class InstructionPool(val size: Int) {

    val instructions = mutableListOf<InstructionMaker>()

    /**
     * Deterministically fills the pool according to rarity
     */
    init {
        for (instruction in InstructionMaker.values()) {
            for (i in 0 until instruction.rarity * this.size) {
                this.instructions.add(instruction)
            }
        }
    }

    /**
     * Selects a number of random instructions from the pool with specifiec type(s)
     */
    fun pull(amount: Int, type: Int = 127): List<InstructionMaker> {
        val pool = this.instructions.filter { (it.type and type) != 0 }
        return pool.shuffled().slice(0 until amount)
    }
}
