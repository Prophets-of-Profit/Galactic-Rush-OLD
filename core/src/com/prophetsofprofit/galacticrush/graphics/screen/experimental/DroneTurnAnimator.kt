package com.prophetsofprofit.galacticrush.graphics.screen.experimental

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.Action
import com.prophetsofprofit.galacticrush.logic.GamePhase
import kotlin.math.PI
import kotlin.math.atan2

/**
 * An action that handles animating drone turns
 */
class DroneTurnAnimator(val gameScreen: GameScreen) : Action() {

    //Actions that are handling moving each drone using DroneMovementInfo
    val actions = mutableListOf<Action>()
    //How long it takes for each drone to reach its destination
    val travelTime = 1f

    /**
     * Checks if the game is in the droneturn phase and then tries animating it
     */
    override fun act(delta: Float): Boolean {
        if (this.gameScreen.phase != GamePhase.DRONE_PHASE) {
            return false
        }
        this.actions.removeAll { it.act(delta) }
        if (actions.isEmpty()) {
            this.gameScreen.mainGame.droneTurnChanges.removeAt(0)
            if (this.gameScreen.mainGame.droneTurnChanges.isNotEmpty()) {
                //TODO: add actions to animate drone actions (combat, base creation, mining, etc)
                //Adds actions to animate drone movement
                this.gameScreen.mainGame.droneTurnChanges.first().changedDrones.mapTo(actions) { drone ->
                    object : Action() {

                        val droneSprite = Sprite(gameScreen.textureHandler.getTexture(drone))
                        val droneOrigin = gameScreen.player.oldGameState.galaxy.getPlanetWithId(gameScreen.player.oldGameState.galaxy.getDroneWithId(drone.id)!!.locationId)!!
                        val droneEnding = gameScreen.mainGame.galaxy.getPlanetWithId(drone.locationId)!! //TODO: doesn't account for intermediate steps?
                        var elapsedTime = 0f
                        val xTotalChange = (droneEnding.x - droneOrigin.x) * gameScreen.game.camera.viewportWidth
                        val yTotalChange = (droneEnding.y - droneOrigin.y) * gameScreen.game.camera.viewportHeight

                        init {
                            this.droneSprite.setCenter(this.droneOrigin.x * gameScreen.game.camera.viewportWidth, this.droneOrigin.y * gameScreen.game.camera.viewportHeight)
                            this.droneSprite.rotate((atan2(yTotalChange, xTotalChange) * 180 / PI).toFloat())
                        }

                        override fun act(delta: Float): Boolean {
                            this.elapsedTime = minOf(elapsedTime + delta, travelTime)
                            this.droneSprite.setPosition(
                                    xTotalChange * elapsedTime / travelTime,
                                    yTotalChange * elapsedTime / travelTime
                            )
                            this.droneSprite.draw(gameScreen.game.batch)
                            return this.elapsedTime >= travelTime
                        }

                    }
                }
            }
        }
        return false
    }

}