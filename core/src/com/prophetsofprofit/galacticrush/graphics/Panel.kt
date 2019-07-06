package com.prophetsofprofit.galacticrush.graphics

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Align

/**
 * A class that represents a fixed-place or static panel that doesn't move outside of animations
 */
open class Panel(screen: GalacticRushScreen, skin: Skin, title: String, private val location: Rectangle, private val dormantLocation: Rectangle, val animationTime: Float, align: Int = Align.bottomLeft) :
        GalacticRushWindow(screen, title, skin, false, location.x, location.y, location.width, location.height, align) {

    //Whether the panel is where it should be
    val isInLocation
        get() = this.x == this.location.x && this.y == this.location.y && this.width == this.location.width && this.height == this.location.height

    /**
     * Initializes the Window as an immovable panel
     */
    init {
        this.titleLabel.setAlignment(Align.center)
    }

    /**
     * What happens when the panel appears
     * How does it appear?
     */
    override fun appear() {
        this.move(this.location, this.animationTime)
    }

    /**
     * What happens when the panel disappears
     * How does it disappear?
     */
    override fun disappear() {
        this.move(this.dormantLocation, this.animationTime)
    }

}
