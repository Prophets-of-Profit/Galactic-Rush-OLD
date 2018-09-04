package com.prophetsofprofit.galacticrush.graphics.screen.maingame.instructiondisplays

import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.prophetsofprofit.galacticrush.logic.drone.instruction.InstructionInstance

/**
 * Displays the instructions in a scrollable pane where each instruction has the same width
 */
class InstructionInventoryDisplay(w: Float, h: Float, var rowLength: Int, var length: Int, var instructions: MutableList<InstructionInstance>) : Table() {

    //The number of rows the queue will display
    val rowNum: Int
        get() = Math.ceil(this.length.toDouble() / this.rowLength).toInt()
    //The size represented by a single unit of memory
    //Each instruction has height = cellSize and width = cellSize * memory
    val cellSize: Float
        get() = minOf(this.width / this.rowLength, this.height / this.rowNum)

    init {
        this.height = h
        this.width = w
    }

    /**
     * Updates the elements of the inventory
     */
    fun update() {
        this.clearChildren()
        this.instructions.forEach { _ -> Unit }
    }

}
