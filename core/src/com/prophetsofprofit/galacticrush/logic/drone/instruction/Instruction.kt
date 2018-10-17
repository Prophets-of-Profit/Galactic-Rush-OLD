package com.prophetsofprofit.galacticrush.logic.drone.instruction

import com.prophetsofprofit.galacticrush.logic.Game
import com.prophetsofprofit.galacticrush.logic.base.Base
import com.prophetsofprofit.galacticrush.logic.base.Facility
import com.prophetsofprofit.galacticrush.logic.drone.Drone
import com.prophetsofprofit.galacticrush.logic.map.PlanetAttribute

//Utility alias for calling (Drone, Galaxy) -> Unit a DroneAction
typealias DroneAction = (Drone, Game, InstructionInstance) -> Unit

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
        val occurrenceAmount: Int,
        val cost: Int,
        val memorySize: Int,
        val health: Int,
        val types: Array<InstructionType>,
        val addAction: DroneAction = { _, _, _ -> },
        val removeAction: DroneAction = { _, _, _ -> },
        val startCycleAction: DroneAction = { _, _, _ -> },
        val mainAction: DroneAction = { _, _, _ -> },
        val endCycleAction: DroneAction = { _, _, _ -> }
) {
    NONE("None", "THIS IS INVALID", 0, 0, 100000, 100000, arrayOf()),
    DAMAGED_MEMORY(
            "Damaged Memory",
            "A sector of bad memory that crashes the drone",
            0,
            -10,
            1,
            5,
            arrayOf(InstructionType.INSTRUCTION_MODIFICATION),
            mainAction = { drone, _, _ ->
                drone.advancePointer(drone.instructions.size)
            }
    ),
    CORRUPT_FIRST(
            "Corrupt First",
            "Corrupts the memory for the selected drone's first instruction",
            3,
            100,
            4,
            8,
            arrayOf(InstructionType.INSTRUCTION_MODIFICATION),
            mainAction = { drone, game, _ ->
                val selectedDrone = game.galaxy.getDroneWithId(drone.selectableDroneIds?.first())
                selectedDrone?.removeInstruction(0, game)
                selectedDrone?.addInstruction(if (Math.random() < 0.05) Instruction.values().toList().shuffled().first() else Instruction.DAMAGED_MEMORY, 0)
            }
    ),
    IF_HIGH_MASS(
            "If High Mass",
            "Makes the next instruction only trigger when the current planet has a high mass.",
            3,
            10,
            1,
            3,
            arrayOf(InstructionType.ORDER),
            mainAction = { drone, game, _ ->
                //If planet isn't massive enough, move the pointer to skip the next instruction if any
                if (game.galaxy.getPlanetWithId(drone.locationId)!!.attributes[PlanetAttribute.MASS]!! < 0.75) {
                    drone.advancePointer(1)
                }
            }
    ),
    IF_LOW_MASS(
            "If Low Mass",
            "Makes the next instruction only trigger when the current planet has a low mass.",
            3,
            10,
            1,
            3,
            arrayOf(InstructionType.ORDER),
            mainAction = { drone, game, _ ->
                if (game.galaxy.getPlanetWithId(drone.locationId)!!.attributes[PlanetAttribute.MASS]!! > 0.25) {
                    drone.advancePointer(1)
                }
            }
    ),
    IF_HIGH_TEMPERATURE(
            "If High Temperature",
            "Makes the next instruction only trigger when the current planet has a high temperature.",
            3,
            10,
            1,
            3,
            arrayOf(InstructionType.ORDER),
            mainAction = { drone, game, _ ->
                if (game.galaxy.getPlanetWithId(drone.locationId)!!.attributes[PlanetAttribute.TEMPERATURE]!! < 0.75) {
                    drone.advancePointer(1)
                }
            }
    ),
    IF_LOW_TEMPERATURE(
            "If Low Temperature",
            "Makes the next instruction only trigger when the current planet has a low temperature.",
            3,
            10,
            1,
            3,
            arrayOf(InstructionType.ORDER),
            mainAction = { drone, game, _ ->
                if (game.galaxy.getPlanetWithId(drone.locationId)!!.attributes[PlanetAttribute.TEMPERATURE]!! > 0.25) {
                    drone.advancePointer(1)
                }
            }
    ),
    IF_HIGH_PRESSURE(
            "If High Pressure",
            "Makes the next instruction only trigger when the current planet has a high atmospheric pressure.",
            3,
            10,
            1,
            3,
            arrayOf(InstructionType.ORDER),
            mainAction = { drone, game, _ ->
                if (game.galaxy.getPlanetWithId(drone.locationId)!!.attributes[PlanetAttribute.ATMOSPHERE]!! < 0.75) {
                    drone.advancePointer(1)
                }
            }
    ),
    IF_LOW_PRESSURE(
            "If Hot",
            "Makes the next instruction only trigger when the current planet has a low pressure.",
            3,
            10,
            1,
            3,
            arrayOf(InstructionType.ORDER),
            mainAction = { drone, game, _ ->
                if (game.galaxy.getPlanetWithId(drone.locationId)!!.attributes[PlanetAttribute.ATMOSPHERE]!! > 0.25) {
                    drone.advancePointer(1)
                }
            }
    ),
    IF_HIGH_HUMIDITY(
            "If High Humidity",
            "Makes the next instruction only trigger when the current planet has a high humidity.",
            3,
            10,
            1,
            3,
            arrayOf(InstructionType.ORDER),
            mainAction = { drone, game, _ ->
                if (game.galaxy.getPlanetWithId(drone.locationId)!!.attributes[PlanetAttribute.WATER]!! < 0.75) {
                    drone.advancePointer(1)
                }
            }
    ),
    IF_LOW_HUMIDITY(
            "If Low Humidity",
            "Makes the next instruction only trigger when the current planet has a low humidity.",
            3,
            10,
            1,
            3,
            arrayOf(InstructionType.ORDER),
            mainAction = { drone, game, _ ->
                if (game.galaxy.getPlanetWithId(drone.locationId)!!.attributes[PlanetAttribute.WATER]!! > 0.25) {
                    drone.advancePointer(1)
                }
            }
    ),
    IF_HIGH_SOLIDITY(
            "If High Solidity",
            "Makes the next instruction only trigger when the current planet has a high solidity.",
            3,
            10,
            1,
            3,
            arrayOf(InstructionType.ORDER),
            mainAction = { drone, game, _ ->
                if (game.galaxy.getPlanetWithId(drone.locationId)!!.attributes[PlanetAttribute.SOLIDITY]!! < 0.75) {
                    drone.advancePointer(1)
                }
            }
    ),
    IF_LOW_SOLIDITY(
            "If Low Solidity",
            "Makes the next instruction only trigger when the current planet has a low solidity.",
            3,
            10,
            1,
            3,
            arrayOf(InstructionType.ORDER),
            mainAction = { drone, game, _ ->
                if (game.galaxy.getPlanetWithId(drone.locationId)!!.attributes[PlanetAttribute.SOLIDITY]!! > 0.25) {
                    drone.advancePointer(1)
                }
            }
    ),
    ORDER_HIGH_MASS(
            "Order High Mass",
            "Orders the drone's planet selection queue by decreasing mass.",
            5,
            50,
            2,
            2,
            arrayOf(InstructionType.INSTRUCTION_MODIFICATION),
            mainAction = { drone, game, _ ->
                drone.selectablePlanetIds = mutableListOf(drone.selectablePlanetIds
                !!.greatestBy { game.galaxy.getPlanetWithId(it)!!.attributes[PlanetAttribute.MASS]!! })
            }
    ),
    ORDER_LOW_MASS(
            "Order Low Mass",
            "Orders the drone's planet selection queue by increasing mass.",
            5,
            50,
            2,
            2,
            arrayOf(InstructionType.INSTRUCTION_MODIFICATION),
            mainAction = { drone, game, _ ->
                drone.selectablePlanetIds = mutableListOf(drone.selectablePlanetIds
                !!.leastBy { game.galaxy.getPlanetWithId(it)!!.attributes[PlanetAttribute.MASS]!! })
            }
    ),

    ORDER_HIGH_TEMPERATURE(
            "Order High Temperature",
            "Orders the drone's planet selection queue by decreasing temperature.",
            5,
            50,
            2,
            2,
            arrayOf(InstructionType.INSTRUCTION_MODIFICATION),
            mainAction = { drone, game, _ ->
                drone.selectablePlanetIds = mutableListOf(drone.selectablePlanetIds
                !!.greatestBy { game.galaxy.getPlanetWithId(it)!!.attributes[PlanetAttribute.TEMPERATURE]!! })
            }
    ),
    ORDER_LOW_TEMPERATURE(
            "Order Low Temperature",
            "Orders the drone's planet selection queue by increasing temperature.",
            5,
            50,
            2,
            2,
            arrayOf(InstructionType.INSTRUCTION_MODIFICATION),
            mainAction = { drone, game, _ ->
                drone.selectablePlanetIds = mutableListOf(drone.selectablePlanetIds
                !!.leastBy { game.galaxy.getPlanetWithId(it)!!.attributes[PlanetAttribute.TEMPERATURE]!! })
            }
    ),
    ORDER_HIGH_PRESSURE(
            "Order High Pressure",
            "Orders the drone's planet selection queue by decreasing pressure.",
            5,
            50,
            2,
            2,
            arrayOf(InstructionType.INSTRUCTION_MODIFICATION),
            mainAction = { drone, game, _ ->
                drone.selectablePlanetIds = mutableListOf(drone.selectablePlanetIds
                !!.greatestBy { game.galaxy.getPlanetWithId(it)!!.attributes[PlanetAttribute.ATMOSPHERE]!! })
            }
    ),
    ORDER_LOW_PRESSURE(
            "Order Low Pressure",
            "Orders the drone's planet selection queue by increasing pressure.",
            5,
            50,
            2,
            2,
            arrayOf(InstructionType.INSTRUCTION_MODIFICATION),
            mainAction = { drone, game, _ ->
                drone.selectablePlanetIds = mutableListOf(drone.selectablePlanetIds
                !!.leastBy { game.galaxy.getPlanetWithId(it)!!.attributes[PlanetAttribute.ATMOSPHERE]!! })
            }
    ),
    ORDER_HIGH_HUMIDITY(
            "Order High Humidity",
            "Orders the drone's planet selection queue by decreasing humidity.",
            5,
            50,
            2,
            2,
            arrayOf(InstructionType.INSTRUCTION_MODIFICATION),
            mainAction = { drone, game, _ ->
                drone.selectablePlanetIds = mutableListOf(drone.selectablePlanetIds
                !!.greatestBy { game.galaxy.getPlanetWithId(it)!!.attributes[PlanetAttribute.WATER]!! })
            }
    ),
    ORDER_LOW_HUMIDITY(
            "Order Low Humidity",
            "Orders the drone's planet selection queue by increasing humidity.",
            5,
            50,
            2,
            2,
            arrayOf(InstructionType.INSTRUCTION_MODIFICATION),
            mainAction = { drone, game, _ ->
                drone.selectablePlanetIds = mutableListOf(drone.selectablePlanetIds
                !!.leastBy { game.galaxy.getPlanetWithId(it)!!.attributes[PlanetAttribute.WATER]!! })
            }
    ),
    ORDER_HIGH_SOLIDITY(
            "Order High Solidity",
            "Orders the drone's planet selection queue by decreasing solidity.",
            5,
            50,
            2,
            2,
            arrayOf(InstructionType.INSTRUCTION_MODIFICATION),
            mainAction = { drone, game, _ ->
                drone.selectablePlanetIds = mutableListOf(drone.selectablePlanetIds
                !!.greatestBy { game.galaxy.getPlanetWithId(it)!!.attributes[PlanetAttribute.SOLIDITY]!! })
            }
    ),
    ORDER_LOW_SOLIDITY(
            "Order Low Solidity",
            "Orders the drone's planet selection queue by increasing solidity.",
            5,
            50,
            2,
            2,
            arrayOf(InstructionType.INSTRUCTION_MODIFICATION),
            mainAction = { drone, game, _ ->
                drone.selectablePlanetIds = mutableListOf(drone.selectablePlanetIds
                !!.leastBy { game.galaxy.getPlanetWithId(it)!!.attributes[PlanetAttribute.SOLIDITY]!! })
            }
    ),
    REVERSE_PLANET_QUEUE(
            "Reverse Planet Queue",
            "Reverses the order in which planets are prioritized for selection.",
            3,
            50,
            2,
            2,
            arrayOf(InstructionType.INSTRUCTION_MODIFICATION),
            mainAction = { drone, _, _ ->
                drone.selectablePlanetIds!!.reverse()
            }
    ),
    REVERSE_DRONE_QUEUE(
            "Reverse Drone Queue",
            "Reverses the order in which drones are prioritized for selection.",
            3,
            50,
            2,
            2,
            arrayOf(InstructionType.INSTRUCTION_MODIFICATION),
            mainAction = { drone, _, _ ->
                drone.selectableDroneIds!!.reverse()
            }
    ),
    REVERSE_SELECTION_QUEUES(
            "Reverse Selection Queues",
            "Reverses the orders in which drones and planets are prioritized for selection.",
            2,
            50,
            3,
            2,
            arrayOf(InstructionType.INSTRUCTION_MODIFICATION),
            mainAction = { drone, _, _ ->
                drone.selectableDroneIds!!.reverse()
                drone.selectablePlanetIds!!.reverse()
            }
    ),
    SELECT_HIGHEST_MASS(
            "Select Highest Mass",
            "Restricts the drone's planet selection queue to the available planet with the highest mass.",
            6,
            20,
            1,
            5,
            arrayOf(InstructionType.INSTRUCTION_MODIFICATION),
            mainAction = { drone, game, _ ->
                drone.selectablePlanetIds = mutableListOf(drone.selectablePlanetIds
                !!.maxBy { game.galaxy.getPlanetWithId(it)!!.attributes[PlanetAttribute.MASS]!! }!!)
            }
    ),
    SELECT_LOWEST_MASS(
            "Select Lowest Mass",
            "Restricts the drone's planet selection queue to the available planet with the lowest mass.",
            6,
            20,
            1,
            5,
            arrayOf(InstructionType.INSTRUCTION_MODIFICATION),
            mainAction = { drone, game, _ ->
                drone.selectablePlanetIds = mutableListOf(drone.selectablePlanetIds
                !!.minBy { game.galaxy.getPlanetWithId(it)!!.attributes[PlanetAttribute.MASS]!! }!!)
            }
    ),
    SELECT_HIGHEST_TEMPERATURE(
            "Select Highest Temperature",
            "Restricts the drone's planet selection queue to the available planet with the highest temperature.",
            6,
            20,
            1,
            5,
            arrayOf(InstructionType.INSTRUCTION_MODIFICATION),
            mainAction = { drone, game, _ ->
                drone.selectablePlanetIds = mutableListOf(drone.selectablePlanetIds
                !!.maxBy { game.galaxy.getPlanetWithId(it)!!.attributes[PlanetAttribute.TEMPERATURE]!! }!!)
            }
    ),
    SELECT_LOWEST_TEMPERATURE(
            "Select Lowest Temperature",
            "Restricts the drone's planet selection queue to the available planet with the lowest temperature.",
            6,
            20,
            1,
            5,
            arrayOf(InstructionType.INSTRUCTION_MODIFICATION),
            mainAction = { drone, game, _ ->
                drone.selectablePlanetIds = mutableListOf(drone.selectablePlanetIds
                !!.minBy { game.galaxy.getPlanetWithId(it)!!.attributes[PlanetAttribute.TEMPERATURE]!! }!!)
            }
    ),
    SELECT_HIGHEST_PRESSURE(
            "Select Highest Pressure",
            "Restricts the drone's planet selection queue to the available planet with the highest pressure.",
            6,
            20,
            1,
            5,
            arrayOf(InstructionType.INSTRUCTION_MODIFICATION),
            mainAction = { drone, game, _ ->
                drone.selectablePlanetIds = mutableListOf(drone.selectablePlanetIds
                !!.maxBy { game.galaxy.getPlanetWithId(it)!!.attributes[PlanetAttribute.ATMOSPHERE]!! }!!)
            }
    ),
    SELECT_LOWEST_PRESSURE(
            "Select Lowest Pressure",
            "Restricts the drone's planet selection queue to the available planet with the lowest pressure.",
            6,
            20,
            1,
            5,
            arrayOf(InstructionType.INSTRUCTION_MODIFICATION),
            mainAction = { drone, game, _ ->
                drone.selectablePlanetIds = mutableListOf(drone.selectablePlanetIds
                !!.minBy { game.galaxy.getPlanetWithId(it)!!.attributes[PlanetAttribute.ATMOSPHERE]!! }!!)
            }
    ),
    SELECT_HIGHEST_HUMIDITY(
            "Select Highest Humidity",
            "Restricts the drone's planet selection queue to the available planet with the highest humidity.",
            6,
            20,
            1,
            5,
            arrayOf(InstructionType.INSTRUCTION_MODIFICATION),
            mainAction = { drone, game, _ ->
                drone.selectablePlanetIds = mutableListOf(drone.selectablePlanetIds
                !!.maxBy { game.galaxy.getPlanetWithId(it)!!.attributes[PlanetAttribute.WATER]!! }!!)
            }
    ),
    SELECT_LOWEST_HUMIDITY(
            "Select Lowest Humidity",
            "Restricts the drone's planet selection queue to the available planet with the lowest humidity.",
            6,
            20,
            1,
            5,
            arrayOf(InstructionType.INSTRUCTION_MODIFICATION),
            mainAction = { drone, game, _ ->
                drone.selectablePlanetIds = mutableListOf(drone.selectablePlanetIds
                !!.minBy { game.galaxy.getPlanetWithId(it)!!.attributes[PlanetAttribute.WATER]!! }!!)
            }
    ),
    SELECT_HIGHEST_SOLIDITY(
            "Select Highest Solidity",
            "Restricts the drone's planet selection queue to the available planet with the highest solidity.",
            6,
            20,
            1,
            5,
            arrayOf(InstructionType.INSTRUCTION_MODIFICATION),
            mainAction = { drone, game, _ ->
                drone.selectablePlanetIds = mutableListOf(drone.selectablePlanetIds
                !!.maxBy { game.galaxy.getPlanetWithId(it)!!.attributes[PlanetAttribute.SOLIDITY]!! }!!)
            }
    ),
    SELECT_LOWEST_SOLIDITY(
            "Select Lowest Solidity",
            "Restricts the drone's planet selection queue to the available planet with the lowest solidity.",
            6,
            20,
            1,
            5,
            arrayOf(InstructionType.INSTRUCTION_MODIFICATION),
            mainAction = { drone, game, _ ->
                drone.selectablePlanetIds = mutableListOf(drone.selectablePlanetIds
                !!.minBy { game.galaxy.getPlanetWithId(it)!!.attributes[PlanetAttribute.SOLIDITY]!! }!!)
            }
    ),
    SELECT_WEAKEST(
            "Select Weakest",
            "Selects the weakest drone by attack on the planet",
            6,
            20,
            1,
            3,
            arrayOf(InstructionType.INSTRUCTION_MODIFICATION),
            mainAction = { drone, game, _ ->
                drone.selectableDroneIds = mutableListOf(drone.selectableDroneIds
                !!.leastBy { game.galaxy.getDroneWithId(it)!!.attack.toDouble() })
            }
    ),
    SELECT_MOST_VALUABLE(
            "Select Most Valuable",
            "Selects the most valuable drone on this planet",
            6,
            20,
            1,
            3,
            arrayOf(InstructionType.INSTRUCTION_MODIFICATION),
            mainAction = { drone, game, _ ->
                drone.selectableDroneIds = mutableListOf(drone.selectableDroneIds
                !!.leastBy { game.galaxy.getDroneWithId(it)!!.instructions.sumBy { instance -> instance.baseInstruction.cost }.toDouble() })
            }
    ),
    RESTRICT_3(
            "Restrict 3",
            "Shortens the drone's selection queues to the first three elements",
            3,
            50,
            2,
            2,
            arrayOf(InstructionType.INSTRUCTION_MODIFICATION),
            mainAction = { drone, _, _ ->
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
            6,
            30,
            1,
            3,
            arrayOf(InstructionType.INSTRUCTION_MODIFICATION),
            mainAction = { drone, game, _ ->
                drone.resetSelectables(game)
            }
    ),
    MOVE_SELECTED(
            "Move Selected",
            "Moves the drone to its currently selected planet.",
            6,
            100,
            2,
            5,
            arrayOf(InstructionType.MOVEMENT),
            mainAction = { drone, game, _ ->
                if (drone.selectablePlanetIds!!.isNotEmpty()) {
                    drone.moveToPlanet(drone.selectablePlanetIds!!.first(), game)
                }
            }
    ),
    LOOP_3(
            "Loop 3",
            "Repeats all previous instructions 3 times.",
            2,
            500,
            5,
            2,
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
            1000,
            5,
            3,
            arrayOf(InstructionType.PLANET_MODIFICATION),
            mainAction = { drone, game, _ ->
                val dronePlanet = game.galaxy.getPlanetWithId(drone.locationId)!!
                if (dronePlanet.base == null) {
                    dronePlanet.base = Base(drone.ownerId, drone.locationId, arrayOf(Facility.BASE))
                }
            }
    ),
    REPRODUCTIVE_VIRUS(
            "Reproductive Virus",
            "A virus that adds itself to the currently selected drone.",
            2,
            50,
            4,
            5,
            arrayOf(InstructionType.VIRUS),
            mainAction = { drone, game, instance ->
                val selectedDrone = if (drone.selectableDroneIds!!.isNotEmpty()) game.galaxy.getDroneWithId(drone.selectableDroneIds!!.first()) else null
                if (selectedDrone != null && selectedDrone.memoryAvailable >= instance.baseInstruction.memorySize) {
                    selectedDrone.addInstruction(instance.baseInstruction)
                }
            }
    ),
    ATTACK_SELECTED(
            "Attack Selected",
            "Attacks the currently selected drone.",
            6,
            100,
            3,
            4,
            arrayOf(InstructionType.COMBAT),
            mainAction = { drone, game, _ ->
                game.galaxy.getDroneWithId(if (drone.selectableDroneIds!!.isNotEmpty()) drone.selectableDroneIds!!.first() else null)?.takeDamage(drone.attack, game)
            }
    ),
    ATTACK_BASE(
            "Attack Base",
            "Attacks a base on the current planet.",
            6,
            100,
            3,
            4,
            arrayOf(InstructionType.COMBAT),
            mainAction = { drone, game, _ ->
                game.galaxy.getPlanetWithId(drone.locationId)!!.base?.takeDamage(drone.attack)
            }
    ),
    RELEASE_CFCS(
            "Release Chlorofluorocarbons",
            "Damages current planet's atmosphere and temperature.",
            3,
            50,
            2,
            3,
            arrayOf(InstructionType.PLANET_MODIFICATION),
            mainAction = { drone, game, _ ->
                val currentPlanet = game.galaxy.getPlanetWithId(drone.locationId)!!
                currentPlanet.attributes[PlanetAttribute.TEMPERATURE] = maxOf(0.0, currentPlanet.attributes[PlanetAttribute.TEMPERATURE]!! - 0.05)
                currentPlanet.attributes[PlanetAttribute.ATMOSPHERE] = maxOf(0.0, currentPlanet.attributes[PlanetAttribute.ATMOSPHERE]!! - 0.05)
            }
    ),
    CHARGE(
            "Charge",
            "Builds up a charge within the drone. Charge in and of itself is useless, but other instructions consume charge.",
            4,
            40,
            1,
            2,
            arrayOf(InstructionType.INSTRUCTION_MODIFICATION),
            mainAction = { drone, _, _ ->
                if (drone.persistentData["charge"] == null) {
                    drone.persistentData["charge"] = "0"
                }
                drone.persistentData["charge"] = "${drone.persistentData["charge"]!!.toInt() + 1}"
            }
    ),
    HEAT_DISCHARGE(
            "Discharge",
            "Discharges all stored drone charge to damage all drones on the current planet and also heat up the planet.",
            3,
            70,
            3,
            3,
            arrayOf(InstructionType.COMBAT, InstructionType.PLANET_MODIFICATION),
            mainAction = { drone, game, _ ->
                if (drone.persistentData["charge"] == null) {
                    drone.persistentData["charge"] = "0"
                }
                val charge = drone.persistentData["charge"]!!.toInt()
                game.galaxy.getPlanetWithId(drone.locationId)!!.drones.forEach { it.takeDamage(charge * 3, game) }
                game.galaxy.getPlanetWithId(drone.locationId)!!.attributes[PlanetAttribute.TEMPERATURE] = minOf(game.galaxy.getPlanetWithId(drone.locationId)!!.attributes[PlanetAttribute.TEMPERATURE]!! + charge * 0.05, 1.0)
                drone.persistentData["charge"] = "0"
            }
    ),
    LOOP_CHARGE(
            "Loop Charge",
            "Discharges all stored charge once to loop as many times as charge was discharged.",
            2,
            700,
            6,
            1,
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
    ),
    ENERGIZE(
            "Energize",
            "Releases all stored charge to heal all drones on the current planet.",
            3,
            70,
            3,
            3,
            arrayOf(InstructionType.INSTRUCTION_MODIFICATION),
            mainAction = { drone, game, _ ->
                val charge = drone.persistentData["charge"]?.toInt() ?: 0
                //TODO: does below line work?
                game.galaxy.getPlanetWithId(drone.locationId)!!.drones.forEach { it.takeDamage(-charge * 3, game) }
                drone.persistentData["charge"] = "0"
            }
    ),
    COLLECT(
            "Collect",
            "Pops the first loot item from the planet.",
            6,
            150,
            2,
            5,
            arrayOf(InstructionType.MINING),
            mainAction = { drone, game, _ ->
                game.galaxy.getPlanetWithId(drone.locationId)!!.loot.firstOrNull()?.getDiscovered(drone.ownerId, drone, game)
            }
    ),
    SELL_CHARGE(
            "Sell Charge",
            "Sells all stored charge for money.",
            5,
            200,
            2,
            4,
            arrayOf(InstructionType.INSTRUCTION_MODIFICATION),
            mainAction = { drone, game, _ ->
                game.money[drone.ownerId] = game.money[drone.ownerId]!! + (drone.persistentData["charge"]?.toInt()
                        ?: 0) * 50
                drone.persistentData["charge"] = "0"
            }
    ),
    BUFF_CHARGE(
            "Buff Charge",
            "Discharges all stored charge to strengthen the drone for the rest of the turn.",
            3,
            100,
            4,
            8,
            arrayOf(InstructionType.INSTRUCTION_MODIFICATION),
            mainAction = { drone, _, instance ->
                val buffAmount = drone.persistentData["charge"]?.toInt() ?: 0
                drone.persistentData["charge"] = "0"
                instance.data["buffAmount"] = "$buffAmount"
                drone.attack += buffAmount
            },
            endCycleAction = { drone, _, instance ->
                drone.attack -= instance.data["buffAmount"]!!.toInt()
                instance.data["buffAmount"] = "0"
            },
            removeAction = { drone, _, instance ->
                drone.attack -= instance.data["buffAmount"]?.toInt() ?: 0
                instance.data["buffAmount"] = "0"
            }
    ),
    BUFF_2(
            "Buff Attack",
            "Strengthens the drone by 2 attack for the rest of the turn.",
            2,
            50,
            3,
            5,
            arrayOf(InstructionType.INSTRUCTION_MODIFICATION),
            startCycleAction = { _, _, instance ->
                instance.data["buffAmount"] = "0"
            },
            mainAction = { drone, _, instance ->
                drone.attack += 2
                instance.data["buffAmount"] = "2"
            },
            endCycleAction = { drone, _, instance ->
                drone.attack -= instance.data["buffAmount"]?.toInt() ?: 0
                instance.data["buffAmount"] = "0"
            },
            removeAction = { drone, _, instance ->
                drone.attack -= instance.data["buffAmount"]?.toInt() ?: 0
                instance.data["buffAmount"] = "0"
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