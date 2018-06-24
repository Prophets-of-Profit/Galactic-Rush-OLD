package com.prophetsofprofit.galacticrush.logic;

import com.prophetsofprofit.galacticrush.logic.instructions.Instruction
import com.prophetsofprofit.galacticrush.logic.map.Planet
import java.util.*

/**
 * A class that represent's a player's drone
 * Is the main unit of the game that carries out instructions
 * Is what is used to achieve victory
 */
class Drone(val ownerId: Int) {

    //When the drone was initialized: the game assumes that this is unique for each drone which is kinda hacky
    val creationTime = Date()
    //What the drone will do
    val instructions: MutableList<Instruction> = mutableListOf()
    //Where the drone is TODO: get starting location based on owner id
    var location: Planet = Planet(-1.0f, -1.0f, -1.0f)
    //An arbitrary measure of how many instructions a drone can hold;
    //Each instruction has a certain memory use, and the total cannot exceed this value
    val maxMemory = 10
    //How much memory the drone has available
    val memoryAvailable: Int
        get() {
            return this.maxMemory - this.instructions.sumBy { it.memory }
        }

    //The following methods interface with the drone's instruction structure

    /**
     * Adds an instruction to the end of the drone's task list
     */
    fun add(instruction: Instruction): Boolean {
        if(this.memoryAvailable < instruction.memory) return false
        this.instructions.add(instruction)
        return true
    }

    /**
     * Swaps the positions of two instructions in the drone's task list
     */
    fun swap(index1: Int, index2: Int): Boolean {
        if(index1 >= this.instructions.size || index2 >= this.instructions.size) return false
        val placeholder = this.instructions[index1]
        this.instructions[index1] = this.instructions[index2]
        this.instructions[index2] = placeholder
        return true
    }

    /**
     * Pops the last instruction of the drone's task list, freeing memory
     */
    fun pop(): Boolean {
        if(this.instructions.size == 0) return false
        this.instructions.removeAt(this.instructions.size - 1)
        return true
    }
}