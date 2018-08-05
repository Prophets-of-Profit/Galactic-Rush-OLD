package com.prophetsofprofit.galacticrush.logic.drone.instruction

import com.prophetsofprofit.galacticrush.logic.base.Base
import com.prophetsofprofit.galacticrush.logic.drone.Drone
import com.prophetsofprofit.galacticrush.logic.map.Attribute
import com.prophetsofprofit.galacticrush.logic.map.Galaxy

//Utility alias for calling (Drone, Galaxy) -> Unit a DroneAction
typealias DroneAction = (Drone, Galaxy, InstructionInstance) -> Unit

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
        val addAction: DroneAction = { _, _, _ -> },
        val removeAction: DroneAction = { _, _, _ -> },
        val startCycleAction: DroneAction = { _, _, _ -> },
        val mainAction: DroneAction = { _, _, _ -> },
        val endCycleAction: DroneAction = { _, _, _ -> }
) {
    NONE("None", 0, 100000, 100000, arrayOf()),
    SELECT_HOTTEST(
            "Select Hottest Adjacent Planet",
            30,
            1,
            3,
            arrayOf(InstructionType.MODIFICATION),
            mainAction = { drone, galaxy, _ ->
                drone.selectedPlanetId = galaxy.planetsAdjacentTo(drone.locationId)
                        .map { galaxy.getPlanetWithId(it)!! }
                        .reduce { hottestPlanet, currentPlanet -> if (hottestPlanet.attributes[Attribute.TEMPERATURE]!! > currentPlanet.attributes[Attribute.TEMPERATURE]!!) hottestPlanet else currentPlanet }.id
            }
    ),
    SELECT_WEAKEST(
            "Select Weakest Drone on this Planet",
            20,
            1,
            3,
            arrayOf(InstructionType.MODIFICATION),
            mainAction = { drone, galaxy, _ ->
                drone.selectedDroneId = galaxy.getPlanetWithId(drone.locationId)!!.drones
                        .reduce { weakestDrone, currentDrone -> if (weakestDrone.attack < currentDrone.attack) weakestDrone else currentDrone }.id
            }
    ),
    MOVE_SELECTED(
            "Move to Selected Planet",
            10,
            2,
            5,
            arrayOf(InstructionType.MOVEMENT),
            mainAction = { drone, galaxy, _ ->
                if (drone.selectedPlanetId != null) {
                    drone.moveToPlanet(drone.selectedPlanetId!!, galaxy)
                }
            }
    ),
    LOOP_3(
            "Loop All Previous Instructions 3 times",
            3,
            3,
            8,
            arrayOf(InstructionType.ORDER),
            startCycleAction = { _, _, instance ->
                instance.data["counter"] = "3"
            },
            mainAction = { drone, _, instance ->
                val counter = instance.data["counter"]!!.toInt()
                if (counter > 0) {
                    drone.pointer = 0
                    instance.data["counter"] = "${counter - 1}"
                }
            }
    ),
    CONSTRUCT_BASE(
            "Construct a Base on Current Planet",
            3,
            5,
            5,
            arrayOf(InstructionType.CONSTRUCTION),
            mainAction = { drone, galaxy, _ ->
                val dronePlanet = galaxy.getPlanetWithId(drone.locationId)!!
                if (dronePlanet.base == null) {
                    dronePlanet.base = Base(drone.ownerId, drone.locationId, arrayOf())
                }
            }
    ),
    REPRODUCTIVE_VIRUS(
            "Spread a Virus Instruction to the Selected Drone",
            2,
            8,
            20,
            arrayOf(InstructionType.VIRUS),
            mainAction = { drone, galaxy, instance ->
                val selectedDrone = galaxy.getDroneWithId(drone.selectedDroneId)
                if (selectedDrone != null && selectedDrone.memoryAvailable >= instance.baseInstruction.memorySize) {
                    selectedDrone.addInstruction(instance.baseInstruction, galaxy)
                }
            }
    ),
    ATTACK_SELECTED(
            "Attack the Selected Drone",
            5,
            5,
            5,
            arrayOf(InstructionType.COMBAT),
            mainAction = { drone, galaxy, _ ->
                galaxy.getDroneWithId(drone.selectedDroneId)?.takeDamage(drone.attack, galaxy)
            }
    )
}

/**
 * A class that is an instruction but with mutable properties
 */
class InstructionInstance(val baseInstruction: Instruction) {

    //The health of the instruction
    var health = this.baseInstruction.health
    //A hashmap of all of the mutable data that the instruction is using/uses
    internal val data = mutableMapOf<String, String>()

    /**
     * Empty constructor for serialization
     */
    constructor() : this(Instruction.NONE)

}