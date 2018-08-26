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
    //The game's current phase
    var phase: GamePhase = GamePhase.DRAFT_PHASE
        set(value) {
            field = value
            this.hasBeenUpdated = true
        }
    //Whether the game has been updated; is used for determining whether to send the game over the network
    var hasBeenUpdated = false
    //The players who need to submit their changes for the drones to commence
    val waitingOn = this.players.toMutableList()
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
    //The instructions that each player is being offered right now; initial value is draft for all players
    val currentDraft = players.map { it to this.drawInstructions().toMutableList() }.toMap()
    //The number of times the draft has been called in the current draft cycle
    var draftCounter = 0

    /**
     * Takes a random draw of (player number + 2) instructions from the instruction pool
     */
    fun drawInstructions(types: Array<InstructionType> = InstructionType.values()): List<Instruction> {
        val randomOptions = this.instructionPool.filter { it.types.intersect(types.asIterable()).isNotEmpty() }.shuffled()
        val draftOptions = randomOptions.slice(0 until minOf(randomOptions.size, this.players.size + 2))
        draftOptions.forEach { this.instructionPool.remove(it) }
        return draftOptions
    }

    /**
     * A method that collects changes, verifies their integrity, and then applies them to the game
     */
    fun collectChange(change: Change) {
        if (this.phase == GamePhase.DRAFT_PHASE) {
            change as PlayerChange
            println("Received from ${change.ownerId}: $change \nwaiting on ${this.waitingOn}")
            if (!this.waitingOn.contains(change.ownerId) || change.gainedInstructions.size != 1 || !this.currentDraft[change.ownerId]!!.containsAll(change.gainedInstructions)) {
                return
            }
            this.waitingOn.remove(change.ownerId)
            this.currentDraft[change.ownerId]!!.remove(change.gainedInstructions.first())
            this.unlockedInstructions[change.ownerId]!!.addAll(change.gainedInstructions)
            if (this.waitingOn.isEmpty()) {
                if (this.draftCounter < this.players.size - 1) {
                    draftCounter++
                    println(this.currentDraft)
                    val firstPlayerOptions = this.currentDraft[this.players[0]]!!.toMutableList()
                    val secondPlayerOptions = this.currentDraft[this.players[1]]!!.toMutableList()
                    this.currentDraft[this.players[0]]!!.clear()
                    this.currentDraft[this.players[0]]!!.addAll(secondPlayerOptions)
                    this.currentDraft[this.players[1]]!!.clear()
                    this.currentDraft[this.players[1]]!!.addAll(firstPlayerOptions)
                    this.hasBeenUpdated = true
                    println(this.currentDraft)
                } else {
                    this.currentDraft.values.forEach { this.instructionPool.addAll(it); it.clear() }
                    this.draftCounter = 0
                    this.phase = GamePhase.PLAYER_FREE_PHASE
                }
                this.waitingOn.addAll(this.players)
            }
        } else if (this.phase == GamePhase.PLAYER_FREE_PHASE) {
            change as PlayerChange
            if (!this.waitingOn.contains(change.ownerId)) {
                return
            }
            //TODO: verify change integrity
            //Add all the changes into the game
            for (changedDrone in change.changedDrones) {
                this.drones.filter { it.ownerId == changedDrone.ownerId && it.creationTime == changedDrone.creationTime }.forEach { this.galaxy.getPlanetWithId(it.locationId)!!.drones.remove(it) }
                this.galaxy.getPlanetWithId(changedDrone.locationId)!!.drones.add(changedDrone)
            }
            //TODO apply changes to instructions
            this.waitingOn.remove(change.ownerId)
            if (this.waitingOn.isEmpty()) {
                this.droneTurnChanges.clear()
                this.phase = GamePhase.DRONE_PHASE
            }
        }
    }

    /**
     * Performs one action per drone for all drones that can perform an action; won't be callable until game is ready
     * Returns whether the drone turns are done
     */
    fun doDroneTurn(): Boolean {
        //If phase isn't right, don't do anything
        if (this.phase != GamePhase.DRONE_PHASE) {
            return true
        }
        val changedDrones = mutableListOf<Drone>()
        val changedPlanets = mutableListOf<Planet>()
        //If this is the first doDroneTurn call for this turn, start the cycle for each drone
        if (this.droneTurnChanges.isEmpty()) {
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
            this.drones.forEach { it.resetQueue(this.galaxy) }
            this.turnsPlayed++
            this.currentDraft.values.forEach { it.addAll(this.drawInstructions()) }
            this.waitingOn.addAll(this.players)
            this.phase = GamePhase.DRAFT_PHASE
        }
        this.droneTurnChanges.add(Change().also { it.changedDrones.addAll(changedDrones); it.changedPlanets.addAll(changedPlanets) })
        return isDone
    }

}
