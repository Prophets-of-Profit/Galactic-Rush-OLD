package com.prophetsofprofit.galacticrush.graphics.screens.menu

import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Align
import com.prophetsofprofit.galacticrush.Main
import com.prophetsofprofit.galacticrush.graphics.GalacticRushScreen
import ktx.scene2d.Scene2DSkin

/**
 * Allows user to enter the game or select what they want to do
 */
class MainMenuScreen(main: Main) : GalacticRushScreen(main) {

    /**
     * Adds all the menu buttons to the stage
     */
    init {
        val hostGameButton = TextButton("Host Game", Scene2DSkin.defaultSkin)
        hostGameButton.setPosition(100f, 100f, Align.center)
        this.uiContainer.addActor(hostGameButton)
    }

    /**
     * How the main menu is drawn; main menu doesn't do anything special
     */
    override fun draw(delta: Float) {}

    /**
     * Main Menu doesn't dispose of any resources
     */
    override fun leave() {}

}