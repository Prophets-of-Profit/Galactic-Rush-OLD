package com.prophetsofprofit.galacticrush.graphics.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
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
    //The two panels on the side of the screen that are always displayed
    val planetLabel = Label("Planet Stats", Scene2DSkin.defaultSkin, "ui")
    val dronesList = Label("Drones", Scene2DSkin.defaultSkin, "ui")
    //The icons and labels for planet information
    val massIcon = ImageButton(Scene2DSkin.defaultSkin, "mass")
    val temperatureIcon = ImageButton(Scene2DSkin.defaultSkin, "temp")
    val atmosphereIcon = ImageButton(Scene2DSkin.defaultSkin, "atm")
    val humidityIcon = ImageButton(Scene2DSkin.defaultSkin, "humid")
    val solidityIcon = ImageButton(Scene2DSkin.defaultSkin, "solid")
    val massLabel = Label("", Scene2DSkin.defaultSkin, "small")
    val temperatureLabel = Label("", Scene2DSkin.defaultSkin, "small")
    val atmosphereLabel = Label("", Scene2DSkin.defaultSkin, "small")
    val humidityLabel = Label("", Scene2DSkin.defaultSkin, "small")
    val solidityLabel = Label("", Scene2DSkin.defaultSkin, "small")
    //Below are the menu buttons: end turn, quit game, etc
    val endTurnButton = TextButton("Submit", Scene2DSkin.defaultSkin)
    val gameMenuButton = TextButton("Game Menu", Scene2DSkin.defaultSkin)
    //The arrow textures used in indicating selected planets
    private val selectionArrowTextures = Array(8) { Texture("image/arrows/Arrow$it.png") }
    //The menu that gets confirmation for quitting the game; consists of three parts
    val confirmationLabel = Label("Quit to main menu?", Scene2DSkin.defaultSkin, "ui")
    val confirmationButtonYes = TextButton("Yes", Scene2DSkin.defaultSkin)
    val confirmationButtonNo = TextButton("No", Scene2DSkin.defaultSkin)
    //The game menu for doing options, saving, quitting, etc.
    val menuBase = Label("", Scene2DSkin.defaultSkin, "ui")
    val menuResume = TextButton("Resume", Scene2DSkin.defaultSkin)
    val menuOptions = TextButton("Options", Scene2DSkin.defaultSkin)
    val menuSave = TextButton("Save", Scene2DSkin.defaultSkin)
    val menuQuit = TextButton("Quit", Scene2DSkin.defaultSkin)
    //The font that is displayed when there is no label
    val font = BitmapFont()

    init {
        this.dronesList.width = this.game.camera.viewportWidth / 8
        this.dronesList.height = this.game.camera.viewportHeight / 3
        this.dronesList.setPosition(this.game.camera.viewportWidth - this.dronesList.width,
                this.game.camera.viewportHeight / 3)
        this.dronesList.setAlignment(Align.top)
        this.planetLabel.width = this.game.camera.viewportWidth / 8
        this.planetLabel.height = this.game.camera.viewportHeight / 3
        this.planetLabel.setPosition(this.game.camera.viewportWidth - this.dronesList.width,
                this.game.camera.viewportHeight - this.planetLabel.height)
        this.planetLabel.setAlignment(Align.top)

        this.endTurnButton.setPosition(this.game.camera.viewportWidth - this.endTurnButton.width, 0f)
        this.gameMenuButton.setPosition(this.game.camera.viewportWidth - this.endTurnButton.width, this.endTurnButton.height)
        this.endTurnButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                player.submitChanges()
            }
        })
        this.gameMenuButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                menuBase.isVisible = true
                menuResume.isVisible = true
                menuOptions.isVisible = true
                menuSave.isVisible = true
                menuQuit.isVisible = true
            }
        })

        this.massIcon.setPosition(this.planetLabel.x + this.massIcon.width / 5f, this.planetLabel.y + 9f * this.massIcon.height)
        this.temperatureIcon.setPosition(this.planetLabel.x + this.temperatureIcon.width / 5f, this.planetLabel.y + 7f * this.temperatureIcon.height)
        this.atmosphereIcon.setPosition(this.planetLabel.x + this.atmosphereIcon.width / 5f, this.planetLabel.y + 5f * this.atmosphereIcon.height)
        this.humidityIcon.setPosition(this.planetLabel.x + this.humidityIcon.width / 5f, this.planetLabel.y + 3f * this.humidityIcon.height)
        this.solidityIcon.setPosition(this.planetLabel.x + this.solidityIcon.width / 5f, this.planetLabel.y + 1f * this.solidityIcon.height)
        this.massLabel.setPosition(this.planetLabel.x + 2f * this.massIcon.width, this.planetLabel.y + 9.5f * this.massIcon.height)
        this.temperatureLabel.setPosition(this.planetLabel.x + 2f * this.temperatureIcon.width, this.planetLabel.y + 7.5f * this.temperatureIcon.height)
        this.atmosphereLabel.setPosition(this.planetLabel.x + 2f * this.atmosphereIcon.width, this.planetLabel.y + 5.5f * this.atmosphereIcon.height)
        this.humidityLabel.setPosition(this.planetLabel.x + 2f * this.humidityIcon.width, this.planetLabel.y + 3.5f * this.humidityIcon.height)
        this.solidityLabel.setPosition(this.planetLabel.x + 2f * this.solidityIcon.width, this.planetLabel.y + 1.5f * this.solidityIcon.height)

        this.confirmationLabel.setAlignment(Align.top)
        this.confirmationLabel.width = this.game.camera.viewportWidth / 3
        this.confirmationLabel.height = this.game.camera.viewportHeight / 7
        this.confirmationLabel.setPosition(this.game.camera.viewportWidth / 2, this.game.camera.viewportHeight / 2, Align.center)
        this.confirmationButtonYes.setPosition(this.game.camera.viewportWidth / 2 - this.confirmationButtonYes.width,
                this.game.camera.viewportHeight / 2 - this.confirmationButtonYes.height)
        this.confirmationButtonNo.setPosition(this.game.camera.viewportWidth / 2,
                this.game.camera.viewportHeight / 2 - this.confirmationButtonYes.height)
        this.confirmationLabel.isVisible = false
        this.confirmationButtonYes.isVisible = false
        this.confirmationButtonNo.isVisible = false
        this.confirmationButtonYes.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                game.screen = MainMenuScreen(game)
                dispose()
            }
        })
        this.confirmationButtonNo.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                confirmationLabel.isVisible = false
                confirmationButtonYes.isVisible = false
                confirmationButtonNo.isVisible = false
                menuBase.isVisible = true
                menuResume.isVisible = true
                menuOptions.isVisible = true
                menuSave.isVisible = true
                menuQuit.isVisible = true
            }
        })

        this.menuBase.setAlignment(Align.top)
        this.menuBase.width = this.game.camera.viewportWidth / 5
        this.menuBase.height = this.game.camera.viewportHeight / 2
        this.menuBase.setPosition(this.game.camera.viewportWidth / 2, this.game.camera.viewportHeight / 2, Align.center)
        this.menuResume.setPosition(this.game.camera.viewportWidth / 2,
                this.game.camera.viewportHeight / 2 + 3 * this.menuResume.height, Align.center)
        this.menuOptions.setPosition(this.game.camera.viewportWidth / 2,
                this.game.camera.viewportHeight / 2 + this.menuOptions.height, Align.center)
        this.menuSave.setPosition(this.game.camera.viewportWidth / 2,
                this.game.camera.viewportHeight / 2 - this.menuSave.height, Align.center)
        this.menuQuit.setPosition(this.game.camera.viewportWidth / 2,
                this.game.camera.viewportHeight / 2 - 3 * this.menuSave.height, Align.center)
        this.menuBase.isVisible = false
        this.menuResume.isVisible = false
        this.menuOptions.isVisible = false
        this.menuSave.isVisible = false
        this.menuQuit.isVisible = false
        this.menuResume.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                menuBase.isVisible = false
                menuResume.isVisible = false
                menuOptions.isVisible = false
                menuSave.isVisible = false
                menuQuit.isVisible = false
            }
        })
        this.menuOptions.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                game.screen = OptionsScreen(game) { game.screen = MainGameScreen(game, player) }
            }
        })
        this.menuSave.addListener(object : ClickListener() {
            //TODO
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
            }
        })
        this.menuQuit.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                confirmationLabel.isVisible = true
                confirmationButtonYes.isVisible = true
                confirmationButtonNo.isVisible = true
                menuBase.isVisible = false
                menuResume.isVisible = false
                menuOptions.isVisible = false
                menuSave.isVisible = false
                menuQuit.isVisible = false
            }
        })

        this.uiContainer.addActor(this.planetLabel)
        this.uiContainer.addActor(this.dronesList)
        this.uiContainer.addActor(this.massIcon)
        this.uiContainer.addActor(this.temperatureIcon)
        this.uiContainer.addActor(this.atmosphereIcon)
        this.uiContainer.addActor(this.humidityIcon)
        this.uiContainer.addActor(this.solidityIcon)
        this.uiContainer.addActor(this.massLabel)
        this.uiContainer.addActor(this.temperatureLabel)
        this.uiContainer.addActor(this.atmosphereLabel)
        this.uiContainer.addActor(this.humidityLabel)
        this.uiContainer.addActor(this.solidityLabel)
        this.uiContainer.addActor(this.endTurnButton)
        this.uiContainer.addActor(this.gameMenuButton)
        this.uiContainer.addActor(this.confirmationLabel)
        this.uiContainer.addActor(this.confirmationButtonYes)
        this.uiContainer.addActor(this.confirmationButtonNo)
        this.uiContainer.addActor(this.menuBase)
        this.uiContainer.addActor(this.menuResume)
        this.uiContainer.addActor(this.menuOptions)
        this.uiContainer.addActor(this.menuSave)
        this.uiContainer.addActor(this.menuQuit)
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
        menuBase.isVisible = false
        menuResume.isVisible = false
        menuOptions.isVisible = false
        menuSave.isVisible = false
        menuQuit.isVisible = false
        this.selectedPlanet = this.mainGame.galaxy.planets.firstOrNull {
            sqrt((this.game.windowToCamera(x.toInt(), y.toInt()).x / this.game.camera.viewportWidth - it.x).pow(2)
                    + (this.game.windowToCamera(x.toInt(), y.toInt()).y / this.game.camera.viewportHeight - it.y).pow(2)) < it.radius * 10
        }
        if (this.selectedPlanet != null) {
            this.massLabel.setText(Attribute.MASS.stringValue(this.selectedPlanet!!.attributes[Attribute.MASS]!!))
            this.temperatureLabel.setText(Attribute.TEMPERATURE.stringValue(this.selectedPlanet!!.attributes[Attribute.TEMPERATURE]!!))
            this.atmosphereLabel.setText(Attribute.ATMOSPHERE.stringValue(this.selectedPlanet!!.attributes[Attribute.ATMOSPHERE]!!))
            this.humidityLabel.setText(Attribute.WATER.stringValue(this.selectedPlanet!!.attributes[Attribute.WATER]!!))
            this.solidityLabel.setText(Attribute.SOLIDITY.stringValue(this.selectedPlanet!!.attributes[Attribute.SOLIDITY]!!))
        } else {
            this.massLabel.setText("")
            this.temperatureLabel.setText("")
            this.atmosphereLabel.setText("")
            this.humidityLabel.setText("")
            this.solidityLabel.setText("")
        }
        return this.selectedPlanet != null
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            menuBase.isVisible = !menuBase.isVisible
            menuResume.isVisible = !menuResume.isVisible
            menuOptions.isVisible = !menuOptions.isVisible
            menuSave.isVisible = !menuSave.isVisible
            menuQuit.isVisible = !menuQuit.isVisible
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
