package com.prophetsofprofit.galacticrush.graphics.screen.maingame

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.prophetsofprofit.galacticrush.logic.drone.Drone
import com.prophetsofprofit.galacticrush.logic.map.Planet
import ktx.scene2d.Scene2DSkin
import kotlin.math.atan2
import kotlin.math.PI

/**
 * Handles animating transitions between turns
 */
class TurnAnimationHandler(val mainGameScreen: MainGameScreen) {

    //Which actors are still moving
    //The values are of the form [dx, dy, time remaining, total time]
    var currentlyMoving = mutableMapOf<Sprite, Array<Float>>()
    val transitionTime = 1f

    /**
     * Updates the state of the animations
     */
    fun update(delta: Float) {
        val toRemove = mutableListOf<Sprite>()
        this.currentlyMoving.keys.forEach {
            it.translate(
                    this.currentlyMoving[it]!![0] * minOf(delta, this.currentlyMoving[it]!![2]) / this.currentlyMoving[it]!![3],
                    this.currentlyMoving[it]!![1] * minOf(delta, this.currentlyMoving[it]!![2]) / this.currentlyMoving[it]!![3]
            )
            this.currentlyMoving[it]!![2] -= delta
            if (this.currentlyMoving[it]!![2] <= 0) toRemove.add(it)
        }
        toRemove.forEach { this.currentlyMoving.remove(it) }
    }

    /**
     * Moves a drone between two planets
     */
    fun move(drone: Drone, p0: Planet, p1: Planet) {
        val dx = (p1.x - p0.x) * this.mainGameScreen.game.camera.viewportWidth
        val dy = (p1.y - p0.y) * this.mainGameScreen.game.camera.viewportHeight
        val droneImage = Sprite(drone.image)
        droneImage.setCenter(p0.x * this.mainGameScreen.game.camera.viewportWidth, p0.y * this.mainGameScreen.game.camera.viewportHeight)
        println("dx: $dx , dy: $dy, rotation = ${(atan2(dx, dy) * 180 / PI).toFloat()}")
        droneImage.rotation = (atan2(dy, dx) * 180 / PI).toFloat()
        //Ensure the drone image is not already moving
        this.currentlyMoving.remove(droneImage)
        //Queue the drone image for an animation
        this.currentlyMoving[droneImage] = arrayOf(dx, dy, this.transitionTime, this.transitionTime)
    }

}