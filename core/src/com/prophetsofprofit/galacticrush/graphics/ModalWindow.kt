package com.prophetsofprofit.galacticrush.graphics

import com.badlogic.gdx.utils.Align
import com.prophetsofprofit.galacticrush.graphics.screen.GalacticRushScreen

/**
 * A class that represents a modal window that can be dragged and moved
 * Always appears in center of screen
 */
open class ModalWindow(screen: GalacticRushScreen, title: String, w: Float, h: Float) : Panel(screen, title, screen.uiCamera.viewportWidth / 2, screen.uiCamera.viewportHeight / 2, w, h, Align.center) {

    /**
     * Sets up the modal window by setting position
     */
    init {
        this.isMovable = true
        this.isVisible = false
    }

}