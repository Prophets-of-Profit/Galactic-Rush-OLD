package com.prophetsofprofit.galacticrush.graphics

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ScalingViewport
import com.prophetsofprofit.galacticrush.Main
import ktx.app.KtxScreen

/**
 * An abstract class that is what each screen in Galactic Rush should handle
 * Is a screen that allows for screen lifetime and input/gesturedetection to be handled
 */
abstract class GalacticRushScreen(val main: Main) : KtxScreen, GestureDetector.GestureListener, InputProcessor {

    //The stage that contains all of the screen's UI elements
    val uiContainer = Stage(ScalingViewport(Scaling.fit, this.main.camera.viewportWidth, this.main.camera.viewportHeight))
    //Gets the camera for the ui components
    val uiCamera
        get() = this.uiContainer.camera

    /**
     * Sets the screen as the main input method
     */
    init {
        Gdx.input.inputProcessor = InputMultiplexer(this, GestureDetector(this), this.uiContainer)
    }

    /**
     * The procedure for drawing and/or updating the screen
     */
    abstract fun draw(delta: Float)

    /**
     * Requires a leave procedure to be defined
     */
    abstract fun leave()

    /**
     * The exit procedure for the screen just disposes of held items
     */
    override fun dispose() {
        this.leave()
        this.uiContainer.dispose()
    }

    /**
     * Draws the screen by first updating the camera, clearing the screen, calling the abstract draw method, and then drawing the UI components
     */
    override fun render(delta: Float) {
        //Clear the screen with black
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        //Calls the screen draw method
        this.draw(delta)
        //Updates and draws the UI components
        this.uiContainer.act(delta)
        this.uiContainer.draw()
    }

    /**
     * Notifies the uiContainer of the changed window dimensions
     */
    override fun resize(width: Int, height: Int) {
        this.uiContainer.viewport.update(width, height)
    }

    //Called when a finger is dragged and lifted
    override fun fling(velocityX: Float, velocityY: Float, button: Int): Boolean = false

    //Called when a key is pressed
    override fun keyDown(keycode: Int): Boolean = false

    //Called when a key mapping to a character is pressed
    override fun keyTyped(char: Char): Boolean = false

    //Called when a key is released
    override fun keyUp(keycode: Int): Boolean = false

    //Called when a finger is held down for some time
    override fun longPress(x: Float, y: Float): Boolean = false

    //Called when the mouse moves
    override fun mouseMoved(x: Int, y: Int): Boolean = false

    //Called when the mouse is dragged
    override fun pan(x: Float, y: Float, deltaX: Float, deltaY: Float): Boolean = false

    //Called when no longer panning
    override fun panStop(x: Float, y: Float, pointer: Int, button: Int): Boolean = false

    //Called when distance between fingers changes in multitouch
    override fun pinch(initialPointer1: Vector2?, initialPointer2: Vector2?, pointer1: Vector2?, pointer2: Vector2?): Boolean = false

    //Called when distance between fingers stops changing in multitouch
    override fun pinchStop() {}

    //Called when the mouse scroll wheel is moved
    override fun scrolled(amount: Int): Boolean = false

    //Called when the mouse is clicked or screen is tapped
    override fun tap(x: Float, y: Float, count: Int, button: Int): Boolean = false

    //Called when the screen is touched
    override fun touchDown(x: Float, y: Float, pointer: Int, button: Int): Boolean = false

    //Called when the screen is touched
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = false

    //Called when a mouse button is held and the mouse is moved
    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean = false

    //Called when the mouse button is released
    override fun touchUp(x: Int, y: Int, pointer: Int, button: Int): Boolean = false

    //Called when two fingers pinch in or out
    override fun zoom(initialDistance: Float, distance: Float): Boolean = false

}
