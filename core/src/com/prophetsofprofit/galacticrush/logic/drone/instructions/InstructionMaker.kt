package com.prophetsofprofit.galacticrush.logic.drone.instructions

import com.prophetsofprofit.galacticrush.logic.drone.Drone

/**
 * Creates instructions given a drone
 * Each instruction maker should create a new instruction for the drone to add
 */
enum class InstructionMaker(val createInstructionInstanceFor: (Drone) -> Instruction, val rarity: Int, val type: Int) {
    ATTACK({ Attack(it) }, 5, InstructionType.COMBAT.value),
    BUILDFACILITY({ BuildFacility(it) }, 5, InstructionType.CONSTRUCTION.value),
    SELECTLOWESTATTACK({ SelectLowestAttack(it) }, 2, InstructionType.MODIFICATION.value),
    MOVE({ Move(it) }, 7, InstructionType.MOVEMENT.value),
    REPEATPREVIOUS({ RepeatPrevious(it) }, 2, InstructionType.ORDER.value),
    MINORATTACKBUFF({ MinorAttackBuff(it) }, 4, InstructionType.UPGRADE.value)
}