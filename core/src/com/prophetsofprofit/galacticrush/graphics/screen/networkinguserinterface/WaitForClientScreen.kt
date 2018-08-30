package com.prophetsofprofit.galacticrush.graphics.screen.networkinguserinterface

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import com.prophetsofprofit.galacticrush.Main
import com.prophetsofprofit.galacticrush.graphics.screen.GalacticRushScreen
import com.prophetsofprofit.galacticrush.graphics.screen.MainMenuScreen
import com.prophetsofprofit.galacticrush.graphics.screen.loading.HostLoadingScreen
import com.prophetsofprofit.galacticrush.logic.GameCreationOptions
import com.prophetsofprofit.galacticrush.networking.GalacticRushServer
import com.prophetsofprofit.galacticrush.networking.player.LocalPlayer
import com.prophetsofprofit.galacticrush.networking.player.NetworkPlayer
import ktx.scene2d.Scene2DSkin

/**
 * The screen where the host of the game can wait for clients and set the game settings
 */
class WaitForClientScreen(game: Main) : GalacticRushScreen(game) {

    //The text field where the host enters in the hosting port
    val portTextField = PortTextField()
    //The button that either locks in the selected port or undoes the selection of the port
    val lockButton = TextButton("________________", Scene2DSkin.defaultSkin)
    //The field for inputting the size of the galaxy
    val galaxySizeField = TextField("100", Scene2DSkin.defaultSkin)

    /**
     * Initializes the networker as a host and also initializes GUI components
     */
    init {
        //Sets up portTextField
        this.portTextField.setPosition(this.uiContainer.width / 2, this.uiContainer.height * 0.85f, Align.center)
        this.portTextField.setAlignment(Align.center)

        //Sets up port field label
        val portLabel = Label("Port", Scene2DSkin.defaultSkin)
        portLabel.width = this.portTextField.width
        portLabel.setAlignment(Align.center)
        portLabel.setPosition(this.uiContainer.width / 2, this.uiContainer.height * 0.9f, Align.center)

        //Sets up the galaxy size text field
        this.galaxySizeField.maxLength = 5
        this.galaxySizeField.setAlignment(Align.center)
        this.galaxySizeField.setTextFieldFilter { _, newChar -> newChar.isDigit() }
        this.galaxySizeField.setTextFieldListener { _, _ ->
            //TODO: isn't working
            lockButton.isDisabled = try {
                this.galaxySizeField.text.toInt() < 1 || this.galaxySizeField.text.toInt() > 500
            } catch (ignored: Exception) {
                true
            }
        }
        this.galaxySizeField.setPosition(this.uiContainer.width / 2, this.uiContainer.height * 0.75f - portTextField.height * 1.5f, Align.center)

        //Sets up the galaxy size label
        val galaxySizeLabel = Label("Galaxy Size", Scene2DSkin.defaultSkin)
        galaxySizeLabel.width = galaxySizeField.width
        galaxySizeLabel.setAlignment(Align.center)
        galaxySizeLabel.setPosition(this.uiContainer.width / 2, this.uiContainer.height * 0.8f - portTextField.height * 1.5f, Align.center)

        //Sets up the lockButton
        val confirmPort = "Confirm Selection"
        val cancelSelection = "Cancel Selection"
        this.lockButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                //lockButton does nothing when disabled
                if (lockButton.isDisabled) {
                    return
                }
                lockButton.setText(if (portTextField.isDisabled) {
                    GalacticRushServer.close()
                    confirmPort
                } else {
                    GalacticRushServer.start()
                    GalacticRushServer.usePort(portTextField.text.toInt())
                    cancelSelection
                })
                galaxySizeField.isDisabled = !galaxySizeField.isDisabled
                portTextField.isDisabled = !portTextField.isDisabled
            }
        })
        this.lockButton.setText(confirmPort)
        this.lockButton.setPosition(this.uiContainer.width / 2, this.uiContainer.height * 0.5f - portTextField.height * 1.5f, Align.center)
        this.lockButton.align(Align.center)

        //Sets up cancelButton
        val cancelButton = TextButton("Cancel", Scene2DSkin.defaultSkin)
        cancelButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                game.screen = MainMenuScreen(game)
                dispose()
            }
        })
        cancelButton.setPosition(this.uiContainer.width * 0.1f, this.uiContainer.height * 0.1f, Align.center)

        //Adds all widgets
        this.uiContainer.addActor(portLabel)
        this.uiContainer.addActor(this.portTextField)
        this.uiContainer.addActor(galaxySizeLabel)
        this.uiContainer.addActor(this.galaxySizeField)
        this.uiContainer.addActor(this.lockButton)
        this.uiContainer.addActor(cancelButton)
    }

    /**
     * Checks whether a connection has been made and if it has, it moves to the next screen
     */
    override fun draw(delta: Float) {
        this.lockButton.isDisabled = !this.portTextField.isValid
        if (GalacticRushServer.connections.isEmpty()) {
            return
        }
        this.game.screen = HostLoadingScreen(this.game, arrayOf(LocalPlayer(0), NetworkPlayer(1, GalacticRushServer.connections.first().id)), GameCreationOptions(this.galaxySizeField.text.toInt()))
        this.dispose()
    }

    override fun leave() {}

}