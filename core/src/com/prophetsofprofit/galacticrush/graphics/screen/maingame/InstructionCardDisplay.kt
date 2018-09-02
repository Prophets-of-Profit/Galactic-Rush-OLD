package com.prophetsofprofit.galacticrush.graphics.screen.maingame

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.prophetsofprofit.galacticrush.instructionTextures
import com.prophetsofprofit.galacticrush.logic.drone.instruction.Instruction
import ktx.scene2d.Scene2DSkin

/**
 * A class that shows the instruction like a card
 */
class InstructionCardDisplay(val instruction: Instruction) : Table() {

    //Puts and aligns all of the instruction info inside this table
    init {
        this.add(Image(TextureRegionDrawable(TextureRegion(instructionTextures[this.instruction]!!)))).expand().fill().row()
        this.add(Label(this.instruction.displayName, Scene2DSkin.defaultSkin)).expandX().fillX().pad(5f).row()
        this.add(Label(this.instruction.displayDescription, Scene2DSkin.defaultSkin)).expandX().fillX()
    }

}