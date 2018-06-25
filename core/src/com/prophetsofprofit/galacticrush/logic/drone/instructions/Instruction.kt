package com.prophetsofprofit.galacticrush.logic.drone.instructions

import com.badlogic.gdx.graphics.g2d.Sprite
import com.prophetsofprofit.galacticrush.logic.drone.Drone

/**
 * Each instruction has one (or, eventually, more) types which determine how it's achieved and what it does
 * At the start of the game, each player drafts one of each type of instruction blueprint
 */
enum class InstructionType {
    NONE,           //Miscellaneous
    MOVEMENT,       //Instructs the drone to travel along cosmic highways
    COMBAT,         //Instructs the drone to initiate combat or do something combat-related
    CONSTRUCTION,   //Instructs the drone to modify the world or construct facilities on planets
    MINING,         //Instructs the drone to gather resources from a planet
    UPGRADE,        //Modifies the properties of a drone
    MODIFIER        //Modifies the properties of an instruction
}

/**
 * An enumeration for instructions
 * Contains all of the instructions as well as how they look and what they do
 */
enum class Instruction(val icon: Sprite,            //The icon of the instruction
                       val action: (Drone) -> Unit, //What the instruction tells the drone to do every turn
                       val memory: Int,             //How much memory the instruction takes up
                       val type: InstructionType,   //What kind of instruction it is
                       var health: Int              //How much health this component has, which affects the drone's survivabiliy
) {
    //TODO: a temporary example for how to construct an instruction: treat this as a compile-time list
    NOTHING(Sprite(), {}, 0, InstructionType.NONE, 0)
}