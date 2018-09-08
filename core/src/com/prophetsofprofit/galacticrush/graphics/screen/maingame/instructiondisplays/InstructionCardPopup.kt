package com.prophetsofprofit.galacticrush.graphics.screen.maingame.instructiondisplays

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.prophetsofprofit.galacticrush.graphics.Direction
import com.prophetsofprofit.galacticrush.graphics.ModalWindow
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.MainGameScreen
import com.prophetsofprofit.galacticrush.logic.drone.instruction.Instruction
import ktx.scene2d.Scene2DSkin

/**
 * Displays a single instruction that does something when clicked and TODO disappears otherwise
 */
class InstructionCardPopup(gameScreen: MainGameScreen, val instruction: Instruction, val clicked: () -> Unit) : ModalWindow(gameScreen, "Draft Options", gameScreen.uiCamera.viewportWidth / 4, gameScreen.uiCamera.viewportHeight / 2) {

    /**
     * Initializes the popup
     */
    init {
        this.add(Button(InstructionCardDisplay(instruction), Scene2DSkin.defaultSkin).also {
            it.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent, actor: Actor) {
                    clicked()
                    disappear(Direction.POP, 0.1f)
                }
            })
            //TODO: Maintain aspect ratio
        }).expand().fill().pad(5f)
        this.appear(Direction.POP, 0.1f)
    }

}
