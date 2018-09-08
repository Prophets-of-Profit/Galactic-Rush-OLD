package com.prophetsofprofit.galacticrush.graphics.screen.maingame.instructiondisplays

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.prophetsofprofit.galacticrush.instructionTextures
import com.prophetsofprofit.galacticrush.logic.drone.instruction.InstructionInstance

/**
 * Displays the instructions in a line where each instruction has a width corresponding to its memory
 */
class DroneQueueDisplay(var length: Int, var instructions: MutableList<InstructionInstance>) : Table() {

    init {
        this.update()
    }

    /**
     * Updates the elements of the inventory
     */
    fun update() {
        this.width = this.height * length
        this.clearChildren()
        this.instructions.forEach { instructionInstance ->
            //Add a button with the instruction's texture
            this.add(Button(TextureRegionDrawable(TextureRegion(instructionTextures[instructionInstance.baseInstruction]))).also {
                it.addListener(object : ChangeListener() {
                    override fun changed(event: ChangeEvent?, actor: Actor?) {
                        instructions.remove(instructionInstance)
                        update()
                    }
                })
            }).size(this.height * instructionInstance.baseInstruction.memorySize, this.height)
        }
    }


}
