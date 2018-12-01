package com.prophetsofprofit.galacticrush.graphics.screens.menu

import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Align
import com.prophetsofprofit.galacticrush.Main
import com.prophetsofprofit.galacticrush.graphics.GalacticRushScreen
import com.prophetsofprofit.galacticrush.graphics.OptionsWindow
import com.prophetsofprofit.galacticrush.graphics.onClick
import ktx.scene2d.Scene2DSkin

/**
 * Allows user to enter the game or select what they want to do
 */
class MainMenuScreen(main: Main) : GalacticRushScreen(main) {

    /**
     * Adds all the menu buttons to the stage
     */
    init {
        /*
         * OPTIONS BUTTON
         */
        val optionsButton = TextButton("Options", Scene2DSkin.defaultSkin)
        optionsButton.setSize(this.uiContainer.width * 0.3f, this.uiContainer.height * 0.1f)
        optionsButton.setPosition(this.uiContainer.width * 0.025f, this.uiContainer.height * 0.025f, Align.bottomLeft)
        this.uiContainer.addActor(optionsButton)
        optionsButton.onClick { this.uiContainer.addActor(OptionsWindow(this.main)) }

        /*
         * JOIN GAME BUTTON
         */
        val joinGameButton = TextButton("Join Game", Scene2DSkin.defaultSkin)
        joinGameButton.setSize(this.uiContainer.width * 0.3f, this.uiContainer.height * 0.1f)
        joinGameButton.setPosition(this.uiContainer.width * 0.025f, optionsButton.top + this.uiContainer.height * 0.025f, Align.bottomLeft)
        this.uiContainer.addActor(joinGameButton)
        joinGameButton.onClick { /* TODO: Make */ }

        /*
         * HOST GAME BUTTON
         */
        val hostGameButton = TextButton("Host Game", Scene2DSkin.defaultSkin)
        hostGameButton.setSize(this.uiContainer.width * 0.3f, this.uiContainer.height * 0.1f)
        hostGameButton.setPosition(this.uiContainer.width * 0.025f, joinGameButton.top + this.uiContainer.height * 0.025f, Align.bottomLeft)
        this.uiContainer.addActor(hostGameButton)
        hostGameButton.onClick { /* TODO: Make */ }
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