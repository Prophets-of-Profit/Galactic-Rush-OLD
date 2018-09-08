package com.prophetsofprofit.galacticrush.graphics.screen.maingame.instructiondisplays

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.MainGameScreen
import com.prophetsofprofit.galacticrush.instructionTextures
import com.prophetsofprofit.galacticrush.logic.drone.instruction.Instruction
import com.prophetsofprofit.galacticrush.logic.drone.instruction.InstructionInstance

/**
 * Displays the instructions in a scrollable pane where each instruction has the same width
 */
class InstructionInventoryDisplay(var rowLength: Int, val gameScreen: MainGameScreen, var action: (Instruction) -> Unit) : Table() {

    //The instructions to display
    val instructions: MutableList<Instruction>
        get() = this.gameScreen.mainGame.unlockedInstructions[gameScreen.player.id]!!.toMutableList()
    //The number of rows the queue will display
    val rowNum: Int
        get() = Math.ceil(this.instructions.size.toDouble() / this.rowLength).toInt()
    //The size represented by a single unit of memory
    //Each instruction has height = cellSize and width = cellSize * memory
    val cellSize: Float
        get() = minOf(this.width / this.rowLength, this.height / this.rowNum)

    init {
        this.update()
        this.width = 100f
        this.height = 100f
    }

    /**
     * Updates the elements of the inventory
     */
    fun update() {
        this.height = this.cellSize * this.rowNum
        this.clearChildren()
        for (i in 0 until this.instructions.size) {
            var newCell = this.add(Button(TextureRegionDrawable(TextureRegion(instructionTextures[this.instructions[i]]))).also {
                it.addListener(object : ChangeListener() {
                    override fun changed(event: ChangeEvent?, actor: Actor?) {
                        action(instructions.toMutableList()[i])
                        update()
                    }
                })
            }).size(this.cellSize, this.cellSize)
            if ((i + 1) % this.rowLength == 0) newCell.row()
        }
    }

}
