package com.prophetsofprofit.galacticrush.logic

import com.badlogic.gdx.graphics.Color
import com.prophetsofprofit.galacticrush.logic.drone.Drone
import com.prophetsofprofit.galacticrush.logic.map.Galaxy

/**
 * The main game object
 * Handles attributes of the current game, and is serialized for networking
 */
class Game(val players: Array<Int>, val galaxy: Galaxy) {

    /**
     * Empty constructor for serialization
     */
    constructor() : this(arrayOf(), Galaxy(0, listOf()))

    //The amount of turns that have passed since the game was created
    var turnsPlayed = 0
    //The players who need to submit their changes for the drones to commence
    val waitingOn = this.players.toMutableList()
    //The drones that currently exist in the game; should be ordered in order of creation
    val drones: Array<Drone>
        get() {
            return this.galaxy.planets.fold(mutableListOf<Drone>()) { list, currentPlanet -> list.addAll(currentPlanet.drones); list }.toTypedArray()
        }
    //Whether the game has been changed since last send
    var gameChanged = false
    //How much money each player has; maps id to money
    val money = mutableMapOf<Int, Int>()
    //The map of player id to their color
    val playerColors = mutableMapOf<Int, Color>()

    /**
     * Assigns each player a random colors
     */
    init {
        this.players.forEach { playerColors[it] = Color(Math.random().toFloat(), Math.random().toFloat(), Math.random().toFloat(), 1f) }
    }

    /**
     * Makes a copy of the game object
     */
    fun clone(): Game {
        val clone = Game(this.players, this.galaxy.clone())
        clone.turnsPlayed = turnsPlayed
        this.money.keys.forEach { clone.money[it] = this.money[it]!! }
        this.playerColors.keys.forEach { clone.playerColors[it] = this.playerColors[it]!! }
        return clone
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
        println(this.drones.size)
        println(change.changedDrones)
        for (changedDrone in change.changedDrones) {
            this.drones.filter { it.ownerId == changedDrone.ownerId && it.creationTime == changedDrone.creationTime }.forEach { it.getLocationAmong(this.galaxy.planets.toTypedArray())!!.drones.remove(it) }
            changedDrone.getLocationAmong(this.galaxy.planets.toTypedArray())!!.drones.add(changedDrone)
        }
        println(this.drones.size)
        //TODO apply changes to instructions
        this.waitingOn.remove(change.ownerId)
        if (this.waitingOn.isEmpty()) {
            //this.doDroneTurn()
            this.gameChanged = true
            this.players.mapTo(this.waitingOn) { it }
        }
    }

    /**
     * Performs one action per drone for all drones that can perform an action; won't be callable until game is ready
     */
    /*fun doDroneTurn() {
        //If waiting on players don't do anything
        if (waitingOn.isNotEmpty()) {
            return
        }
        //Complete the actions of all the drones who can do actions in the queue
        this.drones.filterNot { it.queueFinished }.forEach { it.completeAction() }
        //If all the drones are now finished, wait for players and reset drones
        if (this.drones.all { it.queueFinished }) {
            this.drones.forEach { it.resetQueue() }
            players.mapTo(waitingOn) { it }
        }
        this.gameChanged = true
    }*/

}
