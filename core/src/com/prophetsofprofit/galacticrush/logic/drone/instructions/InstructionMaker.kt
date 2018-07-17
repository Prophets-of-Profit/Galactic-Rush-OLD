package com.prophetsofprofit.galacticrush.logic.drone.instructions

import com.prophetsofprofit.galacticrush.logic.drone.Drone
import com.prophetsofprofit.galacticrush.logic.drone.instructions.Attack
import com.prophetsofprofit.galacticrush.logic.drone.instructions.BuildFacility
import com.prophetsofprofit.galacticrush.logic.drone.instructions.SelectLowestAttack
import com.prophetsofprofit.galacticrush.logic.drone.instructions.Move
import com.prophetsofprofit.galacticrush.logic.drone.instructions.RepeatPrevious
import com.prophetsofprofit.galacticrush.logic.drone.instructions.MinorAttackBuff

/**
 * Creates instructions given a drone
 * Each instruction maker should create a new instruction for the drone to add
 */
enum class InstructionMaker(val action: (Drone) -> Instruction) {
    ATTACK({ drone -> Attack(drone) }),
    BUILDFACILITY({ drone -> BuildFacility(drone) }),
    SELECTLOWESTATTACK({ drone -> SelectLowestAttack(drone) }),
    MOVE({ drone -> Move(drone) }),
    REPEATPREVIOUS({ drone -> RepeatPrevious(drone) }),
    MINORATTACKBUFF({ drone -> MinorAttackBuff(drone)})
}