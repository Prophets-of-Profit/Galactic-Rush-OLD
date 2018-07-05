package com.prophetsofprofit.galacticrush.graphics.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.math.Vector2
import com.prophetsofprofit.galacticrush.Main
import com.prophetsofprofit.galacticrush.graphics.MusicPlayer
import com.prophetsofprofit.galacticrush.logic.Game
import com.prophetsofprofit.galacticrush.logic.map.Planet
import com.prophetsofprofit.galacticrush.logic.player.Player
import ktx.app.KtxScreen
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * The screen where all the playing will be done
 */
class MainGameScreen(val game: Main, var player: Player) : KtxScreen, GestureDetector.GestureListener, InputProcessor {

    //A convenience getter for the game because the player's game will continuously change
    var mainGame = Game(arrayOf(), 0)
        get() = this.player.game
    //The smallest (closest) zoom factor allowed
    val minZoom = 0.1f
    //The highest (furthest) zoom factor allowed
    val maxZoom = 1f
    //The music player object; initialize with all files in music folder
    val musicPlayer = MusicPlayer(Array(Gdx.files.internal("music/").list().size) { "" + Gdx.files.internal("music/").list()[it] })
    //The planet currently selected by the player
    var selectedPlanet: Planet? = null

    /**
     * Initializes the camera for the screen
     */
    init {
        this.musicPlayer.volume = game.userOptions.musicVolume
        val multiplexer = InputMultiplexer()
        multiplexer.addProcessor(GestureDetector(this))
        multiplexer.addProcessor(this)
        game.camera.setToOrtho(false, 1600f, 900f)
        Gdx.input.inputProcessor = multiplexer
    }

    /**
     * How the game is drawn
     * Draws the map on the bottom and then draws planets
     */
    override fun render(delta: Float) {
        //Update position of view to match camera's position
        this.game.camera.update()
        this.game.batch.projectionMatrix = this.game.camera.combined
        this.game.shapeRenderer.projectionMatrix = this.game.camera.combined

        //Color background in black
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

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
        this.musicPlayer.update()
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
        return this.selectedPlanet != null
    }

    //GestureListener abstract method implementations:
    //Called when a finger is dragged and lifted
    override fun fling(velocityX: Float, velocityY: Float, button: Int): Boolean = false

    //Called when a finger is held down for some time
    override fun longPress(x: Float, y: Float): Boolean = false

    //Called when no longer panning
    override fun panStop(x: Float, y: Float, pointer: Int, button: Int): Boolean = false

    //Called when distance between fingers changes in multitouch
    override fun pinch(initialPointer1: Vector2?, initialPointer2: Vector2?, pointer1: Vector2?, pointer2: Vector2?): Boolean = false

    //Called when distance between fingers stops changing in multitouch
    override fun pinchStop() {}

    //Called when the screen is touched
    override fun touchDown(x: Float, y: Float, pointer: Int, button: Int): Boolean = false

    //InputProcessor abstract method implementations:
    //Called when the mouse button is pressed
    override fun touchDown(x: Int, y: Int, pointer: Int, button: Int): Boolean = false

    //Called when the mouse button is released
    override fun touchUp(x: Int, y: Int, pointer: Int, button: Int): Boolean = false

    //Called when the mouse moves
    override fun mouseMoved(x: Int, y: Int): Boolean = false

    //Called when a key mapping to a character is pressed
    override fun keyTyped(char: Char): Boolean = false

    //Called when a key is pressed
    override fun keyDown(keycode: Int): Boolean = false

    //Called when a key is released
    override fun keyUp(keycode: Int): Boolean = false

    //Called when a mouse button is held and the mouse is moved
    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean = false

}
