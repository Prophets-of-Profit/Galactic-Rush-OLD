package com.prophetsofprofit.galacticrush.logic

import com.prophetsofprofit.galacticrush.logic.drone.Drone
import com.prophetsofprofit.galacticrush.logic.map.Planet

/**
 * Stores changes to one object caused by a doDroneTurn call
 */
class DroneTurnChange(val changedDrones: Array<Drone>, val changedPlanets: Array<Planet>)