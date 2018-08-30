package com.prophetsofprofit.galacticrush.graphics.screen.maingame.panel

import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.prophetsofprofit.galacticrush.graphics.Direction
import com.prophetsofprofit.galacticrush.graphics.Panel
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.MainGameScreen
import com.prophetsofprofit.galacticrush.logic.map.PlanetAttribute
import ktx.scene2d.Scene2DSkin

/**
 * The panel that will allow the user to view information about the current planet
 */
class PlanetInformationPanel(gameScreen: MainGameScreen) : Panel(gameScreen, "Planet Information", gameScreen.uiCamera.viewportWidth, 0.9f * gameScreen.uiCamera.viewportHeight, 0.25f * gameScreen.uiCamera.viewportWidth, 0.45f * gameScreen.uiCamera.viewportHeight, Align.topRight) {

    /**
     * Initializes the UI components and their positionings
     */
    init {
        fun makeLabelForAttribute(attribute: PlanetAttribute): Label {
            return Label("", Scene2DSkin.defaultSkin).also {
                it.setAlignment(Align.center)
                it.addAction(object : Action() {
                    override fun act(delta: Float): Boolean {
                        it.setText(if (gameScreen.selectedPlanet == null) "" else attribute.stringValue(gameScreen.selectedPlanet!!.attributes[attribute]!!))
                        return false
                    }
                })
            }
        }
        this.add(Image(Scene2DSkin.defaultSkin, "Atmosphere")).pad(10f)
        this.add(makeLabelForAttribute(PlanetAttribute.ATMOSPHERE)).expandX().fillX().padRight(10f).row()
        this.add(Image(Scene2DSkin.defaultSkin, "Humidity")).pad(10f)
        this.add(makeLabelForAttribute(PlanetAttribute.WATER)).expandX().fillX().padRight(10f).row()
        this.add(Image(Scene2DSkin.defaultSkin, "Mass")).pad(10f)
        this.add(makeLabelForAttribute(PlanetAttribute.MASS)).expandX().padRight(10f).fillX().row()
        this.add(Image(Scene2DSkin.defaultSkin, "Solidity")).pad(10f)
        this.add(makeLabelForAttribute(PlanetAttribute.SOLIDITY)).expandX().fillX().padRight(10f).row()
        this.add(Image(Scene2DSkin.defaultSkin, "Temperature")).pad(10f)
        this.add(makeLabelForAttribute(PlanetAttribute.TEMPERATURE)).expandX().fillX().padRight(10f)
        this.addAction(object : Action() {
            override fun act(delta: Float): Boolean {
                if (gameScreen.selectedPlanet != null) {
                    appear(Direction.RIGHT, 1f)
                } else {
                    disappear(Direction.RIGHT, 1f)
                }
                return false
            }
        })
    }

}