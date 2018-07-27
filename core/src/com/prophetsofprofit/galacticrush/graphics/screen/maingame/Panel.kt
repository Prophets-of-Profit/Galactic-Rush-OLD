package com.prophetsofprofit.galacticrush.graphics.screen.maingame

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import ktx.scene2d.Scene2DSkin

/**
 * A class that represents a section of UI elements
 */
open class Panel(val screen: MainGameScreen, title: String, width: Float, height: Float, x: Float = screen.uiCamera.viewportWidth / 2, y: Float = screen.uiCamera.viewportHeight / 2, align: Int = Align.center): Group() {

    //How tall the title of the panel is
    val textOffset: Float

    /**
     * Initializes the panel background as a label
     */
    init {
        val windowLabel = Label(title, Scene2DSkin.defaultSkin, "ui")
        this.textOffset = windowLabel.height
        windowLabel.setAlignment(Align.top)
        windowLabel.width = width
        windowLabel.height = height
        windowLabel.setPosition(x, y, align)
        this.addActor(windowLabel)
    }

}