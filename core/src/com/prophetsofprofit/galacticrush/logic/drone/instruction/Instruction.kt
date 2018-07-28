package com.prophetsofprofit.galacticrush.logic.drone.instruction

import com.prophetsofprofit.galacticrush.logic.drone.Drone
import com.prophetsofprofit.galacticrush.logic.map.Galaxy

//Utility alias for calling (Drone, Galaxy) -> Unit a DroneAction
typealias DroneAction = (Drone, Galaxy) -> Unit

/**
 * A class that represents an instruction which is something a drone can do
 * Display Name is the text representation of the instruction and is also used as its ID, so it should be unique
 * Value is how 'good' the instruction is and how rare it is (how many times it appears in the instruction pool relative to every other instruction)
 * Memory Size is how much space in the drone queue the instruction takes
 * Health is how much damage the instruction can take before getting destroyed
 * Types is what categories the instruction would be classified as
 */
enum class Instruction(
        val displayName: String,
        val value: Int,
        val memorySize: Int,
        val health: Int,
        val types: Array<InstructionType>,
        val addAction: DroneAction = { _, _ -> },
        val removeAction: DroneAction = { _, _ -> },
        val startCycleAction: DroneAction = { _, _ -> },
        val mainAction: DroneAction = { _, _ -> },
        val endCycleAction: DroneAction = { _, _ -> }
) {
    MOVE(
        "Move",
        10,
        2,
        5,
        arrayOf(InstructionType.MOVEMENT),
        mainAction = {
            drone, galaxy ->
            null
        }
    )
}

/**
 * A class that is an instruction but with mutable properties
 */
class InstructionInstance(val baseInstruction: Instruction) {
    var health = this.baseInstruction.health
}