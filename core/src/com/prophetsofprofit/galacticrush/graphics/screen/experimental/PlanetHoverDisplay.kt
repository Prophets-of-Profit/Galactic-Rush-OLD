package com.prophetsofprofit.galacticrush.graphics.screen.experimental

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.ui.Table

/**
 * A panel that displays information of a hovered planet
 */
class PlanetHoverDisplay(gameScreen: GameScreen) : Table() {

    //How long the menu should wait for the user to hover over a planet before appearing
    val durationUntilAppearance = 2f

    /**
     * Adds all basic actors to the display
     */
    init {
        var prevX = Gdx.input.x
        var prevY = Gdx.input.y
        var elapsedStillTime = 0f

        this.addAction(object : Action() {
            override fun act(delta: Float): Boolean {
                //Updates previous x and y coordinates as well as elapsed still time; elapsed time increments if mouse is still on a planet or anywhere on top of this
                val stageMouseCoords = localToStageCoordinates(Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat()))
                val hovered = gameScreen.uiContainer.hit(stageMouseCoords.x, stageMouseCoords.y, false)
                if (Gdx.input.x == prevX && Gdx.input.y == prevY || hovered == this@PlanetHoverDisplay || children.any { it == hovered }) {
                    elapsedStillTime += delta
                } else {
                    elapsedStillTime = 0f
                    prevX = Gdx.input.x
                    prevY = Gdx.input.y
                }
                //TODO: if mouse is not on a planet, set elapsed time to 0f

                //If elapsed still time is great enough, the menu appears
                return false
            }
        })
    }

}