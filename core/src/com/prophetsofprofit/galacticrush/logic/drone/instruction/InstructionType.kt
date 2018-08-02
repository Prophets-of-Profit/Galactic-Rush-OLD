package com.prophetsofprofit.galacticrush.logic.drone.instruction

/**
 * A enumeration of the types of instructions
 * Just used for classification, but doesn't do anything on its own
 */
enum class InstructionType {
    NONE, //The instruction does nothing
    COMBAT, //The instruction handles fighting other drones
    MOVEMENT, //The instruction handles moving the drone
    CONSTRUCTION, //The instruction handles building bases
    MINING, //The instruction handles mining money
    MODIFICATION, //The instruction modifies the effect of a different instruction in the queue
    UPGRADE, //The instruction modifies the drone's stats
    ORDER //The instruction modifies the order of instruction executions in the queue
}