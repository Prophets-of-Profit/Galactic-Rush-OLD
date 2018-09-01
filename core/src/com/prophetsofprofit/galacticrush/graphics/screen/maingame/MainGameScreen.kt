package com.prophetsofprofit.galacticrush.graphics.screen.maingame

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import com.prophetsofprofit.galacticrush.Main
import com.prophetsofprofit.galacticrush.graphics.Direction
import com.prophetsofprofit.galacticrush.graphics.OptionsMenu
import com.prophetsofprofit.galacticrush.graphics.screen.GalacticRushScreen
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.menu.DraftPopup
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.menu.DroneModificationMenu
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.menu.PauseMenu
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.panel.BaseInformationPanel
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.panel.DroneSelectionPanel
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.panel.GeneralInformationPanel
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.panel.PlanetInformationPanel
import com.prophetsofprofit.galacticrush.logic.GamePhase
import com.prophetsofprofit.galacticrush.logic.base.Facility
import com.prophetsofprofit.galacticrush.logic.drone.Drone
import com.prophetsofprofit.galacticrush.logic.drone.DroneId
import com.prophetsofprofit.galacticrush.logic.drone.baseDroneImage
import com.prophetsofprofit.galacticrush.logic.map.Planet
import com.prophetsofprofit.galacticrush.networking.player.Player
import ktx.scene2d.Scene2DSkin
import kotlin.math.*


/**
 * The screen where all the playing will be done
 */
class MainGameScreen(game: Main, var player: Player) : GalacticRushScreen(game, Array(Gdx.files.internal("music/").list().size) { "${Gdx.files.internal("music/").list()[it]}" }) {

    //A convenience getter that gets the old game state of the player
    val oldGameState
        get() = this.player.oldGameState
    //A convenience getter for the game because the player's game will continuously change
    val mainGame
        get() = this.player.game
    //The smallest (closest) zoom factor allowed
    val minZoom = 0.1f
    //The highest (furthest) zoom factor allowed
    val maxZoom = 10f
    //The id of the planet currently selected by the player
    var selectedPlanetId: Int? = null
    //The id of the drone currently selected by the player
    var selectedDroneId: DroneId? = null
    //The planet currently selected by the player
    val selectedPlanet: Planet?
        get() = this.mainGame.galaxy.getPlanetWithId(this.selectedPlanetId ?: -1)
    //The drone currently selected by the player
    val selectedDrone: Drone?
        get() = this.mainGame.galaxy.getDroneWithId(this.selectedDroneId)
    //A bool that dictates whether or not the player is programming a drone
    //when programming, the modification menu is open
    var programming = false
    //The arrow textures used in indicating selected planets
    private val selectionArrowTextures = Array(8) { Texture("image/arrows/Arrow$it.png") }
    //The mechanism to handle panning the screen over a time
    val panHandler = PanHandler(this.game.camera)
    //The mechanism that handles animating turn transitions
    val turnAnimationHandler = TurnAnimationHandler(this)
    //The menu that is opened when the user wants to change their options
    val optionsMenu = OptionsMenu(game, this)
    //The menu that is opened when the game is paused
    val pauseMenu = PauseMenu(this)
    //The index of the change that is currently being animated
    var droneTurnAnimationIndex = 0
    //The font that is displayed when there is no label
    val font = BitmapFont()
    //The button that controls submitting turn changes
    val submitButton = TextButton("Submit", Scene2DSkin.defaultSkin).also {
        it.addAction(object : Action() {
            override fun act(delta: Float): Boolean {
                it.isDisabled = mainGame.phase != GamePhase.PLAYER_FREE_PHASE
                return false
            }
        })
    }
    //Store the images instead of reallocating memory every draw call
    val planetImages = this.mainGame.galaxy.planets.map { it.id to this.createPlanetTexture(it) }.toMap()

    /**
     * Initializes the main game screen by adding UI components and moving the camera to the player's home base
     */
    init {
        this.submitButton.setPosition(this.game.camera.viewportWidth, 0f, Align.bottomRight)
        this.submitButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                player.submitCurrentChanges()
            }
        })
        this.uiContainer.addActor(GeneralInformationPanel(this))
        this.uiContainer.addActor(BaseInformationPanel(this))
        this.uiContainer.addActor(DroneSelectionPanel(this))
        this.uiContainer.addActor(DroneModificationMenu(this))
        this.uiContainer.addActor(PlanetInformationPanel(this))
        this.uiContainer.addActor(DraftPopup(this))
        this.uiContainer.addActor(this.pauseMenu)
        this.uiContainer.addActor(this.optionsMenu)
        this.uiContainer.addActor(this.submitButton)
        //Finds the player's home base, moves the camera to be centered on the home base planet, and then zooms the camera in
        this.selectPlanet(this.mainGame.galaxy.planets.first { it.base != null && it.base!!.ownerId == this.player.id && it.base!!.facilityHealths.containsKey(Facility.HOME_BASE) })
    }

    /**
     * How the game is drawn
     * Draws the map on the bottom and then draws planets
     */
    override fun draw(delta: Float) {
        //Begins rendering the objects on the screen
        this.game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        //Render highways as white lines
        this.game.shapeRenderer.color = Color.LIGHT_GRAY
        for (highway in this.mainGame.galaxy.highways) {
            val planet0 = this.mainGame.galaxy.getPlanetWithId(highway.p0)!!
            val planet1 = this.mainGame.galaxy.getPlanetWithId(highway.p1)!!
            this.game.shapeRenderer.line(planet0.x * this.game.camera.viewportWidth, planet0.y * this.game.camera.viewportHeight, planet1.x * this.game.camera.viewportWidth, planet1.y * this.game.camera.viewportHeight)
        }
        this.game.shapeRenderer.end()
        //Render home bases as filled circles
        //TODO: add textures for planets, make planet size
        for (planet in this.mainGame.galaxy.planets) {
            this.game.batch.begin()
            this.game.batch.draw(
                    this.planetImages[planet.id],
                    planet.x * this.game.camera.viewportWidth
                            - 25 * planet.radius * sqrt(this.game.camera.viewportWidth.pow(2) + this.game.camera.viewportHeight.pow(2)),
                    planet.y * this.game.camera.viewportHeight
                            - 25 * planet.radius * sqrt(this.game.camera.viewportWidth.pow(2) + this.game.camera.viewportHeight.pow(2)),
                    //TODO: Avoid hardcoding size with magic constants, trim planet textures
                    50 * planet.radius * sqrt(this.game.camera.viewportWidth.pow(2) + this.game.camera.viewportHeight.pow(2)),
                    50 * planet.radius * sqrt(this.game.camera.viewportWidth.pow(2) + this.game.camera.viewportHeight.pow(2))
            )
            this.game.batch.end()
        }
        //Draw arrows pointing at the selected planet
        this.turnAnimationHandler.update(delta)
        this.game.batch.begin()
        this.drawTurnChanges()
        this.drawSelectionArrows()
        this.drawDrones()
        this.game.batch.end()
        this.panHandler.update(delta)
        if (this.selectedPlanet?.drones?.contains(this.selectedDrone) != true) {
            this.selectedDroneId = null
        }
        if (this.mainGame.droneTurnChanges.isNotEmpty() && this.droneTurnAnimationIndex < this.mainGame.droneTurnChanges.size) {
            if (this.turnAnimationHandler.currentlyMoving.isEmpty())
                this.animateChange()
        } else {
            this.mainGame.droneTurnChanges.clear()
            this.droneTurnAnimationIndex = 0
        }
    }

    /**
     * Draws all sprites that are being handles by the turn handler
     */
    private fun drawTurnChanges() {
        this.turnAnimationHandler.currentlyMoving.keys.forEach { it.draw(this.game.batch) }
    }

    /**
     * Indicates that the selected planet is selected
     */
    private fun drawSelectionArrows() {
        if (this.selectedPlanet == null) {
            return
        }
        this.game.batch.draw(this.selectionArrowTextures[0],
                this.selectedPlanet!!.x * this.game.camera.viewportWidth - 5,
                (this.selectedPlanet!!.y - 0.02f * this.game.camera.viewportWidth * this.selectedPlanet!!.radius) * this.game.camera.viewportHeight - 10)
        this.game.batch.draw(this.selectionArrowTextures[1],
                (this.selectedPlanet!!.x + 0.02f * this.game.camera.viewportHeight * this.selectedPlanet!!.radius) * this.game.camera.viewportWidth,
                (this.selectedPlanet!!.y - 0.02f * this.game.camera.viewportWidth * this.selectedPlanet!!.radius) * this.game.camera.viewportHeight - 10)
        this.game.batch.draw(this.selectionArrowTextures[2],
                (this.selectedPlanet!!.x + 0.02f * this.game.camera.viewportHeight * this.selectedPlanet!!.radius) * this.game.camera.viewportWidth,
                this.selectedPlanet!!.y * this.game.camera.viewportHeight - 5)
        this.game.batch.draw(this.selectionArrowTextures[3],
                (this.selectedPlanet!!.x + 0.02f * this.game.camera.viewportHeight * this.selectedPlanet!!.radius) * this.game.camera.viewportWidth,
                (this.selectedPlanet!!.y + 0.02f * this.game.camera.viewportWidth * this.selectedPlanet!!.radius) * this.game.camera.viewportHeight)
        this.game.batch.draw(this.selectionArrowTextures[4],
                this.selectedPlanet!!.x * this.game.camera.viewportWidth - 5,
                (this.selectedPlanet!!.y + 0.02f * this.game.camera.viewportWidth * this.selectedPlanet!!.radius) * this.game.camera.viewportHeight)
        this.game.batch.draw(this.selectionArrowTextures[5],
                (this.selectedPlanet!!.x - 0.02f * this.game.camera.viewportHeight * this.selectedPlanet!!.radius) * this.game.camera.viewportWidth - 10,
                (this.selectedPlanet!!.y + 0.02f * this.game.camera.viewportWidth * this.selectedPlanet!!.radius) * this.game.camera.viewportHeight)
        this.game.batch.draw(this.selectionArrowTextures[6],
                (this.selectedPlanet!!.x - 0.02f * this.game.camera.viewportHeight * this.selectedPlanet!!.radius) * this.game.camera.viewportWidth - 10,
                this.selectedPlanet!!.y * this.game.camera.viewportHeight - 5)
        this.game.batch.draw(this.selectionArrowTextures[7],
                (this.selectedPlanet!!.x - 0.02f * this.game.camera.viewportHeight * this.selectedPlanet!!.radius) * this.game.camera.viewportWidth - 10,
                (this.selectedPlanet!!.y - 0.02f * this.game.camera.viewportWidth * this.selectedPlanet!!.radius) * this.game.camera.viewportHeight - 10)
    }

    /**
     * Indicates the drones on each planet
     */
    private fun drawDrones() {
        for (planet in this.mainGame.galaxy.planets) {
            this.game.batch.draw(
                    baseDroneImage,
                    planet.x * this.game.camera.viewportWidth - baseDroneImage.width / 3,
                    planet.y * this.game.camera.viewportHeight - baseDroneImage.height / 3,
                    baseDroneImage.width / 1.5f,
                    baseDroneImage.height / 1.5f
            )
            //Write how many drones it has
            this.font.draw(this.game.batch, planet.drones.size.toString(),
                    planet.x * this.game.camera.viewportWidth + this.font.spaceWidth / 2,
                    planet.y * this.game.camera.viewportHeight + this.font.xHeight)
        }
    }

    /**
     * Selects a planet and moves the camera to it and zooms in a little
     */
    fun selectPlanet(p: Planet) {
        this.selectedPlanetId = p.id
        this.panHandler.add(p.x * this.game.camera.viewportWidth - this.game.camera.position.x,
                p.y * this.game.camera.viewportHeight - this.game.camera.position.y,
                0.5f - this.game.camera.zoom,
                0.1f)
    }

    /**
     * Selects a drone and then selects the planet it is on
     */
    fun selectDrone(d: Drone) {
        this.selectedDroneId = d.id
        this.selectPlanet(this.mainGame.galaxy.getPlanetWithId(d.locationId)!!)
    }

    /**
     * Creates a texture for the specified planet
     */
    fun createPlanetTexture(p: Planet): Texture {
        return Texture(p.imagePath)
    }

    /**
     * Animate the difference between two game states
     */
    fun animateChange() {
        for (drone in this.mainGame.droneTurnChanges[this.droneTurnAnimationIndex].changedDrones) {
            //Get the most recent version of the drone
            val recentTime = this.mainGame.droneTurnChanges.slice(0 until this.droneTurnAnimationIndex).lastOrNull { it.changedDrones.any { it.id == drone.id } }
            var location = this.oldGameState.drones.first { it.id == drone.id }.locationId
            if (recentTime != null) {
                location = recentTime.changedDrones.first { it.id == drone.id }.locationId
            }
            //Update the drone's position with an animation if it moved
            if (drone.locationId != location) {
                this.turnAnimationHandler.move(drone, this.oldGameState.galaxy.getPlanetWithId(location)!!, this.mainGame.galaxy.getPlanetWithId(drone.locationId)!!)
            }
        }
        this.droneTurnAnimationIndex++
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
        this.setZoomClamped(this.game.camera.zoom + amount * 0.1f)
        return true
    }

    /**
     * When the screen is tapped, check to see if any planets are selected
     * Will only consume the tap event if a planet is selected
     */
    override fun tap(x: Float, y: Float, count: Int, button: Int): Boolean {
        val mouseLocation = this.game.windowToCamera(x.roundToInt(), y.roundToInt(), this.uiCamera)
        if (this.uiContainer.hit(mouseLocation.x, mouseLocation.y, false) != null) {
            return false
        }
        //Leaving this here for convenience, in case we want to disable centering on planet when selecting it
        this.selectedPlanetId = this.mainGame.galaxy.planets.firstOrNull {
            sqrt((this.game.windowToCamera(x.toInt(), y.toInt()).x / this.game.camera.viewportWidth - it.x).pow(2)
                    + (this.game.windowToCamera(x.toInt(), y.toInt()).y / this.game.camera.viewportHeight - it.y).pow(2)) < it.radius * 10
        }?.id
        if (this.selectedPlanetId == null) {
            this.selectedDroneId = null
        }
        if (this.selectedPlanet != null) this.selectPlanet(this.selectedPlanet!!)
        return this.selectedPlanet != null
    }

    /**
     * When the escape or back keys are pressed, a pause menu appears
     */
    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            if (this.pauseMenu.isVisible) {
                this.pauseMenu.disappear(Direction.POP, 1f)
            } else {
                this.pauseMenu.appear(Direction.POP, 1f)
            }
            return true
        }
        return false
    }

    /**
     * Does nothing on leave
     */
    override fun leave() {
        this.font.dispose()
        this.game.resetCamera()
    }

}
