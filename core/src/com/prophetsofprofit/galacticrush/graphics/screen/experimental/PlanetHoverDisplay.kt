package com.prophetsofprofit.galacticrush.graphics.screen.experimental

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.prophetsofprofit.galacticrush.logic.map.PlanetAttribute
import com.prophetsofprofit.galacticrush.planetRadiusScale
import ktx.math.minus
import ktx.scene2d.Scene2DSkin
import kotlin.math.pow
import kotlin.math.sqrt

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
        var hoveredPlanetId: Int? = null

        this.width = 200f
        this.height = 50f

        PlanetAttribute.values().forEach { attribute ->
            this.add(Image(Scene2DSkin.defaultSkin, attribute.displayString)).pad(5f)
            this.add(Label("", Scene2DSkin.defaultSkin).also {
                it.setAlignment(Align.center)
                it.addAction(object : Action() {
                    override fun act(delta: Float): Boolean {
                        it.setText(if (hoveredPlanetId == null) "" else attribute.stringValue(gameScreen.mainGame.galaxy.getPlanetWithId(hoveredPlanetId!!)!!.attributes[attribute]!!))
                        return false
                    }
                })
            }).expandX().fillX().padRight(5f).row()
        }

        this.addAction(object : Action() {
            override fun act(delta: Float): Boolean {
                //Updates previous x and y coordinates as well as elapsed still time; elapsed time increments if mouse is still on a planet or anywhere on top of this
                val stageMouseCoords = gameScreen.game.windowToCamera(Gdx.input.x, Gdx.input.y, gameScreen.uiCamera)
                val hovered = gameScreen.uiContainer.hit(stageMouseCoords.x, stageMouseCoords.y, false)
                if (Gdx.input.x == prevX && Gdx.input.y == prevY || hovered == this@PlanetHoverDisplay || children.any { it == hovered }) {
                    elapsedStillTime += delta
                } else {
                    elapsedStillTime = 0f
                    prevX = Gdx.input.x
                    prevY = Gdx.input.y
                }

                //Moves menu to mouse location
                setPosition(stageMouseCoords.x, stageMouseCoords.y, Align.topLeft)

                //If elapsed still time is not great enough, the menu becomes invisible
                isVisible = elapsedStillTime >= durationUntilAppearance
                if (!isVisible) {
                    return false
                }

                //Sets the hovered planet id to either the currently hovered planet if any or the already selected id otherwise
                val gameMouseCoords = gameScreen.game.windowToCamera(Gdx.input.x, Gdx.input.y)
                hoveredPlanetId = gameScreen.mainGame.galaxy.planets.firstOrNull { planet ->
                    val distanceBetween = (gameMouseCoords - Vector2(planet.x * gameScreen.game.camera.viewportWidth, planet.y * gameScreen.game.camera.viewportHeight)).len()
                    val planetRadius = planetRadiusScale * planet.radius * sqrt(gameScreen.game.camera.viewportWidth.pow(2) + gameScreen.game.camera.viewportHeight.pow(2))
                    distanceBetween < planetRadius
                }?.id ?: hoveredPlanetId

                return false
            }
        })
    }

}