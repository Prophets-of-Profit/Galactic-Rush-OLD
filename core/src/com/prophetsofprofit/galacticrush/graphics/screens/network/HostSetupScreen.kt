package com.prophetsofprofit.galacticrush.graphics.screens.network

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.utils.Align
import com.prophetsofprofit.galacticrush.Main
import com.prophetsofprofit.galacticrush.defaultTcpPort
import com.prophetsofprofit.galacticrush.graphics.GalacticRushScreen
import com.prophetsofprofit.galacticrush.graphics.onClick
import com.prophetsofprofit.galacticrush.graphics.screens.menu.MainMenuScreen
import com.prophetsofprofit.galacticrush.networking.GalacticRushServer
import ktx.scene2d.Scene2DSkin

/**
 * A screen where a host can broadcast that they are available to play
 */
class HostSetupScreen(main: Main) : GalacticRushScreen(main) {

    /**
     * Adds all the UI elements to host setup screen
     */
    init {
        /*
         * TITLE LABEL
         */
        val titleLabel = Label("HOST A GAME", Scene2DSkin.defaultSkin, "title")
        titleLabel.setSize(this.uiContainer.width * 0.9f, titleLabel.height)
        titleLabel.setPosition(this.uiContainer.width / 2f, this.uiContainer.height * 0.9f, Align.center)
        titleLabel.setAlignment(Align.center)
        this.uiContainer.addActor(titleLabel)

        /*
         * PORT LABEL
         */
        val portLabel = Label("Port", Scene2DSkin.defaultSkin, "subtitle")
        portLabel.setSize(this.uiContainer.width * 0.1f, portLabel.height * 5)
        portLabel.setPosition(titleLabel.x, this.uiContainer.height * 0.7f)
        portLabel.setAlignment(Align.center)
        this.uiContainer.addActor(portLabel)

        /*
         * PORT FIELD
         */
        val portField = TextField("$defaultTcpPort", Scene2DSkin.defaultSkin)
        portField.setSize(this.uiContainer.width * 0.25f, portLabel.height)
        portField.setPosition(portLabel.right + this.uiContainer.width * 0.025f, portLabel.y)
        portField.setAlignment(Align.center)
        this.uiContainer.addActor(portField)

        /*
         * LOCK BUTTON
         */
        val lockButton = TextButton("Confirm", Scene2DSkin.defaultSkin)
        lockButton.setSize(this.uiContainer.width * 0.25f, portLabel.height)
        lockButton.setPosition(titleLabel.right, portField.y, Align.bottomRight)
        this.uiContainer.addActor(lockButton)
        lockButton.onClick {
            if (lockButton.text.toString() == "Confirm") {
                try {
                    GalacticRushServer.usePort(portField.text.toIntOrNull() ?: -1)
                    lockButton.setText("Cancel")
                } catch (e: Exception) {
                }
            } else {
                GalacticRushServer.close()
                lockButton.setText("Confirm")
            }
        }

        /*
         * BACK BUTTON
         */
        val backButton = TextButton("Back", Scene2DSkin.defaultSkin)
        backButton.setSize(this.uiContainer.width * 0.9f, backButton.height)
        backButton.setPosition(this.uiContainer.width / 2f, this.uiContainer.height * 0.1f, Align.center)
        this.uiContainer.addActor(backButton)
        backButton.onClick { this.main.screen = MainMenuScreen(this.main) }
    }

    override fun draw(delta: Float) {
    }

    override fun leave() {
    }

}
