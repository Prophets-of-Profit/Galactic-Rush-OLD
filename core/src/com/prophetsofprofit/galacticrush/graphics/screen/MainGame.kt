package com.prophetsofprofit.galacticrush.graphics.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.math.Vector2
import com.prophetsofprofit.galacticrush.Main
import com.prophetsofprofit.galacticrush.logic.map.Galaxy
import ktx.app.KtxScreen
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * The screen where all the playing will be done
 */
class MainGame(val game: Main, val galaxy: Galaxy = Galaxy(100)): KtxScreen, GestureDetector.GestureListener {

    /**
     * Initializes the camera for the screen
     */
    init {
        game.camera.setToOrtho(false, 1600f, 900f)
        Gdx.input.inputProcessor = GestureDetector(this)
    }

    /**
     * How the game is drawn
     * Draws the map on the bottom and then draws planets
     */
    override fun render(delta: Float) {
        this.game.camera.update()
        this.game.batch.projectionMatrix = this.game.camera.combined
        this.game.shapeRenderer.projectionMatrix = this.game.camera.combined

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        game.shapeRenderer.color = Color.WHITE
        for (highway in galaxy.highways) {
            game.shapeRenderer.line(highway.p0.x.toFloat() * game.camera.viewportWidth, highway.p0.y.toFloat() * game.camera.viewportHeight, highway.p1.x.toFloat() * game.camera.viewportWidth, highway.p1.y.toFloat() * game.camera.viewportHeight)
        }
        game.shapeRenderer.end()

        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        for (planet in galaxy.planets) {
            game.shapeRenderer.color = planet.color
            game.shapeRenderer.circle((planet.x * game.camera.viewportWidth).toFloat(), (planet.y * game.camera.viewportHeight).toFloat(), 10 * planet.radius * sqrt(game.camera.viewportWidth.pow(2) + game.camera.viewportHeight.pow(2)))
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
     * Zooming moves the camera closer in or further out
     */
    override fun zoom(initialDistance: Float, distance: Float): Boolean {
        this.game.camera.zoom = distance / initialDistance
        return true
    }

    override fun fling(velocityX: Float, velocityY: Float, button: Int): Boolean = false
    override fun longPress(x: Float, y: Float): Boolean = false
    override fun panStop(x: Float, y: Float, pointer: Int, button: Int): Boolean = false
    override fun pinch(initialPointer1: Vector2?, initialPointer2: Vector2?, pointer1: Vector2?, pointer2: Vector2?): Boolean = false
    override fun pinchStop() {}
    override fun tap(x: Float, y: Float, count: Int, button: Int): Boolean = false
    override fun touchDown(x: Float, y: Float, pointer: Int, button: Int): Boolean = false

}
