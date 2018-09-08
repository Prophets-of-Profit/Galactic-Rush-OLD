package com.prophetsofprofit.galacticrush.graphics.screen.experimental

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.prophetsofprofit.galacticrush.Main
import com.prophetsofprofit.galacticrush.graphics.ModalWindow
import com.prophetsofprofit.galacticrush.graphics.screen.GalacticRushScreen
import com.prophetsofprofit.galacticrush.logic.GamePhase
import com.prophetsofprofit.galacticrush.networking.player.Player
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * The main screen where the game is actually played
 * Is how the user interacts with the game logic and rules
 */
class GameScreen(game: Main, val player: Player) : GalacticRushScreen(game, Array(Gdx.files.internal("music/").list().size) { "${Gdx.files.internal("music/").list()[it]}" }) {

    //The game to represent
    val mainGame get() = this.player.game
    //A getter that gets the phase of the game: doesn't necessarily match the phase of the game because this may be animating a phase while the game has moved on
    val phase get() = if (this.mainGame.droneTurnChanges.isNotEmpty()) GamePhase.DRONE_PHASE else this.mainGame.phase
    //An object to make textures for game objects
    val textureHandler = TextureHandler()
    //The camera zoom bounds for the screen
    val minZoom = 0.1f
    val maxZoom = 10f
    //The currently opened modal windows
    val activeModals get() = this.uiContainer.actors.filter { it is ModalWindow && it.isVisible }
    //The images of each planet
    val planetImages = this.mainGame.galaxy.planets.map { it.id to this.textureHandler.getTexture(it) }.toMap()

    /**
     * The procedure for drawing the current game state
     */
    override fun draw(delta: Float) {
        //Draws highways
        this.game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        this.game.shapeRenderer.color = Color.LIGHT_GRAY
        for (highway in this.mainGame.galaxy.highways) {
            val planet0 = this.mainGame.galaxy.getPlanetWithId(highway.p0)!!
            val planet1 = this.mainGame.galaxy.getPlanetWithId(highway.p1)!!
            this.game.shapeRenderer.line(planet0.x * this.game.camera.viewportWidth, planet0.y * this.game.camera.viewportHeight, planet1.x * this.game.camera.viewportWidth, planet1.y * this.game.camera.viewportHeight)
        }
        this.game.shapeRenderer.end()

        //Draws planets
        this.game.batch.begin()
        for (planet in this.mainGame.galaxy.planets) {
            this.game.batch.draw(
                    this.planetImages[planet.id],
                    planet.x * this.game.camera.viewportWidth,
                    planet.y * this.game.camera.viewportHeight,
                    //TODO: Avoid hardcoding size with magic constants, trim planet textures
                    50 * planet.radius * sqrt(this.game.camera.viewportWidth.pow(2) + this.game.camera.viewportHeight.pow(2)),
                    50 * planet.radius * sqrt(this.game.camera.viewportWidth.pow(2) + this.game.camera.viewportHeight.pow(2))
            )
        }
        this.game.batch.end()
    }

    /**
     * The procedure for what happens when this game screen is left
     */
    override fun leave() {
        this.game.resetCamera()
    }

}