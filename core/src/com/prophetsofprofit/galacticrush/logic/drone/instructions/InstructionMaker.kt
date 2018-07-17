package com.prophetsofprofit.galacticrush.logic.drone.instructions

import com.prophetsofprofit.galacticrush.logic.drone.Drone

/**
 * Creates instructions given a drone
 * Each instruction maker should create a new instruction for the drone to add
 */
enum class InstructionMaker(val createInstructionInstanceFor: (Drone) -> Instruction) {
    ATTACK({ Attack(it) }),
    BUILDFACILITY({ BuildFacility(it) }),
    SELECTLOWESTATTACK({ SelectLowestAttack(it) }),
    MOVE({ Move(it) }),
    REPEATPREVIOUS({ RepeatPrevious(it) }),
    MINORATTACKBUFF({ MinorAttackBuff(it) })
}