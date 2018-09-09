package com.prophetsofprofit.galacticrush.logic.drone.instruction

import com.prophetsofprofit.galacticrush.logic.base.Base
import com.prophetsofprofit.galacticrush.logic.drone.Drone
import com.prophetsofprofit.galacticrush.logic.map.Galaxy
import com.prophetsofprofit.galacticrush.logic.map.PlanetAttribute

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
        val displayDescription: String,
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
    NONE("None", "THIS IS INVALID", 0, 100000, 100000, arrayOf()),
    ORDER_HOTTEST(
            "Order Hottest",
            "Orders the drone's planet selection queue by the hottest available planets.",
            15,
            1,
            3,
            arrayOf(InstructionType.DRONE_MODIFICATION),
            mainAction = { drone, galaxy, _ ->
                drone.selectablePlanetIds = mutableListOf(drone.selectablePlanetIds
                !!.greatestBy { galaxy.getPlanetWithId(it)!!.attributes[PlanetAttribute.TEMPERATURE]!! })
            }
    ),
    ORDER_COLDEST(
            "Order Coldest",
            "Orders the drone's planet selection queue by the coldest available planets.",
            15,
            1,
            3,
            arrayOf(InstructionType.DRONE_MODIFICATION),
            mainAction = { drone, galaxy, _ ->
                drone.selectablePlanetIds = mutableListOf(drone.selectablePlanetIds
                !!.leastBy { galaxy.getPlanetWithId(it)!!.attributes[PlanetAttribute.TEMPERATURE]!! })
            }
    ),
    SELECT_HOTTEST(
            "Select Hottest",
            "Restricts the drone's planet selection queue to the hottest available planets.",
            30,
            1,
            3,
            arrayOf(InstructionType.DRONE_MODIFICATION),
            mainAction = { drone, galaxy, _ ->
                drone.selectablePlanetIds = mutableListOf(drone.selectablePlanetIds
                !!.maxBy { galaxy.getPlanetWithId(it)!!.attributes[PlanetAttribute.TEMPERATURE]!! }!!)
            }
    ),
    SELECT_COLDEST(
            "Order Coldest",
            "Restricts the drone's planet selection queue to the coldest available planets.",
            30,
            1,
            3,
            arrayOf(InstructionType.DRONE_MODIFICATION),
            mainAction = { drone, galaxy, _ ->
                drone.selectablePlanetIds = mutableListOf(drone.selectablePlanetIds
                !!.minBy { galaxy.getPlanetWithId(it)!!.attributes[PlanetAttribute.TEMPERATURE]!! }!!)
            }
    ),
    SELECT_WEAKEST(
            "Select Weakest",
            "Selects the weakest drone by attack on the planet",
            20,
            1,
            3,
            arrayOf(InstructionType.DRONE_MODIFICATION),
            mainAction = { drone, galaxy, _ ->
                drone.selectableDroneIds = mutableListOf(drone.selectableDroneIds
                !!.leastBy { galaxy.getDroneWithId(it)!!.attack.toDouble() })
            }
    ),
    RESTRICT_3(
            "Restrict 3",
            "Shortens the drone's selection queues to the first three elements",
            10,
            1,
            3,
            arrayOf(InstructionType.DRONE_MODIFICATION),
            mainAction = { drone, galaxy, _ ->
                if (drone.selectableDroneIds!!.size > 3) {
                    drone.selectableDroneIds = drone.selectableDroneIds!!.slice(0 until 3).toMutableList()
                }
                if (drone.selectablePlanetIds!!.size > 3) {
                    drone.selectablePlanetIds = drone.selectablePlanetIds!!.slice(0 until 3).toMutableList()
                }
            }
    ),
    RESET_SELECTABLES(
            "Reset Selectables",
            "Resets all of the drone's selectable planets and drones to be back to their defaults.",
            15,
            2,
            3,
            arrayOf(InstructionType.DRONE_MODIFICATION),
            mainAction = { drone, galaxy, _ ->
                drone.resetSelectables(galaxy)
            }
    ),
    MOVE_SELECTED(
            "Move Selected",
            "Moves the drone to its currently selected planet.",
            10,
            2,
            5,
            arrayOf(InstructionType.MOVEMENT),
            mainAction = { drone, galaxy, _ ->
                if (drone.selectablePlanetIds!!.isNotEmpty()) {
                    drone.moveToPlanet(drone.selectablePlanetIds!!.first(), galaxy)
                }
            }
    ),
    LOOP_3(
            "Loop 3",
            "Repeats all previous instructions 3 times.",
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
                    drone.pointer = -1
                    instance.data["counter"] = "${counter - 1}"
                }
            }
    ),
    CONSTRUCT_BASE(
            "Construct Base",
            "Constructs a base on the current planet",
            3,
            5,
            5,
            arrayOf(InstructionType.PLANET_MODIFICATION),
            mainAction = { drone, galaxy, _ ->
                val dronePlanet = galaxy.getPlanetWithId(drone.locationId)!!
                if (dronePlanet.base == null) {
                    dronePlanet.base = Base(drone.ownerId, drone.locationId, arrayOf())
                }
            }
    ),
    REPRODUCTIVE_VIRUS(
            "Reproductive Virus",
            "A virus that adds itself to the currently selected drone.",
            2,
            8,
            20,
            arrayOf(InstructionType.VIRUS),
            mainAction = { drone, galaxy, instance ->
                val selectedDrone = if (drone.selectableDroneIds!!.isNotEmpty()) galaxy.getDroneWithId(drone.selectableDroneIds!!.first()) else null
                if (selectedDrone != null && selectedDrone.memoryAvailable >= instance.baseInstruction.memorySize) {
                    selectedDrone.addInstruction(instance.baseInstruction)
                }
            }
    ),
    ATTACK_SELECTED(
            "Attack Selected",
            "Attacks the currently selected drone.",
            5,
            5,
            5,
            arrayOf(InstructionType.COMBAT),
            mainAction = { drone, galaxy, _ ->
                galaxy.getDroneWithId(if (drone.selectableDroneIds!!.isNotEmpty()) drone.selectableDroneIds!!.first() else null)?.takeDamage(drone.attack, galaxy)
            }
    ),
    ATTACK_BASE(
            "Attack Base",
            "Attacks a base on the current planet.",
            5,
            5,
            5,
            arrayOf(InstructionType.COMBAT),
            mainAction = { drone, galaxy, _ ->
                galaxy.getPlanetWithId(drone.locationId)!!.base?.takeDamage(drone.attack)
            }
    ),
    RELEASE_CFCS(
            "Release Chlorofluorocarbons",
            "Damages current planet's atmosphere and temperature.",
            3,
            8,
            5,
            arrayOf(InstructionType.PLANET_MODIFICATION),
            mainAction = { drone, galaxy, _ ->
                val currentPlanet = galaxy.getPlanetWithId(drone.locationId)!!
                currentPlanet.attributes[PlanetAttribute.TEMPERATURE] = maxOf(0.0, currentPlanet.attributes[PlanetAttribute.TEMPERATURE]!! - 0.05)
                currentPlanet.attributes[PlanetAttribute.ATMOSPHERE] = maxOf(0.0, currentPlanet.attributes[PlanetAttribute.ATMOSPHERE]!! - 0.05)
            }
    ),
    CHARGE(
            "Charge",
            "Builds up a charge within the drone. Charge in and of itself is useless, but other instructions consume charge.",
            20,
            5,
            5,
            arrayOf(InstructionType.DRONE_MODIFICATION),
            mainAction = { drone, _, _ ->
                if (drone.persistentData["charge"] != null) {
                    drone.persistentData["charge"] = "0"
                }
                drone.persistentData["charge"] = "${drone.persistentData["charge"]!!.toInt() + 1}"
            }
    ),
    HEAT_DISCHARGE(
            "Discharge",
            "Discharges all stored drone charge to damage all drones on the current planet and also heat up the planet.",
            10,
            8,
            10,
            arrayOf(InstructionType.COMBAT, InstructionType.PLANET_MODIFICATION),
            mainAction = { drone, galaxy, _ ->
                if (drone.persistentData["charge"] == null) {
                    drone.persistentData["charge"] = "0"
                }
                val charge = drone.persistentData["charge"]!!.toInt()
                galaxy.getPlanetWithId(drone.locationId)!!.drones.forEach { it.takeDamage(charge * 3, galaxy) }
                galaxy.getPlanetWithId(drone.locationId)!!.attributes[PlanetAttribute.TEMPERATURE] = minOf(galaxy.getPlanetWithId(drone.locationId)!!.attributes[PlanetAttribute.TEMPERATURE]!! + charge * 0.05, 1.0)
                drone.persistentData["charge"] = "0"
            }
    ),
    LOOP_CHARGE(
            "Loop Charge",
            "Discharges all stored charge once to loop as many times as charge was discharged.",
            5,
            5,
            10,
            arrayOf(InstructionType.ORDER),
            startCycleAction = { _, _, instance ->
                instance.data["hasDischarged"] = "false"
                instance.data["counter"] = "0"
            },
            mainAction = { drone, _, instance ->
                if (!instance.data["hasDischarged"]!!.toBoolean() && drone.persistentData["charge"] != null) {
                    instance.data["hasDischarged"] = "true"
                    instance.data["counter"] = drone.persistentData["charge"]!!
                    drone.persistentData["charge"] = "0"
                }
                val counter = instance.data["counter"]!!.toInt()
                if (counter > 0) {
                    drone.pointer = -1
                    instance.data["counter"] = "${counter - 1}"
                }
            }
    );

    override fun toString(): String = this.displayName
}

/**
 * Returns the greatest element of the given collection using the given lambda as an evaluator for value
 */
fun <T> Collection<T>.greatestBy(valueGen: (T) -> Double): T {
    return this.reduce { greatest, current -> if (valueGen(greatest) > valueGen(current)) greatest else current }
}

/**
 * Returns the smallest element of the given collection using the given lambda as an evaluator for value
 */
fun <T> Collection<T>.leastBy(valueGen: (T) -> Double): T {
    return this.reduce { least, current -> if (valueGen(least) < valueGen(current)) least else current }
}

/**
 * Returns the greatest elements of the given collection using the given lambda as an evaluator for value
 */
fun <T> Collection<T>.greatestBy(valueGen: (T) -> Double, number: Int): List<T> {
    return this.sortedBy(valueGen).slice(0 until number)
}

/**
 * Returns the smallest elements of the given collection using the given lambda as an evaluator for value
 */
fun <T> Collection<T>.leastBy(valueGen: (T) -> Double, number: Int): List<T> {
    return this.sortedByDescending(valueGen).slice(0 until number)
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