package com.prophetsofprofit.galacticrush.graphics.screen.maingame.panel

import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import com.prophetsofprofit.galacticrush.graphics.Direction
import com.prophetsofprofit.galacticrush.graphics.Panel
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.MainGameScreen
import com.prophetsofprofit.galacticrush.logic.base.Base
import com.prophetsofprofit.galacticrush.logic.base.Facility
import ktx.scene2d.Scene2DSkin

/**
 * Contains a single button: to purchase a base
 */
class BasePurchasePanel(gameScreen: MainGameScreen) : Panel(gameScreen, "Base Information", 0f, 0.9f * gameScreen.uiCamera.viewportHeight, 0.25f * gameScreen.uiCamera.viewportWidth, 0.1f * gameScreen.uiCamera.viewportHeight, Align.topLeft) {

    /**
     * Initializes the BasePurchasePanel with all of the widgets and positionings and behaviours
     */
    init {

        //The button to buy the base
        val createBaseButton = TextButton("Purchase Base", Scene2DSkin.defaultSkin).also {
            it.align(Align.center)
            it.addAction(object : Action() {
                override fun act(delta: Float): Boolean {
                    if (!canBeUsed)
                        return false
                    it.isDisabled = gameScreen.mainGame.money[gameScreen.player.id]!! < Facility.BASE.cost
                    return false
                }
            })
            it.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    gameScreen.mainGame.money[gameScreen.player.id] = gameScreen.mainGame.money[gameScreen.player.id]!! - Facility.BASE.cost
                    gameScreen.selectedPlanet!!.base = Base(gameScreen.player.id, gameScreen.selectedPlanetId!!, arrayOf(Facility.BASE))
                    gameScreen.player.currentChanges.add(gameScreen.selectedPlanet!!)
                }
            })
        }


        //Sets up the panel to appear whenever a planet without a base (but with at least one drone owned by the player and no
        // drones owned by other players) is selected, and disappear otherwise
        this.addAction(object : Action() {
            override fun act(delta: Float): Boolean {
                if (!canBeUsed) {
                    return false
                }
                if (gameScreen.selectedPlanet?.base != null || gameScreen.selectedPlanet == null) {
                    disappear(Direction.LEFT, 1f)
                } else if (gameScreen.selectedPlanet?.base == null
                        && gameScreen.selectedPlanet!!.drones.isNotEmpty()
                        && gameScreen.selectedPlanet!!.drones.all { it.ownerId == gameScreen.player.id}) {
                    children.forEach { it.act(delta) }
                    appear(Direction.LEFT, 1f)
                }
                return false
            }
        })

        this.add(createBaseButton).expandY().row()
    }

}
