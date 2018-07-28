package com.prophetsofprofit.galacticrush.logic.drone

import com.badlogic.gdx.graphics.Texture
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
    var selectedPlanet: Planet? = null
    //The drone that this drone has selected
    var selectedDrone: Drone? = null
    //Whether the drone is done completing its command queue
    var queueFinished = false
    //Whether the drone is destroyed or not
    var isDestroyed = false
    //What the drone displays as
    var name = "Drone"

    /**
     * Empty constructor for serialization
     */
    constructor() : this(-1, -1)

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
        this.instructions[this.pointer].baseInstruction.mainAction(this, galaxy)
        this.advancePointer(1)
    }

    /**
     * Calls endCyclye for all instructions in the queue
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
    }

    /**
     * Attempts to distribute damage among instructions
     */
    fun takeDamage(damage: Int, galaxy: Galaxy) {
        val damageToAll = damage / this.instructions.size
        val numToReceieveExtra = damage % this.instructions.size
        this.instructions.forEach { it.health -= damageToAll }
        this.instructions.subList(0, numToReceieveExtra).forEach { it.health-- }
        this.instructions.filter { it.health <= 0 }.forEach { it.baseInstruction.removeAction(this, galaxy); this.instructions.remove(it) }
        this.isDestroyed = this.instructions.isEmpty()
    }

    /**
     * Gets the locationId of the drone among a list of planets, or null if it does not exist
     */
    fun getLocationAmong(planets: Array<Planet>): Planet? {
        return planets.firstOrNull { it.id == this.locationId }
    }

    /**
     * How the drone will be displayed on the planet listing
     */
    override fun toString(): String {
        return "$name ($ownerId)"
    }
}