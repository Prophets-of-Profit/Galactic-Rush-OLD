package com.prophetsofprofit.galacticrush.logic;

import com.prophetsofprofit.galacticrush.logic.instructions.Instruction
import com.prophetsofprofit.galacticrush.logic.map.Planet

/**
 * A class that represent's a player's drone
 * Is the main unit of the game that carries out instructions
 * Is what is used to achieve victory
 */
class Drone(val ownerId: Int) {

    //What the drone will do
    val instructions: Array<Instruction> = arrayOf()
    //Where the drone is TODO: get starting location based on owner id
    var location: Planet = Planet(-1.0, -1.0, -1.0f)
    //How much health the drone has: right now is hardcoded as 100, but if we want, we can make drone health customizable later
    var currentHealth = 100
    var maxHealth = 100
    //How quickly the drone travels along a cosmic highway in percentage; currently 0.25 by default (takes 4 moves to travel across highway)
    var warpSpeed = 0.25

}