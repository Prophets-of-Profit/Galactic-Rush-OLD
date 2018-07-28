package com.prophetsofprofit.galacticrush.graphics.screen.maingame

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.Actor
import com.prophetsofprofit.galacticrush.graphics.screen.GalacticRushScreen

/**
 * Handles panning the selected screen from place to place over a time
 */
class PanHandler(val camera: OrthographicCamera) {

    //The distance the screen needs to pan
    var deltaX = 0f
    var deltaY = 0f
    //The amount the screen needs to zoom
    var zoom = 0f
    //The time over which to pan it
    var time = 0f
    //The time that was initially allotted to pan
    var maxTime = 0f

    /**
     * Updates the positions of all the actors and decrements their times
     * If the times go to zero, remove them from the queue
     */
    fun update(delta: Float) {
        this.camera.translate(this.deltaX * minOf(delta, this.time) / this.maxTime, this.deltaY * minOf(delta, this.time) / this.maxTime)
        this.camera.zoom += this.zoom * minOf(delta, this.time) / this.maxTime
        this.time -= delta
        if (this.time <= 0) {
            this.deltaX = 0f
            this.deltaY = 0f
            this.zoom = 0f
        }
    }

    /**
     * Orders a new pan
     */
    fun add(deltaX: Float, deltaY: Float, zoom: Float, time: Float) {
        this.deltaX = deltaX
        this.deltaY = deltaY
        this.zoom = zoom
        this.time = time
        this.maxTime = time
    }

}

