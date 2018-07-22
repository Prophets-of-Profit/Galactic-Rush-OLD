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
    INSTRUCTION, //TODO: ?
    MINING, //The instruction handles mining money
    MODIFICATION, //TODO: ?
    UPGRADE, //TODO: ?
    ORDER //TODO: ?
}