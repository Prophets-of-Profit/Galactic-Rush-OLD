package com.prophetsofprofit.galacticrush.graphics

import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.utils.Align
import com.prophetsofprofit.galacticrush.graphics.screen.GalacticRushScreen
import ktx.scene2d.Scene2DSkin

/**
 * A class that represents a modal window that can be moved and contains elements
 */
open class ModalWindow(screen: GalacticRushScreen, title: String, w: Float, h: Float) : Window(title, Scene2DSkin.defaultSkin) {

    /**
     * Sets up the modal window by setting position
     */
    init {
        this.style.stageBackground = null
        this.setSize(w, h)
        this.setPosition(screen.uiCamera.viewportWidth / 2, screen.uiCamera.viewportHeight / 2, Align.center)
        this.titleLabel.setAlignment(Align.center)
        this.titleTable.debug = true
        this.isModal = true
        this.isMovable = true
        this.isResizable = false
    }

}