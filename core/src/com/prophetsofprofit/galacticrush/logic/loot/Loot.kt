package com.prophetsofprofit.galacticrush.logic.loot

import com.prophetsofprofit.galacticrush.logic.Game
import com.prophetsofprofit.galacticrush.logic.drone.Drone

/**
 * A package that does something when collected by a drone
 */
abstract class Loot(val rarity: Int) {

    //What to do when a drone mines this
    abstract fun getDiscovered(discoveringPlayer: Int, discoveringDrone: Drone, game: Game)

}

/**
 * Creates a new Loot object
 */
fun selectLoot(): Loot {
    val rarity = mutableListOf(1, 1, 1, 1, 1, 1, 2, 2, 2, 3).shuffled()[0]
    return MoneyLoot(rarity)
}
