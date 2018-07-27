package com.prophetsofprofit.galacticrush.graphics.screen.maingame

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import ktx.scene2d.Scene2DSkin

/**
 * A class that represents a section of UI elements
 */
open class Panel(val screen: MainGameScreen, title: String, width: Float, height: Float, x: Float = screen.uiCamera.viewportWidth / 2, y: Float = screen.uiCamera.viewportHeight / 2, align: Int = Align.center): Group() {

    //The base label that is the background for the panel
    val baseLabel = Label(title, Scene2DSkin.defaultSkin, "ui")
    //How tall the title of the panel is
    val verticalTextOffset: Float

    /**
     * Initializes the panel background as a label
     */
    init {
        this.verticalTextOffset = baseLabel.height
        baseLabel.setAlignment(Align.top)
        baseLabel.width = width
        baseLabel.height = height
        baseLabel.setPosition(x, y, align)
        this.addActor(baseLabel)
    }

}