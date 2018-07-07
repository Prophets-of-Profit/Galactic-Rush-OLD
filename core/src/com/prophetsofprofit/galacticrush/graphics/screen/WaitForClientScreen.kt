package com.prophetsofprofit.galacticrush.graphics.screen

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.prophetsofprofit.galacticrush.Main
import com.prophetsofprofit.galacticrush.Networker
import com.prophetsofprofit.galacticrush.defaultTcpPort
import ktx.scene2d.Scene2DSkin

/**
 * The screen where the host of the game can wait for clients and set the game settings
 */
class WaitForClientScreen(game: Main) : GalacticRushScreen(game) {

    //The text field where the host enters in the hosting port
    val portTextField = TextField("$defaultTcpPort", Scene2DSkin.defaultSkin)
    //The button that either locks in the selected port or undoes the selection of the port
    val lockButton = TextButton("________________", Scene2DSkin.defaultSkin)
    //The button to leave this screen
    val cancelButton = TextButton("Cancel", Scene2DSkin.defaultSkin)

    /**
     * Initializes the networker as a host and also initializes GUI components
     */
    init {
        //Sets up networker as a server
        fun setUpNetworker() {
            Networker.init(false)
            Networker.getServer().addListener(object : Listener() {
                /**
                 * What happens when someone tries to connect to the host
                 */
                override fun connected(connection: Connection?) {
                    //TODO: make
                    println("YES! Connected as a host to client ${connection?.id}")
                }
            })
        }
        setUpNetworker()
        //Sets up portTextField to only accept valid ports
        this.portTextField.maxLength = 4
        this.portTextField.setTextFieldFilter { _, newChar -> newChar.isDigit() }
        this.portTextField.setTextFieldListener { textField, _ ->
            this.lockButton.isDisabled = textField.text.length != 4
        }
        this.portTextField.setPosition(this.uiContainer.width / 2, this.uiContainer.height * 0.9f, Align.center)
        this.portTextField.setAlignment(Align.center)
        //Sets up the lockButton
        val confirmPort = "Confirm Port"
        val cancelSelection = "Cancel Selection"
        this.lockButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                //lockButton does nothing when disabled
                if (lockButton.isDisabled) {
                    return
                }
                lockButton.setText(if (portTextField.isDisabled) {
                    Networker.reset()
                    setUpNetworker()
                    confirmPort
                } else {
                    Networker.getServer().bind(portTextField.text.toInt())
                    cancelSelection
                })
                portTextField.isDisabled = !portTextField.isDisabled
            }
        })
        this.lockButton.setPosition(this.uiContainer.width / 2, this.uiContainer.height * 0.9f - this.portTextField.height * 1.5f, Align.center)
        this.lockButton.align(Align.center)
        this.lockButton.setText(confirmPort)
        //Sets up cancelButton
        this.cancelButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                game.screen = MainMenuScreen(game)
                dispose()
            }
        })
        this.cancelButton.setPosition(this.uiContainer.width * 0.1f, this.uiContainer.height * 0.1f, Align.center)
        //Adds all widgets
        this.uiContainer.addActor(this.portTextField)
        this.uiContainer.addActor(this.lockButton)
        this.uiContainer.addActor(this.cancelButton)
    }

    override fun draw(delta: Float) {}
    override fun leave() {}

}