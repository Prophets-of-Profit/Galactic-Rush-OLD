package com.prophetsofprofit.galacticrush

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.prophetsofprofit.galacticrush.graphics.screen.SplashScreen
import ktx.scene2d.Scene2DSkin

/**
 * GALACTIC RUSH
 * The object that is where/what the game is run in in a generalized context
 * No matter what platform, the game will be run and managed mainly by this class
 */
class Main : Game() {

    lateinit var batch: SpriteBatch //What will be used to draw sprites and textures and all game things
    lateinit var textDrawer: BitmapFont //What will be used to draw text that isn't a part of a label
    lateinit var shapeRenderer: ShapeRenderer //What will be used to draw shapes
    lateinit var camera: OrthographicCamera //The object that handles coordinates for drawing game things

    /**
     * Entry point of the game
     */
    override fun create() {
        this.batch = SpriteBatch()
        this.textDrawer = BitmapFont()
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
        this.textDrawer.dispose()
    }

}
