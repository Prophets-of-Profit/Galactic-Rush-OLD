package com.prophetsofprofit.galacticrush.logic.drone.instruction

/**
 * A enumeration of the types of instructions
 * Just used for classification, but doesn't do anything on its own
 */
enum class InstructionType {
    NONE, //The instruction does nothing
    COMBAT, //The instruction handles fighting other drones
    MOVEMENT, //The instruction handles moving the drone
    PLANET_MODIFICATION, //The instruction modifies either a planet's attributes or builds a base
    MINING, //The instruction handles mining money
    INSTRUCTION_MODIFICATION, //The instruction modifies a drone queue or instruction
    VIRUS, //The instruction harmfully modifies either itself or other drones
    UPGRADE, //The instruction modifies the drone's stats
    ORDER //The instruction modifies the order of instruction executions in the queue
}