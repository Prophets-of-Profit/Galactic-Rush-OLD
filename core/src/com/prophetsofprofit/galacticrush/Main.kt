package com.prophetsofprofit.galacticrush

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.GdxRuntimeException
import com.prophetsofprofit.galacticrush.graphics.screen.SplashScreen
import ktx.scene2d.Scene2DSkin
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
 * GALACTIC RUSH
 * The object that is where/what the game is run in in a generalized context
 * No matter what platform, the game will be run and managed mainly by this class
 */
class Main : Game() {

    //The user controllable options: set to default values, but changes get written to file and applied
    var userOptions: Options = Options(25, 50)
        set(value) {
            field = value
            applyOptions()
        }
    lateinit var batch: SpriteBatch //What will be used to draw sprites and textures and all game things
    lateinit var shapeRenderer: ShapeRenderer //What will be used to draw shapes
    lateinit var camera: OrthographicCamera //The object that handles coordinates for drawing game things

    /**
     * Entry point of the game
     */
    override fun create() {
        try {
            userOptions = ObjectInputStream(optionsFile.read()).readObject() as Options
        } catch (ignored: GdxRuntimeException) {
        } finally {
            applyOptions()
        }
        this.batch = SpriteBatch()
        this.shapeRenderer = ShapeRenderer()
        this.camera = OrthographicCamera()
        this.camera.setToOrtho(false, 1600f, 900f)
        Scene2DSkin.defaultSkin = Skin(Gdx.files.internal("uiskin/skin.json"))
        this.screen = SplashScreen(this)
    }

    /**
     * What happens when the game is done
     */
    override fun dispose() {
        this.batch.dispose()
        this.shapeRenderer.dispose()
        this.screen.dispose()
    }

    /**
     * Converts window coordinates (as integers) into coordinates from the camera
     * Returns the coordinates to a 2-dimensional vector
     */
    fun windowToCamera(x: Int, y: Int): Vector2 {
        val convertedVector = this.camera.unproject(Vector3(x.toFloat(), y.toFloat(), 0f))
        return Vector2(convertedVector.x, convertedVector.y)
    }

    /**
     * Updates the game to reflect current options and saves options to file
     */
    private fun applyOptions() {
        //TODO: make changes to game based on options
        ObjectOutputStream(optionsFile.write(false)).writeObject(this.userOptions)
    }

}
