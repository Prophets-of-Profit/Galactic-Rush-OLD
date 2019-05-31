package com.prophetsofprofit.galacticrush.graphics.screens.maingame

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.viewport.*
import com.prophetsofprofit.galacticrush.Main
import com.prophetsofprofit.galacticrush.graphics.GalacticRushScreen
import com.prophetsofprofit.galacticrush.logic.map.Planet
import com.prophetsofprofit.galacticrush.networking.player.Player
import ktx.math.vec3

/**
 * Displays a game
 */
class MainGameScreen(main: Main, var player: Player) : GalacticRushScreen(main) {

    //Game state variables
    //A convenience getter that gets the old game state of the player
    val oldGameState
        get() = this.player.oldGameState
    //A convenience getter for the game because the player's game will continuously change
    val mainGame
        get() = this.player.game

    //Graphics variables
    //Views the planets separately from the UI
    var gameboardCamera = OrthographicCamera()
    //Stores the planets, drones, and so on in a stage
    lateinit var stage: Stage
    //The smallest (closest) zoom factor allowed
    val minZoom = 0.1f
    //The highest (furthest) zoom factor allowed
    val maxZoom = 10f
    //Store the images instead of reallocating memory every draw call
    val planetImages = this.mainGame.galaxy.planets.map { it.id to this.createPlanetTexture(it) }.toMap()

    /**
     * Constructs the planets and adds them to the stage as actors
     */
    init {
        //Set the logical size of the gameboard and position the camera
        this.resetCamera()

        this.stage = Stage(ExtendViewport(this.gameboardCamera.viewportWidth, this.gameboardCamera.viewportHeight))

        this.mainGame.galaxy.planets.forEach {
            val image = Image(planetImages[it.id])
            image.width *= it.radius
            image.height *= it.radius
            image.setPosition(
                    it.x * (this.stage.camera.viewportWidth) - image.width / 2,
                    it.y * (this.stage.camera.viewportHeight) - image.height / 2
            )
            this.stage.addActor(image)
        }
    }

    /**
     * Resets the gameboard camera to be the default setting
     */
    fun resetCamera() {
        this.gameboardCamera.setToOrtho(false, 1600f, 900f)
        this.gameboardCamera.zoom = 20f
        this.gameboardCamera.position.x = this.gameboardCamera.viewportWidth / 2
        this.gameboardCamera.position.y = this.gameboardCamera.viewportHeight / 2
    }

    /**
     * Updates the image of the game screen
     */
    override fun draw(delta: Float) {
        //Render highways as white lines
        this.main.shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        this.main.shapeRenderer.color = Color.LIGHT_GRAY
        //Storing the projection matrix for later, we tell the shape renderer to render where the stage would normally draw
        val projectionMatrix = this.main.shapeRenderer.projectionMatrix
        this.main.shapeRenderer.projectionMatrix = this.stage.batch.projectionMatrix
        //Draw the lines
        for (highway in this.mainGame.galaxy.highways) {
            val planet0 = this.mainGame.galaxy.getPlanetWithId(highway.p0)!!
            val planet1 = this.mainGame.galaxy.getPlanetWithId(highway.p1)!!
            val pos0 = vec3(
                            planet0.x* this.stage.camera.viewportWidth, planet0.y * this.stage.camera.viewportHeight, 0f
                        )
            val pos1 = vec3(
                            planet1.x* this.stage.camera.viewportWidth, planet1.y * this.stage.camera.viewportHeight, 0f
                        )
            this.main.shapeRenderer.line(pos0, pos1)
        }
        this.main.shapeRenderer.end()
        //Reset the shape renderer projection matrix
        this.main.shapeRenderer.projectionMatrix = projectionMatrix
        this.stage.act(delta)
        this.stage.draw()
    }

    override fun leave() {
        this.stage.dispose()
    }

    override fun zoom(initialDistance: Float, distance: Float): Boolean {
        return true
    }

    /**
     * Creates a texture for the specified planet
     */
    fun createPlanetTexture(planet: Planet): Texture {
        return Texture("image/planets/planet${(Math.random() * 5).toInt()}.png")
    }
}