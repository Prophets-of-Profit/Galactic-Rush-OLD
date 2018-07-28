package com.prophetsofprofit.galacticrush.logic

import com.badlogic.gdx.graphics.Color
import com.prophetsofprofit.galacticrush.kryo
import com.prophetsofprofit.galacticrush.logic.drone.Drone
import com.prophetsofprofit.galacticrush.logic.facility.Facility
import com.prophetsofprofit.galacticrush.logic.facility.HomeBase
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
        get() {
            return this.galaxy.planets.fold(mutableListOf<Drone>()) { list, currentPlanet -> list.addAll(currentPlanet.drones); list }.sortedBy { it.creationTime }.toTypedArray()
        }
    //The facilities that currently exist in the game; ordered arbitrarily
    val facilities: Array<Facility>
        get() {
            return this.galaxy.planets.fold(mutableListOf<Facility>()) { list, currentPlanet -> list.addAll(currentPlanet.facilities); list }.toTypedArray()
        }
    //The players that are still in the game
    val players: Array<Int>
        get() {
            return this.facilities.filter { it is HomeBase }.map { it.ownerId }.toTypedArray()
        }
    //The players who need to submit their changes for the drones to commence
    val waitingOn = this.players.toMutableList()
    //Whether the game has been changed since last send
    var gameChanged = false
    //When the previous doDroneTurn was called
    var prevDroneTurn = -1
    //How much money each player has; maps id to money
    val money = this.players.map { it to 0 }.toMap().toMutableMap()
    //The map of player id to their color
    val playerColors = mutableMapOf<Int, Color>()
    //The list of things that happen after each drone turn
    var droneTurnChanges = mutableListOf<DroneTurnChange>()

    /**
     * Assigns each player a random colors
     */
    init {
        this.players.forEach { playerColors[it] = Color(Math.random().toFloat(), Math.random().toFloat(), Math.random().toFloat(), 1f) }
    }

    /**
     * A method that collects changes, verifies their integrity, and then applies them to the game
     */
    fun collectChange(change: Change) {
        if (!this.waitingOn.contains(change.ownerId)) {
            return
        }
        //TODO: verify change integrity
        //Add all the changes into the game
        for (changedDrone in change.changedDrones) {
            this.drones.filter { it.ownerId == changedDrone.ownerId && it.creationTime == changedDrone.creationTime }.forEach { it.getLocationAmong(this.galaxy.planets.toTypedArray())!!.drones.remove(it) }
            changedDrone.getLocationAmong(this.galaxy.planets.toTypedArray())!!.drones.add(changedDrone)
        }
        //TODO apply changes to instructions
        this.waitingOn.remove(change.ownerId)
        if (this.waitingOn.isEmpty()) {
            this.players.mapTo(this.waitingOn) { it }
            this.turnsPlayed++
            this.gameChanged = true
        }
    }

    /**
     * Performs one action per drone for all drones that can perform an action; won't be callable until game is ready
     */
    fun doDroneTurn() {
        val changedDrones = mutableListOf<Drone>()
        val changedPlanets = mutableListOf<Planet>()
        //If waiting on players don't do anything
        if (this.waitingOn.isNotEmpty() || this.players.size <= 1) {
            return
        }
        //If this is the first doDroneTurn call for this turn, start the cycle for each drone
        if (this.prevDroneTurn != this.turnsPlayed) {
            this.prevDroneTurn = this.turnsPlayed
            this.drones.forEach { it.startCycle(this.galaxy) }
        }
        //Complete the actions of all the drones who can do actions in the queue
        this.drones.filterNot { it.queueFinished }.forEach { it.mainAction(this.galaxy); changedDrones.add(kryo.copy(it)) }
        //Removes all of the destroyed drones
        this.drones.filter { it.isDestroyed }.forEach { it.getLocationAmong(this.galaxy.planets.toTypedArray())!!.drones.remove(it) }
        //Remove all of the destroyed facilities
        this.facilities.filter { it.health <= 0 }.forEach { it.getLocationAmong(this.galaxy.planets.toTypedArray())!!.facilities.remove(it) }
        //If all the drones are now finished, wait for players and reset drones
        if (this.drones.all { it.queueFinished }) {
            this.drones.forEach { it.endCycle(this.galaxy) }
            this.drones.forEach { it.resetQueue() }
            this.players.mapTo(this.waitingOn) { it }
        }
        this.droneTurnChanges.add(DroneTurnChange(changedDrones.toTypedArray(), changedPlanets.toTypedArray()))
    }

}
