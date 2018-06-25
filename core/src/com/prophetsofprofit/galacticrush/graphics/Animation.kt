package com.prophetsofprofit.galacticrush.graphics

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture

/**
 * A class that handles frames of an animation
 */
class Animation(val frames: Array<Texture>, val framesTime: Float) {

    //The index of the current frame
    var image = 0
    //The time that has passed since the animation began on its loop
    var timeSpent = 0f

    /**
     * Adds time to the animation
     * Updates the currently active frame according to the difference in time
     */
    fun addTime(delta: Float) {
        this.timeSpent += delta
        this.timeSpent %= this.framesTime
        this.image = (this.timeSpent / this.framesTime * this.frames.size).toInt()
    }

    /**
     * Gets the currently active frame of the animation
     */
    fun getTexture(): Texture {
        return this.frames[image]
    }

}