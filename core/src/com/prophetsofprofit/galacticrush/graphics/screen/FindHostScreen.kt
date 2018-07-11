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
import com.prophetsofprofit.galacticrush.graphics.screen.loading.ClientLoadingScreen
import com.prophetsofprofit.galacticrush.localHostIp
import ktx.scene2d.Scene2DSkin

/**
 * The screen where you can find a host as a client
 */
class FindHostScreen(game: Main) : GalacticRushScreen(game) {

    //The text field where the client enters in the hosting port
    val portTextField = TextField("$defaultTcpPort", Scene2DSkin.defaultSkin)
    //The text field where the client enters in the ip of the host
    val ipTextField = TextField(localHostIp, Scene2DSkin.defaultSkin)
    //A button that automatically fills the ipTextField to the localHostIp
    val localIpAutofillButton = TextButton("Local Ip", Scene2DSkin.defaultSkin)
    //The button to leave this screen
    val cancelButton = TextButton("Cancel", Scene2DSkin.defaultSkin)
    //The button that starts searching for the specified host
    val confirmButton = TextButton("Confirm", Scene2DSkin.defaultSkin)
    //The id of the connection between the client and the host
    var connectionId: Int? = null

    /**
     * Initializes the networker as a client
     */
    init {
        //Sets up the networker as a client
        Networker.init(true)
        /**
         * What happens when a host is found
         */
        Networker.getClient().addListener(object : Listener() {
            override fun connected(connection: Connection?) {
                connectionId = connection!!.id
            }
        })
        //Sets up positions of ip and port text fields
        this.ipTextField.setPosition(this.uiContainer.width / 2, this.uiContainer.height * 0.9f, Align.center)
        this.portTextField.setPosition(this.uiContainer.width / 2, this.uiContainer.height * 0.8f, Align.center)
        this.ipTextField.setAlignment(Align.center)
        //Sets up port text field
        this.portTextField.maxLength = 4
        this.portTextField.setTextFieldFilter { _, newChar -> newChar.isDigit() }
        this.portTextField.setTextFieldListener { textField, _ ->
            this.confirmButton.isDisabled = textField.text.length != 4
        }
        this.portTextField.setAlignment(Align.center)
        //Sets up localIpAutofillButton to autofill the local ip to the ipTextField
        this.localIpAutofillButton.addListener(object: ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                ipTextField.text = localHostIp
            }
        })
        this.localIpAutofillButton.setPosition(this.uiContainer.width / 2 + this.ipTextField.width * 2, this.uiContainer.height * 0.85f, Align.center)
        //Sets up cancelButton
        this.cancelButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                game.screen = MainMenuScreen(game)
                dispose()
            }
        })
        this.cancelButton.setPosition(this.uiContainer.width * 0.1f, this.uiContainer.height * 0.1f, Align.center)
        //Sets up confirmButton
        this.confirmButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                try {
                    Networker.getClient().connect(500, ipTextField.text, portTextField.text.toInt())
                } catch (e: Exception) {
                    println(e.message)
                }
            }
        })
        this.confirmButton.setPosition(this.uiContainer.width * 0.9f, this.uiContainer.height * 0.1f, Align.center)
        //Adds all ui components to the uiContainer
        this.uiContainer.addActor(this.portTextField)
        this.uiContainer.addActor(this.ipTextField)
        this.uiContainer.addActor(this.localIpAutofillButton)
        this.uiContainer.addActor(this.cancelButton)
        this.uiContainer.addActor(this.confirmButton)
    }

    /**
     * Checks continually whether a connection has been made: if there is a connection, moves the screen to the next screen
     */
    override fun draw(delta: Float) {
        if (this.connectionId == null) {
            return
        }
        this.game.screen = ClientLoadingScreen(this.game, this.connectionId!!)
        this.dispose()
    }

    override fun leave() {}

}