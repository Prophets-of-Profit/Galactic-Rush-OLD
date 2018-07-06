package com.prophetsofprofit.galacticrush.graphics.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.prophetsofprofit.galacticrush.Main
import com.prophetsofprofit.galacticrush.logic.Game
import com.prophetsofprofit.galacticrush.logic.map.Attribute
import com.prophetsofprofit.galacticrush.logic.map.Planet
import com.prophetsofprofit.galacticrush.logic.player.Player
import ktx.scene2d.Scene2DSkin
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * The screen where all the playing will be done
 */
class MainGameScreen(game: Main, var player: Player) : GalacticRushScreen(game, Array(Gdx.files.internal("music/").list().size) { "" + Gdx.files.internal("music/").list()[it] }) {

    //A convenience getter for the game because the player's game will continuously change
    var mainGame = Game(arrayOf(), 0)
        get() = this.player.game
    //The smallest (closest) zoom factor allowed
    val minZoom = 0.1f
    //The highest (furthest) zoom factor allowed
    val maxZoom = 1f
    //The planet currently selected by the player
    var selectedPlanet: Planet? = null
    //The label sho
    // wing planet attributes
    var planetLabel = Label("", Scene2DSkin.defaultSkin)

    init {
        this.uiContainer.addActor(this.planetLabel)
    }

    /**
     * How the game is drawn
     * Draws the map on the bottom and then draws planets
     */
    override fun draw(delta: Float) {
        //Begins rendering the objects on the screen
        this.game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        //Render highways as white lines
        this.game.shapeRenderer.color = Color.WHITE
        for (highway in this.mainGame.galaxy.highways) {
            this.game.shapeRenderer.line(highway.p0.x * this.game.camera.viewportWidth, highway.p0.y * this.game.camera.viewportHeight, highway.p1.x * this.game.camera.viewportWidth, highway.p1.y * this.game.camera.viewportHeight)
        }
        this.game.shapeRenderer.end()

        //Render planets as colored circles
        //Draws outline of inverted color for selected planet
        //TODO: add textures for planets and for selected planet
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        if (this.selectedPlanet != null) {
            this.game.shapeRenderer.color = Color(1 - this.selectedPlanet!!.color.r, 1 - this.selectedPlanet!!.color.g, 1 - this.selectedPlanet!!.color.b, 1f)
            this.game.shapeRenderer.circle(this.selectedPlanet!!.x * this.game.camera.viewportWidth, this.selectedPlanet!!.y * this.game.camera.viewportHeight, 15 * this.selectedPlanet!!.radius * sqrt(this.game.camera.viewportWidth.pow(2) + this.game.camera.viewportHeight.pow(2)))
        }
        for (planet in this.mainGame.galaxy.planets) {
            this.game.shapeRenderer.color = planet.color
            this.game.shapeRenderer.circle(planet.x * this.game.camera.viewportWidth, planet.y * this.game.camera.viewportHeight, 10 * planet.radius * sqrt(this.game.camera.viewportWidth.pow(2) + this.game.camera.viewportHeight.pow(2)))
        }
        this.game.shapeRenderer.end()
    }

    /**
     * Indicates that the selected planet is selected
     */
    private fun drawSelectionArrows() {
        if (this.selectedPlanet == null) {
            return
        }
        //TODO: make
    }

    /**
     * Panning moves the camera laterally to adjust what is being seen
     */
    override fun pan(x: Float, y: Float, deltaX: Float, deltaY: Float): Boolean {
        this.game.camera.translate(-deltaX, deltaY)
        return true
    }

    /**
     * Sets the zoom of the camera and ensures that the new zoom is clamped between acceptable values
     */
    private fun setZoomClamped(newZoom: Float) {
        this.game.camera.zoom = max(this.minZoom, min(this.maxZoom, newZoom))
    }

    /**
     * Zooming moves the camera closer in or further out
     */
    override fun zoom(initialDistance: Float, distance: Float): Boolean {
        this.setZoomClamped(initialDistance / distance)
        return true
    }

    /**
     * Zooms with mouse scroll
     */
    override fun scrolled(amount: Int): Boolean {
        this.setZoomClamped(this.game.camera.zoom + amount * 0.1f)
        return true
    }

    /**
     * When the screen is tapped, check to see if any planets are selected
     * Will only consume the tap event if a planet is selected
     */
    override fun tap(x: Float, y: Float, count: Int, button: Int): Boolean {
        this.selectedPlanet = this.mainGame.galaxy.planets.firstOrNull {
            //it != this.selectedPlanet && /* This line of code will unselect a selected planet if clicked on again; TODO: discuss desired behaviour */
            sqrt((this.game.windowToCamera(x.toInt(), y.toInt()).x / this.game.camera.viewportWidth - it.x).pow(2)
                + (this.game.windowToCamera(x.toInt(), y.toInt()).y / this.game.camera.viewportHeight - it.y).pow(2)) < it.radius * 10
        }
        if (this.selectedPlanet != null) {
            this.planetLabel.setText(Attribute.values().map { it.toString() + this.selectedPlanet!!.attributes[it] + "\n" }.toString())
            //TODO: account for changing of camera viewport
            this.planetLabel.setPosition(this.uiContainer.width * this.selectedPlanet!!.x + this.planetLabel.width, this.uiContainer.height * this.selectedPlanet!!.y, Align.center)
            this.planetLabel.isVisible = true
        } else {
            this.planetLabel.setText("")
            this.planetLabel.isVisible = false
        }
        return this.selectedPlanet != null
    }

    /**
     * Does nothing on leave
     */
    override fun leave() {}

}
