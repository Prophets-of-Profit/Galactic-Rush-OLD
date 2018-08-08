package com.prophetsofprofit.galacticrush.graphics.screen.maingame

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.prophetsofprofit.galacticrush.Main
import com.prophetsofprofit.galacticrush.graphics.screen.GalacticRushScreen
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.panel.GeneralInformationPanel
import com.prophetsofprofit.galacticrush.logic.base.Facility
import com.prophetsofprofit.galacticrush.logic.drone.baseDroneImage
import com.prophetsofprofit.galacticrush.logic.map.Planet
import com.prophetsofprofit.galacticrush.logic.player.Player
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
    val maxZoom = 1f
    //The id of the planet currently selected by the player
    var selectedPlanetId: Int? = null
    //The planet currently selected by the player
    val selectedPlanet: Planet?
        get() = this.mainGame.galaxy.getPlanetWithId(this.selectedPlanetId ?: -1)
    //The arrow textures used in indicating selected planets
    private val selectionArrowTextures = Array(8) { Texture("image/arrows/Arrow$it.png") }
    //The mechanism to handle panning the screen over a time
    val panHandler = PanHandler(this.game.camera)
    //The mechanism that handles animating turn transitions
    val turnAnimationHandler = TurnAnimationHandler(this)
    //The change that is currently being animated
    var turnAnimationPointer = 0
    //The font that is displayed when there is no label
    val font = BitmapFont()

    /**
     * Initializes the main game screen by adding UI components and moving the camera to the player's home base
     */
    init {
        this.uiContainer.addActor(GeneralInformationPanel(this))
        //Finds the player's home base, moves the camera to be centered on the home base planet, and then zooms the camera in
        this.selectPlanet(this.mainGame.galaxy.planets.first { it.base != null && it.base!!.ownerId == this.player.id &&  it.base!!.facilityHealths.containsKey(Facility.HOME_BASE) })
    }

    /**
     * How the game is drawn
     * Draws the map on the bottom and then draws planets
     */
    override fun draw(delta: Float) {
        //Begins rendering the objects on the screen
        this.game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        //Render highways as white lines
        this.game.shapeRenderer.color = Color.WHITE
        for (highway in this.mainGame.galaxy.highways) {
            val planet0 = this.mainGame.galaxy.getPlanetWithId(highway.p0)!!
            val planet1 = this.mainGame.galaxy.getPlanetWithId(highway.p1)!!
            this.game.shapeRenderer.line(planet0.x * this.game.camera.viewportWidth, planet0.y * this.game.camera.viewportHeight, planet1.x * this.game.camera.viewportWidth, planet1.y * this.game.camera.viewportHeight)
        }
        this.game.shapeRenderer.end()
        //Render planets as colored circles
        //TODO: add textures for planets, make planet size
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        for (planet in this.mainGame.galaxy.planets) {
            if (planet.base != null && planet.base!!.facilityHealths.containsKey(Facility.HOME_BASE)) {
                this.game.shapeRenderer.color = this.mainGame.playerColors[planet.base!!.ownerId]
                this.game.shapeRenderer.circle(planet.x * this.game.camera.viewportWidth, planet.y * this.game.camera.viewportHeight, 15 * planet.radius * sqrt(this.game.camera.viewportWidth.pow(2) + this.game.camera.viewportHeight.pow(2)))
            }
            this.game.shapeRenderer.color = planet.color
            this.game.shapeRenderer.circle(planet.x * this.game.camera.viewportWidth, planet.y * this.game.camera.viewportHeight, 10 * planet.radius * sqrt(this.game.camera.viewportWidth.pow(2) + this.game.camera.viewportHeight.pow(2)))
            //this.game.batch.draw(baseDroneImage, planet.x * this.game.camera.viewportWidth, planet.y * this.game.camera.viewportHeight, this.game.camera.viewportWidth, this.game.camera.viewportHeight)
        }
        //Draw arrows pointing at the selected planet
        this.game.shapeRenderer.end()
        this.turnAnimationHandler.update(delta)
        this.game.batch.begin()
        this.drawTurnChanges()
        this.drawSelectionArrows()
        this.drawDrones()
        this.game.batch.end()
        this.panHandler.update(delta)
        if (this.mainGame.droneTurnChanges.isNotEmpty() && this.turnAnimationPointer < this.mainGame.droneTurnChanges.size) {
            if (this.turnAnimationHandler.currentlyMoving.isEmpty())
                this.animateChange()
        }
        else {
            this.mainGame.droneTurnChanges.clear()
            this.turnAnimationPointer = 0
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
     * Animate the difference between two game states
     */
    fun animateChange() {
        for (drone in this.mainGame.droneTurnChanges[this.turnAnimationPointer].changedDrones) {
            //Get the most recent version of the drone
            var recentTime = this.mainGame.droneTurnChanges.slice(0 until this.turnAnimationPointer)
                                .filter { it.changedDrones.any { it.id == drone.id } }
                                .lastOrNull()
            var location = this.oldGameState.drones.first { it.id == drone.id }.locationId
            if (recentTime != null) {
                location = recentTime.changedDrones.first { it.id == drone.id }.locationId
            }
            //Update the drone's position with an animation if it moved
            if (drone.locationId != location) {
                this.turnAnimationHandler.move(drone, this.oldGameState.galaxy.getPlanetWithId(location)!!, this.mainGame.galaxy.getPlanetWithId(drone.locationId)!!)
            }
        }
        this.turnAnimationPointer++
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
        if (this.selectedPlanet != null) this.selectPlanet(this.selectedPlanet!!)
        return this.selectedPlanet != null
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
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
