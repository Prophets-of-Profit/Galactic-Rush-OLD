package com.prophetsofprofit.galacticrush.graphics.screen.experimental

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.prophetsofprofit.galacticrush.Main
import com.prophetsofprofit.galacticrush.graphics.ModalWindow
import com.prophetsofprofit.galacticrush.graphics.screen.GalacticRushScreen
import com.prophetsofprofit.galacticrush.logic.GamePhase
import com.prophetsofprofit.galacticrush.networking.player.Player
import com.prophetsofprofit.galacticrush.planetRadiusScale
import ktx.app.use
import kotlin.math.*

/**
 * The main screen where the game is actually played
 * Is how the user interacts with the game logic and rules
 */
class GameScreen(game: Main, val player: Player) : GalacticRushScreen(game, Array(Gdx.files.internal("music/").list().size) { "${Gdx.files.internal("music/").list()[it]}" }) {

    //The game to represent
    val mainGame get() = this.player.game
    //A getter that gets the phase of the game: doesn't necessarily match the phase of the game because this may be animating a phase while the game has moved on
    val phase get() = if (this.mainGame.droneTurnChanges.isNotEmpty()) GamePhase.DRONE_PHASE else this.mainGame.phase
    //An object to make textures for game objects
    val textureHandler = TextureHandler()
    //The camera zoom bounds for the screen
    val minZoom = 0.1f
    val maxZoom = 10f
    //The currently opened modal windows
    val activeModals get() = this.uiContainer.actors.filter { it is ModalWindow && it.isVisible }
    //The images of each planet
    val planetImages = this.mainGame.galaxy.planets.map { it.id to this.textureHandler.getTexture(it) }.toMap()
    //The object that handles animating drone turns
    val droneTurnAnimator = DroneTurnAnimator(this)

    /**
     * Adds all necessary actors to the screen
     */
    init {
        this.uiContainer.addActor(PlanetHoverDisplay(this))
        this.uiContainer.addActor(GeneralInformationPanel(this))
        this.uiContainer.addActor(SubmitButton(this))
    }

    /**
     * The procedure for drawing the current game state
     */
    override fun draw(delta: Float) {
        //Draws highways
        this.game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        this.game.shapeRenderer.color = Color.LIGHT_GRAY
        for (highway in this.mainGame.galaxy.highways) {
            val planet0 = this.mainGame.galaxy.getPlanetWithId(highway.p0)!!
            val planet1 = this.mainGame.galaxy.getPlanetWithId(highway.p1)!!
            this.game.shapeRenderer.line(planet0.x * this.game.camera.viewportWidth, planet0.y * this.game.camera.viewportHeight, planet1.x * this.game.camera.viewportWidth, planet1.y * this.game.camera.viewportHeight)
        }
        this.game.shapeRenderer.end()

        //Draws planets
        this.game.batch.begin()
        for (planet in this.mainGame.galaxy.planets) {
            val planetRadius = planetRadiusScale * planet.radius * sqrt(this.game.camera.viewportWidth.pow(2) + this.game.camera.viewportHeight.pow(2))
            this.game.batch.draw(
                    this.planetImages[planet.id],
                    planet.x * this.game.camera.viewportWidth - planetRadius,
                    planet.y * this.game.camera.viewportHeight - planetRadius,
                    2 * planetRadius,
                    2 * planetRadius
            )
        }
        this.game.batch.end()

        //Tells the droneTurnAnimator to animate if necessary
        this.game.batch.use { this.droneTurnAnimator.act(delta) }
    }

    /**
     * The procedure for what happens when this game screen is left
     */
    override fun leave() {
        this.game.resetCamera()
    }

    /**
     * Panning moves the camera laterally to adjust what is being seen
     */
    override fun pan(x: Float, y: Float, deltaX: Float, deltaY: Float): Boolean {
        val mouseLocation = this.game.windowToCamera(x.roundToInt(), y.roundToInt(), this.uiCamera)
        if (this.uiContainer.hit(mouseLocation.x, mouseLocation.y, false) != null) {
            return false
        }
        this.game.camera.translate(-deltaX * this.game.camera.zoom, deltaY * this.game.camera.zoom)
        return false
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
        val zoomWeight = 0.15f
        this.setZoomClamped(zoomWeight * initialDistance / distance + this.game.camera.zoom * (1 - zoomWeight))
        return true
    }

    /**
     * Zooms with mouse scroll
     */
    override fun scrolled(amount: Int): Boolean {
        //TODO: zoom with mouse as focal point of zoom
        this.setZoomClamped(this.game.camera.zoom + amount * 0.1f)
        return true
    }


}