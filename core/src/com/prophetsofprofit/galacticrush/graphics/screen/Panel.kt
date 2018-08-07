package com.prophetsofprofit.galacticrush.graphics.screen

import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.utils.Align
import ktx.scene2d.Scene2DSkin

/**
 * A class that represents a fixed-place or static panel that doesn't move outside of animations
 */
open class Panel(val screen: GalacticRushScreen, title: String, x: Float, y: Float, w: Float, h: Float, align: Int = Align.bottomLeft) : Window(title, Scene2DSkin.defaultSkin) {

    /**
     * Initializes the Window as an immovable panel
     */
    init {
        this.style.stageBackground = null
        this.setSize(w, h)
        this.setPosition(x, y, align)
        this.titleLabel.setAlignment(Align.center)
        this.isModal = false
        this.isMovable = false
        this.isResizable = false
    }

}