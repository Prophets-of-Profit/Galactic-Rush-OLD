package com.prophetsofprofit.galacticrush.logic.drone

import com.badlogic.gdx.graphics.Texture
import com.prophetsofprofit.galacticrush.logic.drone.instruction.Instruction
import com.prophetsofprofit.galacticrush.logic.drone.instruction.InstructionInstance
import com.prophetsofprofit.galacticrush.logic.map.Galaxy
import com.prophetsofprofit.galacticrush.logic.map.Planet
import java.util.*

//How the drone looks by default
val baseDroneImage = Texture("image/drone/base.png")

/**
 * A class that represent's a player's drone
 * Is the main unit of the game that carries out instructions
 * Is what is used to achieve victory
 * Location stores the id of the planet
 */
class Drone(val ownerId: Int, var locationId: Int) {

    //When the drone was initialized: the game assumes that this along with ownerId are unique
    val creationTime = Date()
    //The instructions the drone currently has
    val instructions = mutableListOf<InstructionInstance>()
    //How much memory the drone has
    val totalMemory = 10
    //A convenience getter to get how much free memory the drone has
    val memoryAvailable
        get() = this.totalMemory - instructions.fold(0) { consumedMemory, instruction -> consumedMemory + instruction.baseInstruction.memorySize }
    //Which instruction the drone is currently reading
    var pointer = 0
    //The planet that the drone has selected
    var selectedPlanet: Int? = null
    //The drone that this drone has selected
    var selectedDroneCreation: Date? = null
    var selectedDroneOwner: Int? = null
    //Whether the drone is done completing its command queue
    var queueFinished = false
    //Whether the drone is destroyed or not
    var isDestroyed = false
    //What the drone displays as in lists
    var name = "Drone"
    //What the drone looks like on the map
    val image: Texture
        get() {
            return baseDroneImage
        }

    /**
     * Empty constructor for serialization
     */
    constructor() : this(-1, -1)

    /**
     * Adds the given instruction this drone at the specified location
     */
    fun addInstruction(instruction: Instruction, galaxy: Galaxy, locationIndex: Int = this.instructions.size) {
        when (locationIndex) {
            this.instructions.size -> this.instructions.add(InstructionInstance(instruction))
            0 -> {
                val copy = this.instructions.toMutableList()
                this.instructions.clear()
                this.instructions.add(InstructionInstance(instruction))
                this.instructions.addAll(copy)
            }
            else -> {
                this.instructions.add(InstructionInstance(Instruction.NONE))
                (locationIndex until this.instructions.size).reversed().forEach { this.instructions[it] = this.instructions[it - 1] }
                this.instructions[locationIndex] = InstructionInstance(instruction)
            }
        }
        instruction.addAction(this, galaxy)
    }

    /**
     * Removes the first instance of the given instruction
     */
    fun removeInstruction(instruction: InstructionInstance, galaxy: Galaxy) {
        instruction.baseInstruction.removeAction(this, galaxy)
        this.instructions.remove(instruction)
    }

    /**
     * Removes the instruction at the given index
     */
    fun removeInstruction(locationIndex: Int, galaxy: Galaxy) {
        this.instructions[locationIndex].baseInstruction.removeAction(this, galaxy)
        this.instructions.removeAt(locationIndex)
    }

    /**
     * Calls startCycle for all instructions in the queue
     */
    fun startCycle(galaxy: Galaxy) {
        this.instructions.forEach { it.baseInstruction.startCycleAction(this, galaxy) }
    }

    /**
     * Calls mainAction for the drone's current instruction and then increments its pointer
     */
    fun mainAction(galaxy: Galaxy) {
        if (this.instructions.isEmpty()) {
            this.queueFinished = true
            return
        }
        this.instructions[this.pointer].baseInstruction.mainAction(this, galaxy)
        this.advancePointer(1)
    }

    /**
     * Calls endCycle for all instructions in the queue
     */
    fun endCycle(galaxy: Galaxy) {
        this.instructions.forEach { it.baseInstruction.endCycleAction(this, galaxy) }
    }

    /**
     * Advances the pointer by some number of steps, changing what the drone will read next
     */
    fun advancePointer(steps: Int) {
        this.pointer += steps
        if (this.pointer >= this.instructions.size) {
            this.pointer = this.instructions.lastIndex
            this.queueFinished = true
        }
    }

    /**
     * Resets the drone's pointer for the next turn
     */
    fun resetQueue() {
        this.pointer = 0
        this.queueFinished = false
        this.selectedPlanet = null
        this.selectedDroneCreation = null
        this.selectedDroneOwner = null
    }

    /**
     * Attempts to distribute damage among instructions
     */
    fun takeDamage(damage: Int, galaxy: Galaxy) {
        val damageToAll = damage / this.instructions.size
        val numToReceieveExtra = damage % this.instructions.size
        this.instructions.forEach { it.health -= damageToAll }
        this.instructions.subList(0, numToReceieveExtra).forEach { it.health-- }
        this.instructions.filter { it.health <= 0 }.forEach { this.removeInstruction(it, galaxy) }
        this.isDestroyed = this.instructions.isEmpty()
    }

    /**
     * Moves the drone to the given planet
     */
    fun moveToPlanet(id: Int, galaxy: Galaxy) {
        this.getLocationAmong(galaxy)!!.drones.remove(this)
        this.locationId = id
        this.getLocationAmong(galaxy)!!.drones.add(this)
    }

    /**
     * Gets the locationId of the drone among a list of planets, or null if it does not exist
     */
    fun getLocationAmong(galaxy: Galaxy): Planet? {
        return galaxy.planets.firstOrNull { it.id == this.locationId }
    }

    /**
     * How the drone will be displayed on the planet listing
     */
    override fun toString(): String {
        return "$name ($ownerId)"
    }

}