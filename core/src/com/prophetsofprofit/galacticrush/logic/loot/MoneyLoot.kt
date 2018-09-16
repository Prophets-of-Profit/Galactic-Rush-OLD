package com.prophetsofprofit.galacticrush.logic.loot

import com.prophetsofprofit.galacticrush.logic.Game
import com.prophetsofprofit.galacticrush.logic.drone.Drone

/**
 * Contains some amount of money depending on the rarity
 */
class MoneyLoot(rarity: Int) : Loot(rarity) {

    var money = 0

    /**
     * Empty constructor for serialization
     */
    constructor() : this(-1)

    init {
        this.money = ((50 * rarity) * (1 + Math.random())).toInt()
    }

    /**
     * Activates upon discovery
     */
    override fun getDiscovered(discoveringPlayer: Int, discoveringDrone: Drone, game: Game) {
        game.money[discoveringPlayer] = game.money[discoveringPlayer]!! + this.money
    }

}
