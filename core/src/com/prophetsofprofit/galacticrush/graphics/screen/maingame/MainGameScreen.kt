package com.prophetsofprofit.galacticrush.graphics.screen.maingame

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.prophetsofprofit.galacticrush.Main
import com.prophetsofprofit.galacticrush.graphics.screen.GalacticRushScreen
import com.prophetsofprofit.galacticrush.graphics.screen.MainMenuScreen
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.overlay.Overlay
import com.prophetsofprofit.galacticrush.logic.Game
import com.prophetsofprofit.galacticrush.logic.drone.baseDroneImage
import com.prophetsofprofit.galacticrush.logic.facility.HomeBase
import com.prophetsofprofit.galacticrush.logic.map.Planet
import com.prophetsofprofit.galacticrush.logic.player.Player
import kotlin.math.*


/**
 * The screen where all the playing will be done
 */
class MainGameScreen(game: Main, var player: Player) : GalacticRushScreen(game, Array(Gdx.files.internal("music/").list().size) { "" + Gdx.files.internal("music/").list()[it] }) {

    //A variable that stores an older game state if one exists; TODO: animate difference between mainGame and oldGameState
    var oldGameState: Game? = null
    //A convenience getter for the game because the player's game will continuously change
    val mainGame: Game
        get() = this.player.game
    //The smallest (closest) zoom factor allowed
    val minZoom = 0.1f
    //The highest (furthest) zoom factor allowed
    val maxZoom = 1f
    //The planet currently selected by the player
    var selectedPlanet: Planet? = null
    //The arrow textures used in indicating selected planets
    private val selectionArrowTextures = Array(8) { Texture("image/arrows/Arrow$it.png") }
    //The permanent overlay of the game screen; see Overlay
    val overlay = Overlay(this)
    //The game menu for handling options and quitting, etc
    val gameMenu = PauseMenu(this)
    //The confirmation menu for quitting
    val quitConfirmation = ConfirmationMenu(this, "Quit game?", this.gameMenu) { game.screen = MainMenuScreen(game); dispose() }
    //The confirmation menu for submitting
    val submitConfirmation = ConfirmationMenu(this, "Submit?", this.overlay) { player.submitChanges() }
    //The font that is displayed when there is no label
    val font = BitmapFont()

    /**
     * Initializes the main game screen by adding UI components and moving the camera to the player's home base
     */
    init {
        this.gameMenu.isVisible = false
        this.uiContainer.addActor(this.overlay)
        this.uiContainer.addActor(this.gameMenu)
        this.uiContainer.addActor(this.quitConfirmation)
        this.uiContainer.addActor(this.submitConfirmation)
        //Finds the player's home base, moves the camera to be centered on the home base planet, and then zooms the camera in
        val homeBaseLocation = this.mainGame.galaxy.planets.first { it.facilities.firstOrNull { it is HomeBase && it.ownerId == this.player.id } != null }
        this.game.camera.translate(homeBaseLocation.x * this.game.camera.viewportWidth - this.game.camera.viewportWidth / 2, homeBaseLocation.y * this.game.camera.viewportHeight - this.game.camera.viewportHeight / 2)
        this.game.camera.zoom = 0.5f
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
            this.game.shapeRenderer.line(highway.p0.x * this.game.camera.viewportWidth, highway.p0.y * this.game.camera.viewportHeight, highway.p1.x * this.game.camera.viewportWidth, highway.p1.y * this.game.camera.viewportHeight)
        }
        this.game.shapeRenderer.end()
        //Render planets as colored circles
        //TODO: add textures for planets, make planet size
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        for (planet in this.mainGame.galaxy.planets) {
            if (planet.facilities.any { it is HomeBase }) {
                this.game.shapeRenderer.color = this.mainGame.playerColors[planet.facilities.first().ownerId]
                this.game.shapeRenderer.circle(planet.x * this.game.camera.viewportWidth, planet.y * this.game.camera.viewportHeight, 15 * planet.radius * sqrt(this.game.camera.viewportWidth.pow(2) + this.game.camera.viewportHeight.pow(2)))
            }
            this.game.shapeRenderer.color = planet.color
            this.game.shapeRenderer.circle(planet.x * this.game.camera.viewportWidth, planet.y * this.game.camera.viewportHeight, 10 * planet.radius * sqrt(this.game.camera.viewportWidth.pow(2) + this.game.camera.viewportHeight.pow(2)))
            //this.game.batch.draw(baseDroneImage, planet.x * this.game.camera.viewportWidth, planet.y * this.game.camera.viewportHeight, this.game.camera.viewportWidth, this.game.camera.viewportHeight)
        }
        //Draw arrows pointing at the selected planet
        this.game.shapeRenderer.end()
        this.game.batch.begin()
        this.drawSelectionArrows()
        this.drawDrones()
        this.game.batch.end()
        //Updates game information
        this.overlay.overlayInformation.update()
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
     * Panning moves the camera laterally to adjust what is being seen
     */
    override fun pan(x: Float, y: Float, deltaX: Float, deltaY: Float): Boolean {
        val mouseLocation = this.game.windowToCamera(x.roundToInt(), y.roundToInt(), this.uiContainer.camera)
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
        val mouseLocation = this.game.windowToCamera(x.roundToInt(), y.roundToInt(), this.uiContainer.camera)
        if (this.uiContainer.hit(mouseLocation.x, mouseLocation.y, false) != null) {
            return false
        }
        this.selectedPlanet = this.mainGame.galaxy.planets.firstOrNull {
            sqrt((this.game.windowToCamera(x.toInt(), y.toInt()).x / this.game.camera.viewportWidth - it.x).pow(2)
                    + (this.game.windowToCamera(x.toInt(), y.toInt()).y / this.game.camera.viewportHeight - it.y).pow(2)) < it.radius * 10
        }
        this.overlay.planetOverlay.update()
        return this.selectedPlanet != null
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            this.gameMenu.isVisible = !this.gameMenu.isVisible
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
