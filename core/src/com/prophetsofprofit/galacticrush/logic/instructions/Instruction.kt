package com.prophetsofprofit.galacticrush.logic.instructions

import com.badlogic.gdx.graphics.g2d.Sprite
import com.prophetsofprofit.galacticrush.logic.Drone

/**
 * An enumeration for instructions
 * Contains all of the instructions as well as how they look and what they do
 */
enum class Instruction(val icon: Sprite, val action: (Drone) -> Unit) {
    //TODO: a temporary example for how to construct an instruction: treat this as a compile-time list
    NOTHING(Sprite(), {})
}