package com.prophetsofprofit.galacticrush.logic

import com.prophetsofprofit.galacticrush.logic.instructions.Instruction
import java.io.Serializable

/**
 * A class that represents a change that a player wants to make to the game
 * Every player directly modifies changes and then sends the changes to the master copy of the game
 */
class Change(val ownerId: Int): Serializable {

    //What the player has changed in regards to drones
    val changedDrones = mutableListOf<Drone>()
    //What instruction the user has gained
    val gainedInstruction: Instruction? = null

}