package com.prophetsofprofit.galacticrush.logic

import com.badlogic.gdx.graphics.Color
import com.prophetsofprofit.galacticrush.kryo
import com.prophetsofprofit.galacticrush.logic.base.Base
import com.prophetsofprofit.galacticrush.logic.base.Facility
import com.prophetsofprofit.galacticrush.logic.change.Change
import com.prophetsofprofit.galacticrush.logic.change.PlayerChange
import com.prophetsofprofit.galacticrush.logic.drone.Drone
import com.prophetsofprofit.galacticrush.logic.drone.instruction.Instruction
import com.prophetsofprofit.galacticrush.logic.drone.instruction.InstructionType
import com.prophetsofprofit.galacticrush.logic.map.Galaxy
import com.prophetsofprofit.galacticrush.logic.map.Planet

/**
 * The main game object
 * Handles attributes of the current game, and is serialized for networking
 */
class Game(val initialPlayers: Array<Int>, val galaxy: Galaxy) {

    /**
     * Empty constructor for serialization
     */
    constructor() : this(arrayOf(), Galaxy(0, listOf()))

    //The amount of turns that have passed since the game was created
    var turnsPlayed = 0
    //The drones that currently exist in the game; should be ordered in order of creation
    val drones: Array<Drone>
        get() = this.galaxy.drones
    //The bases that currently exist in the game; ordered arbitrarily
    val bases: Array<Base>
        get() = this.galaxy.bases
    //The players that are still in the game
    val players: Array<Int>
        get() = this.bases.filter { it.facilityHealths.containsKey(Facility.HOME_BASE) }.map { it.ownerId }.toTypedArray()
    //The players who need to submit their changes for the drones to commence
    val waitingOn = this.players.toMutableList()
    //Whether the game has been changed since last send
    var gameChanged = false
    //When the previous doDroneTurn was called
    var prevDroneTurn = -1
    //How much money each player has; maps id to money
    val money = this.players.map { it to 0 }.toMap().toMutableMap()
    //The map of player id to their color
    val playerColors = this.players.map { it to Color(Math.random().toFloat(), Math.random().toFloat(), Math.random().toFloat(), 1f) }.toMap()
    //The list of things that happen after each drone turn
    var droneTurnChanges = mutableListOf<Change>()
    //Which instruction each player has; maps id to instructions
    val unlockedInstructions = this.players.map { it to mutableListOf<Instruction>() }.toMap()
    //The instructions that can still be drafted
    val instructionPool = Instruction.values().map { instruction -> Array(instruction.value) { instruction } }.toTypedArray().flatten().toMutableList()
    //The player that is currently drafting
    var currentDrafter: Int? = null
    //The draft options the current drafter is receiving
    var draftOptions: List<Instruction>? = null

    /**
     * Takes a random draw of (player number + 2) instructions from the instruction pool
     * TODO: don't assume that the pool is larger than the number of instructions queried
     */
    fun drawInstructions(types: Array<InstructionType> = InstructionType.values()): List<Instruction> {
        val draftOptions = this.instructionPool.filter { it.types.intersect(types.asIterable()).isNotEmpty() }.shuffled().slice(0 until this.players.size + 2)
        draftOptions.forEach { this.instructionPool.remove(it) }
        return draftOptions
    }

    /**
     * A method that collects changes, verifies their integrity, and then applies them to the game
     */
    fun collectChange(change: Change) {
        if (change is PlayerChange) {
            if (!this.waitingOn.contains(change.ownerId)) {
                return
            }
            //TODO: verify droneChange integrity
            //Add all the changes into the game
            for (changedDrone in change.changedDrones) {
                this.drones.filter { it.ownerId == changedDrone.ownerId && it.creationTime == changedDrone.creationTime }.forEach { this.galaxy.getPlanetWithId(it.locationId)!!.drones.remove(it) }
                this.galaxy.getPlanetWithId(changedDrone.locationId)!!.drones.add(changedDrone)
            }
            //TODO apply changes to instructions
            this.waitingOn.remove(change.ownerId)
            if (this.waitingOn.isEmpty()) {
                this.turnsPlayed++
                this.droneTurnChanges.clear()
                this.gameChanged = true
            }
        } else {
            //TODO:
        }
    }

    /**
     * Performs one action per drone for all drones that can perform an action; won't be callable until game is ready
     * Returns whether the drone turns are done
     */
    fun doDroneTurn(): Boolean {
        val changedDrones = mutableListOf<Drone>()
        val changedPlanets = mutableListOf<Planet>()
        //If waiting on players don't do anything
        if (this.players.size <= 1) {
            return true
        }
        //If this is the first doDroneTurn call for this turn, start the cycle for each drone
        if (this.prevDroneTurn != this.turnsPlayed) {
            this.prevDroneTurn = this.turnsPlayed
            this.drones.forEach { it.startCycle(this.galaxy) }
        }
        //Complete the actions of all the drones who can do actions in the queue
        this.drones.filterNot { it.queueFinished }.forEach { it.mainAction(this.galaxy); changedDrones.add(kryo.copy(it)) }
        //Removes all of the destroyed drones
        this.drones.filter { it.isDestroyed }.forEach { this.galaxy.getPlanetWithId(it.locationId)!!.drones.remove(it) }
        //Remove all of the destroyed facilities and bases
        this.bases.filter { it.health <= 0 || it.facilityHealths.isEmpty() }.forEach { galaxy.getPlanetWithId(it.locationId)!!.base = null }
        //If all the drones are now finished, wait for players and reset drones
        val isDone = this.drones.all { it.queueFinished }
        if (isDone) {
            this.drones.forEach { it.endCycle(this.galaxy) }
            this.players.mapTo(this.waitingOn) { it }
            this.drones.forEach { it.resetQueue(this.galaxy) }
        }
        this.droneTurnChanges.add(Change().also { it.changedDrones.addAll(changedDrones); it.changedPlanets.addAll(changedPlanets) })
        return isDone
    }

}
