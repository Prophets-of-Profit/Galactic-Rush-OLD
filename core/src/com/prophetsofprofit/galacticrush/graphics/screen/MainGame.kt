package com.prophetsofprofit.galacticrush.graphics.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.prophetsofprofit.galacticrush.Main
import com.prophetsofprofit.galacticrush.logic.map.Galaxy
import ktx.app.KtxScreen

/**
 * The screen where all the playing will be done
 */
class MainGame(val game: Main, val galaxy: Galaxy = Galaxy(20)): KtxScreen {

    /**
     * Initializes the camera for the screen
     */
    init {
        game.camera.setToOrtho(false, 1600f, 900f)
    }

    /**
     * How the game is drawn
     * Draws the map on the bottom and then draws planets
     */
    override fun render(delta: Float) {

        this.game.batch.projectionMatrix = this.game.camera.combined
        this.game.shapeRenderer.projectionMatrix = this.game.camera.combined

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        for (planet in galaxy.planets) {
            game.shapeRenderer.color = planet.color
            game.shapeRenderer.circle((planet.x * game.camera.viewportWidth).toFloat(), (planet.y * game.camera.viewportHeight).toFloat(), planet.radius.toFloat())
        }
        game.shapeRenderer.end()
    }

}

