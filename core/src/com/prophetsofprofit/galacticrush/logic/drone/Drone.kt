package com.prophetsofprofit.galacticrush.logic.drone;

import com.prophetsofprofit.galacticrush.logic.drone.instructions.Instruction
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
    //The modifiers of the drone's behavior and abilities
    val instructions: MutableList<Instruction> = mutableListOf()
    //Which instruction the drone is currently reading
    var pointer = 0
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
    //Whether the drone is done completing its command queue
    var queueFinished = false

    //The following methods interface with the drone's instruction structure

    /**
     * Adds an instruction to the end of the drone's task list
     */
    fun add(instruction: Instruction): Boolean {
        if (this.memoryAvailable < instruction.memory) return false
        if (!instruction.add()) return false
        this.instructions.add(instruction)
        return true
    }

    /**
     * Swaps the positions of two instructions in the drone's task list
     */
    fun swap(index1: Int, index2: Int): Boolean {
        if (index1 >= this.instructions.size || index2 >= this.instructions.size) return false
        val placeholder = this.instructions[index1]
        this.instructions[index1] = this.instructions[index2]
        this.instructions[index1].location = index1
        this.instructions[index2] = placeholder
        this.instructions[index2].location = index2
        return true
    }

    /**
     * Pops the last instruction of the drone's task list, freeing memory
     */
    fun pop(): Boolean {
        if (this.instructions.size == 0) return false
        this.instructions[this.instructions.size - 1].remove()
        this.instructions.removeAt(this.instructions.size - 1)
        return true
    }

    /**
     * Does the queued action
     */
    fun completeAction() {
        this.instructions[this.pointer].act()
        this.advancePointer(1)
    }

    /**
     * Advances the pointer by some number of steps, changing what the drone will read next
     */
    fun advancePointer(steps: Int) {
        this.pointer += steps
        if (this.pointer >= this.instructions.size) {
            this.pointer = this.instructions.size - 1
            this.queueFinished = true
        }
    }

    /**
     * Resets the drone's pointer for the next turn
     */
    fun resetQueue() {
        this.pointer = 0
        this.queueFinished = false
    }

    /**
     * Attempts to distribute damage among instructions
     */
    fun takeDamage(damage: Int) {
        /*
         * Attempts to distribute damage among instructions
         * Loops though instructions, damaging them one at a time until all damage has been done
         */
        var instructionToDamage = 0
        for (i in 0 until damage) {
            this.instructions[instructionToDamage].takeDamage()
            //If the instruction's health reaches zero, destroy it and do not increment index
            // (because it is removed from the list and the list's length changes)
            if (this.instructions[instructionToDamage].health <= 0) {
                this.instructions[instructionToDamage].getDestroyed()
            } else {
                //Increment location index
                instructionToDamage = (instructionToDamage + 1) % this.instructions.size
            }
        }
        //If the drone has no instructions left it dies a sad and lonely death
        if (this.instructions.isEmpty()) this.getDestroyed()
    }

    /**
     * Remove the drone from the world's list of drones so it can be garbage collected
     * TODO
     */
    fun getDestroyed() {

    }
}