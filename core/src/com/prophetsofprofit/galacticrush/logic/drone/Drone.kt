package com.prophetsofprofit.galacticrush.logic.drone

import com.badlogic.gdx.graphics.Texture
import com.prophetsofprofit.galacticrush.defaultDroneNames
import com.prophetsofprofit.galacticrush.logic.drone.instruction.Instruction
import com.prophetsofprofit.galacticrush.logic.drone.instruction.InstructionInstance
import com.prophetsofprofit.galacticrush.logic.map.Galaxy
import java.util.*

//How the drone looks by default
val baseDroneImage = Texture("image/drone/base.png")

/**
 * Small data class that represents what can uniquely identify a drone
 * Drones can't be assigned integer IDs as usual because their creation is concurrent, and guaranteeing a unique int under those conditions is difficult
 */
data class DroneId(val ownerId: Int, val creationTime: Date) {

    /**
     * Empty constructor for serialization
     */
    constructor() : this(-1, Date(-1))
}

/**
 * A class that represent's a player's drone
 * Is the main unit of the game that carries out instructions
 * Is what is used to achieve victory
 * Location stores the id of the planet
 */
class Drone(val ownerId: Int, var locationId: Int) {

    //How much damage the drone deals
    var attack = 3
    //When the drone was initialized: the game assumes that this along with ownerId are unique
    val creationTime = Date()
    //The instructions the drone currently has
    val instructions = mutableListOf<InstructionInstance>()
    //How much memory the drone has
    val totalMemory = 10
    //A convenience getter to get how much free memory the drone has
    val memoryAvailable
        get() = this.totalMemory - instructions.sumBy { it.baseInstruction.memorySize }
    //Which instruction the drone is currently reading
    var pointer = 0
    //The potential ids of planets that the drone could select
    var selectablePlanetIds: MutableList<Int>? = null
    //The potential ids of drones that the drone could select
    var selectableDroneIds: MutableList<DroneId>? = null
    //Whether the drone is done completing its command queue
    var queueFinished = false
    //Whether the drone is destroyed or not
    var isDestroyed = false
    //What the drone displays as in lists
    var name = defaultDroneNames.toMutableList().shuffled().first()
    //What the drone looks like on the map
    val image: Texture
        get() {
            return baseDroneImage
        }
    //What uniquely identifies the drone
    val id: DroneId
        get() = DroneId(this.ownerId, this.creationTime)
    //Data that the drone has that may persist between turns
    val persistentData = mutableMapOf<String, String>()

    /**
     * Empty constructor for serialization
     */
    constructor() : this(-1, -1)

    /**
     * Adds the given instruction this drone at the specified location
     */
    fun addInstruction(instruction: Instruction, galaxy: Galaxy, locationIndex: Int = this.instructions.size) {
        this.instructions.add(locationIndex, InstructionInstance(instruction))
    }

    /**
     * Removes the first instance of the given instruction
     */
    fun removeInstruction(instruction: InstructionInstance, galaxy: Galaxy) {
        instruction.baseInstruction.removeAction(this, galaxy, instruction)
        this.instructions.remove(instruction)
    }

    /**
     * Removes the instruction at the given index
     */
    fun removeInstruction(locationIndex: Int, galaxy: Galaxy) {
        this.instructions[locationIndex].baseInstruction.removeAction(this, galaxy, this.instructions[locationIndex])
        this.instructions.removeAt(locationIndex)
    }

    /**
     * Calls startCycle for all instructions in the queue
     */
    fun startCycle(galaxy: Galaxy) {
        this.resetSelectables(galaxy)
        this.instructions.forEach { it.baseInstruction.startCycleAction(this, galaxy, it) }
    }

    /**
     * Calls mainAction for the drone's current instruction and then increments its pointer
     */
    fun mainAction(galaxy: Galaxy) {
        if (this.instructions.isEmpty()) {
            this.queueFinished = true
            return
        }
        this.instructions[this.pointer].baseInstruction.mainAction(this, galaxy, this.instructions[this.pointer])
        this.advancePointer(1)
    }

    /**
     * Calls endCycle for all instructions in the queue
     */
    fun endCycle(galaxy: Galaxy) {
        this.instructions.forEach { it.baseInstruction.endCycleAction(this, galaxy, it) }
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
    fun resetQueue(galaxy: Galaxy) {
        this.pointer = 0
        this.queueFinished = false
        this.resetSelectables(galaxy)
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
        galaxy.getPlanetWithId(this.locationId)!!.drones.remove(this)
        this.locationId = id
        galaxy.getPlanetWithId(this.locationId)!!.drones.add(this)
    }

    /**
     * Resets the possible selectable planets and drones
     */
    fun resetSelectables(galaxy: Galaxy) {
        this.selectableDroneIds = galaxy.getPlanetWithId(this.locationId)!!.drones.map { it.id }.toMutableList()
        this.selectablePlanetIds = galaxy.planetsAdjacentTo(this.locationId).toMutableList()
    }

    /**
     * How the drone will be displayed on the planet listing
     */
    override fun toString(): String {
        return "$name ($ownerId)"
    }

}