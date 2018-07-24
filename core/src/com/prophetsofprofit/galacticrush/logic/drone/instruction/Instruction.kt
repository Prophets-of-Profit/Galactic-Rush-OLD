package com.prophetsofprofit.galacticrush.logic.drone.instruction

import com.prophetsofprofit.galacticrush.logic.drone.Drone

//Utility alias for calling (Drone) -> Unit a DroneAction
typealias DroneAction = (Drone) -> Unit

/**
 * A class that represents an instruction which is something a drone can do
 * Display Name is the text representation of the instruction and is also used as its ID, so it should be unique
 * Value is how 'good' the instruction is and how rare it is
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
        val addAction: DroneAction = {},
        val removeAction: DroneAction = {},
        val startCycleAction: DroneAction = {},
        val mainAction: DroneAction = {},
        val endCylcleAction: DroneAction = {}
) {

}

/**
 * A class that is an instruction but with mutable properties
 */
class InstructionInstance(val baseInstruction: Instruction) {
    var health = this.baseInstruction.health
}