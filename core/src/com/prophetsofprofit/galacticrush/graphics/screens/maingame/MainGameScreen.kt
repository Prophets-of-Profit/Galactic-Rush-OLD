package com.prophetsofprofit.galacticrush.graphics.screens.maingame

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.*
import com.prophetsofprofit.galacticrush.Main
import com.prophetsofprofit.galacticrush.graphics.GalacticRushScreen
import com.prophetsofprofit.galacticrush.graphics.screens.maingame.ui.NotificationStack
import com.prophetsofprofit.galacticrush.graphics.screens.maingame.ui.gameboard.PlanetActor
import com.prophetsofprofit.galacticrush.graphics.screens.maingame.ui.notifications.DraftNotification
import com.prophetsofprofit.galacticrush.graphics.screens.maingame.ui.notifications.DronePhaseNotification
import com.prophetsofprofit.galacticrush.graphics.screens.maingame.ui.notifications.PlayerFreePhaseNotification
import com.prophetsofprofit.galacticrush.graphics.screens.maingame.ui.notifications.StartGameNotification
import com.prophetsofprofit.galacticrush.logic.map.Planet
import com.prophetsofprofit.galacticrush.networking.player.Player
import ktx.math.vec3
import ktx.scene2d.Scene2DSkin
import kotlin.math.*

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
    private var gameboardCamera = OrthographicCamera(1600f, 900f)
    //Stores the planets, drones, and so on in a stage
    private val stage = Stage(ExtendViewport(this.gameboardCamera.viewportWidth, this.gameboardCamera.viewportHeight)).also {
        this.inputMultiplexer.addProcessor(it)
    }
    //A convenience getter that views the stage's camera as orthographic
    private val camera
        get() = this.stage.camera as OrthographicCamera
    //The smallest (closest) zoom factor allowed
    private val minZoom = 0.1f
    //The highest (furthest) zoom factor allowed
    private val maxZoom = 10f
    //Store the images instead of reallocating memory every draw call
    private val planetImages = this.mainGame.galaxy.planets.map { it.id to this.createPlanetTexture(it) }.toMap()
    //A map of planet (index) to planet actor for drawing
    private val planetActors = mutableMapOf<Int, PlanetActor>()

    //UI size variables
    private val notificationSize = this.uiCamera.viewportWidth / 16

    //Input
    private val inputMultiplexer: InputMultiplexer
        get() = Gdx.input.inputProcessor as InputMultiplexer

    //UI components
    //The button that controls submitting turn changes
    private val submitButton = TextButton("Submit", Scene2DSkin.defaultSkin).also {
        it.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                this@MainGameScreen.player.submitCurrentChanges()
            }
        })
        it.setSize(this.uiCamera.viewportWidth / 8, this.uiCamera.viewportHeight / 8)
        it.setPosition(this.uiCamera.viewportWidth, 0f, Align.bottomRight)
        it.isDisabled = true
        this.uiContainer.addActor(it)
    }
    //The container that handles notifications
    private val notificationStack = NotificationStack(
            this.uiCamera.viewportWidth * 7/8,
            this.uiCamera.viewportHeight * 1/8,
            this.uiCamera.viewportWidth * 1/8,
            this.uiCamera.viewportHeight * 7/8,
            5,
            this.uiCamera.viewportWidth / 16
    ).also {
        this.uiContainer.addActor(it)
    }

    /**
     * Constructs the planets and adds them to the stage as actors
     */
    init {
        //Add planets as actors
        this.mainGame.galaxy.planets.forEach {
            val image = PlanetActor(it.id, planetImages[it.id]!!)
            image.width *= it.radius
            image.height *= it.radius
            image.setPosition(
                    it.x * (this.camera.viewportWidth) - image.width / 2,
                    it.y * (this.camera.viewportHeight) - image.height / 2
            )
            this.planetActors[it.id] = image
            this.stage.addActor(image)
        }
        this.notificationStack.addNotification(StartGameNotification(this.notificationSize, this.notificationSize))
    }

    /**
     * Resets the gameboard camera to be the default setting
     */
    fun resetCamera() {
        this.camera.setToOrtho(false, 1600f, 900f)
        this.camera.zoom = 1f
        this.camera.position.x = this.camera.viewportWidth / 2
        this.camera.position.y = this.camera.viewportHeight / 2
    }

    /**
     * Updates the image of the game screen
     */
    override fun draw(delta: Float) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            this.notificationStack.addNotification(StartGameNotification(this.notificationSize, this.notificationSize))
            println("${this.notificationStack.topNotification.first!!.x}, ${this.notificationStack.topNotification.first!!.y}")
        }
        //Render highways as white lines
        this.main.shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        this.main.shapeRenderer.color = Color.LIGHT_GRAY
        //Storing the projection matrix for later, we tell the shape renderer to render where the stage batch would normally draw
        val projectionMatrix = this.main.shapeRenderer.projectionMatrix
        this.main.shapeRenderer.projectionMatrix = this.stage.batch.projectionMatrix
        //Draw the lines
        for (highway in this.mainGame.galaxy.highways) {
            val planetActor0 = this.planetActors[highway.p0]!!
            val planetActor1 = this.planetActors[highway.p1]!!
            val pos0 = vec3(planetActor0.x + planetActor0.width / 2, planetActor0.y + planetActor0.height / 2, 0f)
            val pos1 = vec3(planetActor1.x + planetActor1.width / 2, planetActor1.y + planetActor1.height / 2, 0f)
            this.main.shapeRenderer.line(pos0, pos1)
        }
        this.main.shapeRenderer.end()
        //Reset the shape renderer projection matrix
        this.main.shapeRenderer.projectionMatrix = projectionMatrix
        this.stage.act(delta)
        this.uiContainer.act(delta)
        this.stage.draw()
    }

    /**
     * Execute when player free phase (actions) starts
     * Follows draft phase
     */
    fun startPlayerFreePhase() {
        this.submitButton.isDisabled = false
        this.notificationStack.addNotification(PlayerFreePhaseNotification(this.notificationSize, this.notificationSize))
    }

    /**
     * Execute when drone phase (instructions) starts
     * Follows player free phase
     */
    fun startDronePhase() {
        this.submitButton.isDisabled = true
        this.notificationStack.addNotification(DronePhaseNotification(this.notificationSize, this.notificationSize))
    }

    /**
     * Execute when draft phase starts
     * Follows drone phase
     */
    fun startDraftPhase() {
        this.notificationStack.addNotification(DraftNotification(this.notificationSize, this.notificationSize))
    }

    override fun leave() {
        this.stage.dispose()
        this.main.resetCamera()
    }

    /**
     * Moves the camera laterally
     */
    override fun pan(x: Float, y: Float, deltaX: Float, deltaY: Float): Boolean {
        val mouseLocation = this.main.windowToCamera(x.roundToInt(), y.roundToInt(), this.main.camera)
        //Doesn't zoom if we're hovering over a UI element
        if (this.uiContainer.hit(mouseLocation.x, mouseLocation.y, false) != null) {
            return false
        }
        this.camera.translate(-deltaX * this.camera.zoom, deltaY * this.camera.zoom)
        return false
    }

    /**
     * Sets the zoom to a value between the minimum and maximum zoom specified above
     */
    private fun setZoomClamped(newZoom: Float) {
        this.camera.zoom = max(min(newZoom, this.maxZoom), this.minZoom)
    }

    /**
     * Zooming moves the camera closer in or further out
     */
    override fun zoom(initialDistance: Float, distance: Float): Boolean {
        val zoomWeight = 0.15f
        //Zoom is proportional to distance moved and multiplicative
        this.setZoomClamped(zoomWeight * initialDistance / distance + this.camera.zoom * (1 - zoomWeight))
        return true
    }

    /**
     * Zooms with mouse scroll
     */
    override fun scrolled(amount: Int): Boolean {
        val zoomFactor = 0.1f
        //TODO: Center at mouse location
        this.setZoomClamped(this.camera.zoom + amount * zoomFactor)
        return true
    }

    /**
     * Notifies the stage of the changed window dimensions
     */
    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        this.stage.viewport.update(width, height)
    }

    /**
     * Creates a texture for the specified planet
     */
    fun createPlanetTexture(planet: Planet): Texture {
        return Texture("image/planets/planet${(Math.random() * 5).toInt()}.png")
    }

}