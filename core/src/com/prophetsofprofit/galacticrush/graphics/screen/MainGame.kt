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
import com.prophetsofprofit.galacticrush.logic.map.Galaxy
import ktx.app.KtxScreen
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * The screen where all the playing will be done
 */
class MainGame(val game: Main, val galaxy: Galaxy = Galaxy(100)): KtxScreen, GestureDetector.GestureListener, InputProcessor {

    //The smallest (closest) zoom factor allowed
    val minZoom = 0.1f
    //The highest (furthest) zoom factor allowed
    val maxZoom = 1f
    //Temporary place to store music
    val music = Gdx.audio.newMusic(Gdx.files.internal("music/the intergalactic.mp3"))

    /**
     * Initializes the camera for the screen
     */
    init {
        val multiplexer = InputMultiplexer()
        multiplexer.addProcessor(GestureDetector(this))
        multiplexer.addProcessor(this)
        game.camera.setToOrtho(false, 1600f, 900f)
        Gdx.input.inputProcessor = multiplexer
        music.play()
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
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        //Render highways as white lines
        game.shapeRenderer.color = Color.WHITE
        for (highway in galaxy.highways) {
            game.shapeRenderer.line(highway.p0.x * game.camera.viewportWidth, highway.p0.y * game.camera.viewportHeight, highway.p1.x * game.camera.viewportWidth, highway.p1.y * game.camera.viewportHeight)
        }
        game.shapeRenderer.end()

        //Render planets as colored circles
        //TODO: add textures for planets
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        for (planet in galaxy.planets) {
            game.shapeRenderer.color = planet.color
            game.shapeRenderer.circle(planet.x * game.camera.viewportWidth, planet.y * game.camera.viewportHeight, 10 * planet.radius * sqrt(game.camera.viewportWidth.pow(2) + game.camera.viewportHeight.pow(2)))
        }
        game.shapeRenderer.end()
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
        this.setZoomClamped(distance / initialDistance)
        return true
    }

    /**
     * Zooms with mouse scroll
     */
    override fun scrolled(amount: Int): Boolean {
        this.setZoomClamped(this.game.camera.zoom + amount * 0.1f)
        return true
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
    //Called when the screen is tapped
    override fun tap(x: Float, y: Float, count: Int, button: Int): Boolean = false
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
