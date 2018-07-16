package com.prophetsofprofit.galacticrush.graphics.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.prophetsofprofit.galacticrush.Main
import com.prophetsofprofit.galacticrush.logic.Game
import com.prophetsofprofit.galacticrush.logic.drone.baseDroneImage
import com.prophetsofprofit.galacticrush.logic.map.Attribute
import com.prophetsofprofit.galacticrush.logic.map.Planet
import com.prophetsofprofit.galacticrush.logic.player.Player
import ktx.scene2d.Scene2DSkin
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * The screen where all the playing will be done
 */
class MainGameScreen(game: Main, var player: Player) : GalacticRushScreen(game, Array(Gdx.files.internal("music/").list().size) { "" + Gdx.files.internal("music/").list()[it] }) {

    //A variable that stores an older game state if one exists; TODO: animate difference between mainGame and oldGameState
    var oldGameState: Game? = null
    //A convenience getter for the game because the player's game will continuously change
    var mainGame = Game(arrayOf(), 0)
        get() = this.player.game
    //The smallest (closest) zoom factor allowed
    val minZoom = 0.1f
    //The highest (furthest) zoom factor allowed
    val maxZoom = 1f
    //The planet currently selected by the player
    var selectedPlanet: Planet? = null
    //The list of planet attributes TODO: get rid of leading newline and format properly
    var planetLabel = Label(Attribute.values().joinToString { "\n${it.toString().capitalize()}: ${this.mainGame.galaxy.planets[0].attributes[it]}" }, Scene2DSkin.defaultSkin)
    //The list of drones on the planet
    var dronesList = Label("Drones:\nDrone(000000000)\nDrone(000000000)\nDrone(000000000)\nDrone(000000000)\nDrone(000000000)\n", Scene2DSkin.defaultSkin)
    //The button which causes the player to submit their change
    val endTurnButton = TextButton("Submit", Scene2DSkin.defaultSkin)
    //The arrow textures used in indicating selected planets
    private val selectionArrowTextures = Array(8) { Texture("image/arrows/Arrow$it.png") }
    //The texture used as the base panels for the gui
    private val basePanelTexture = Texture("image/SidePanel.png")
    //The font that is displayed when there is no label
    val font = BitmapFont()

    init {
        this.planetLabel.setPosition(0f + this.planetLabel.width / 2, 0f + this.planetLabel.height / 2, Align.center)
        this.dronesList.setPosition(this.game.camera.viewportWidth - this.dronesList.width / 2, 0f + (this.dronesList.height / 2 + this.endTurnButton.height), Align.center)
        this.endTurnButton.setPosition(this.game.camera.viewportWidth - this.endTurnButton.width, 0f)
        this.planetLabel.setAlignment(Align.left)
        this.dronesList.setAlignment(Align.topLeft)
        this.planetLabel.isVisible = false
        this.dronesList.isVisible = false
        this.uiContainer.addActor(this.planetLabel)
        this.uiContainer.addActor(this.dronesList)
        this.uiContainer.addActor(this.endTurnButton)
        endTurnButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                player.submitChanges()
            }
        })

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
            this.game.shapeRenderer.color = planet.color
            this.game.shapeRenderer.circle(planet.x * this.game.camera.viewportWidth, planet.y * this.game.camera.viewportHeight, 10 * planet.radius * sqrt(this.game.camera.viewportWidth.pow(2) + this.game.camera.viewportHeight.pow(2)))
            //this.game.batch.draw(baseDroneImage, planet.x * this.game.camera.viewportWidth, planet.y * this.game.camera.viewportHeight, this.game.camera.viewportWidth, this.game.camera.viewportHeight)
        }
        //Draw arrows pointing at the selected planet
        this.game.shapeRenderer.end()
        this.game.batch.begin()
        this.drawSelectionArrows()
        this.drawDrones()
        this.drawUI()
        this.game.batch.end()
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
     * Draws the user interface to the screen
     * TODO:
     */
    private fun drawUI() {

    }

    /**
     * Panning moves the camera laterally to adjust what is being seen
     */
    override fun pan(x: Float, y: Float, deltaX: Float, deltaY: Float): Boolean {
        this.game.camera.translate(-deltaX, deltaY)
        return true
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
        this.setZoomClamped(initialDistance / distance)
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
        this.selectedPlanet = this.mainGame.galaxy.planets.firstOrNull {
            //it != this.selectedPlanet && /* This line of code will unselect a selected planet if clicked on again; TODO: discuss desired behaviour */
            sqrt((this.game.windowToCamera(x.toInt(), y.toInt()).x / this.game.camera.viewportWidth - it.x).pow(2)
                    + (this.game.windowToCamera(x.toInt(), y.toInt()).y / this.game.camera.viewportHeight - it.y).pow(2)) < it.radius * 10
        }
        if (this.selectedPlanet != null) {
            //Format the attributes nicely
            this.planetLabel.isVisible = true
            this.planetLabel.setText(
                    Attribute.values().map { it.toString().capitalize() + ": " + this.selectedPlanet!!.attributes[it] + "\n" }
                            .reduce { acc, it -> acc + it }
                            .toString()
            )
            this.dronesList.isVisible = true
            this.dronesList.setText(
                    if ((this.selectedPlanet as Planet).drones.isEmpty()) "Drones: \n None" else
                        "Drones: \n" + (this.selectedPlanet as Planet).drones.joinToString { "$it\n" }
            )
            //TODO: account for changing of camera viewport
            //this.planetLabel.setPosition(this.uiContainer.width * this.selectedPlanet!!.x + this.planetLabel.width * 2f, this.uiContainer.height * this.selectedPlanet!!.y, Align.center)
        } else {
            this.planetLabel.isVisible = false
            this.dronesList.isVisible = false
        }
        return this.selectedPlanet != null
    }

    /**
     * Does nothing on leave
     */
    override fun leave() {
        this.font.dispose()
    }

}
