package com.prophetsofprofit.galacticrush.logic.drone.instructions

import com.prophetsofprofit.galacticrush.logic.drone.Drone

/**
 * An enumeration of templates for instructions
 * Each contains a function which creates an instruction of a certain type given a drone,
 * as well as a rarity (higher rarity in this case is more common) and a binary type as defined
 * in instruction
 * Each instruction maker template's function should create an instruction of its type
 */
enum class InstructionMaker(val createInstructionInstanceFor: (Drone) -> Instruction, val rarity: Int, val type: Int) {
    ATTACK({ Attack(it) }, 5, InstructionType.COMBAT.value),
    BUILDFACILITY({ BuildFacility(it) }, 5, InstructionType.CONSTRUCTION.value),
    SELECTLOWESTATTACK({ SelectLowestAttack(it) }, 2, InstructionType.MODIFICATION.value),
    MOVE({ Move(it) }, 7, InstructionType.MOVEMENT.value),
    REPEATPREVIOUS({ RepeatPrevious(it) }, 2, InstructionType.ORDER.value),
    MINORATTACKBUFF({ AttackModification(it, 2) }, 4, InstructionType.UPGRADE.value) //Minor attack buff is +2
}