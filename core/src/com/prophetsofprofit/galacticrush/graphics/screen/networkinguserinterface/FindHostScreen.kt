package com.prophetsofprofit.galacticrush.graphics.screen.networkinguserinterface

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.prophetsofprofit.galacticrush.Main
import com.prophetsofprofit.galacticrush.Networker
import com.prophetsofprofit.galacticrush.graphics.screen.GalacticRushScreen
import com.prophetsofprofit.galacticrush.graphics.screen.MainMenuScreen
import com.prophetsofprofit.galacticrush.graphics.screen.loading.ClientLoadingScreen
import com.prophetsofprofit.galacticrush.localHostIp
import ktx.scene2d.Scene2DSkin

/**
 * The screen where you can find a host as a client
 */
class FindHostScreen(game: Main) : GalacticRushScreen(game) {

    //The text field where the client enters in the hosting port
    val portTextField = PortTextField()
    //The button that starts searching for the specified host
    val confirmButton = TextButton("Confirm", Scene2DSkin.defaultSkin)
    //The id of the connection between the client and the host
    var connectionId: Int? = null

    /**
     * Initializes the networker as a client
     */
    init {
        //The text field where the client enters in the ip of the host
        val ipTextField = TextField(localHostIp, Scene2DSkin.defaultSkin)
        //A button that automatically fills the ipTextField to the localHostIp
        val localIpAutofillButton = TextButton("Local Ip", Scene2DSkin.defaultSkin)
        //The button to leave this screen
        val cancelButton = TextButton("Cancel", Scene2DSkin.defaultSkin)
        //Sets up the networker as a client
        Networker.init(true)
        /**
         * What happens when a host is found
         */
        Networker.getClient().addListener(object : Listener() {
            override fun connected(connection: Connection?) {
                connectionId = connection!!.id
                Networker.getClient().removeListener(this)
            }
        })
        //Sets up positions of ip and port text fields
        ipTextField.setPosition(this.uiContainer.width / 2, this.uiContainer.height * 0.9f, Align.center)
        this.portTextField.setPosition(this.uiContainer.width / 2, this.uiContainer.height * 0.8f, Align.center)
        ipTextField.setAlignment(Align.center)
        this.portTextField.setAlignment(Align.center)
        //Sets up localIpAutofillButton to autofill the local ip to the ipTextField
        localIpAutofillButton.addListener(object: ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                ipTextField.text = localHostIp
            }
        })
        localIpAutofillButton.setPosition(this.uiContainer.width / 2 + ipTextField.width * 2, this.uiContainer.height * 0.85f, Align.center)
        //Sets up cancelButton
        cancelButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                game.screen = MainMenuScreen(game)
                dispose()
            }
        })
        cancelButton.setPosition(this.uiContainer.width * 0.1f, this.uiContainer.height * 0.1f, Align.center)
        //Sets up confirmButton
        confirmButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                try {
                    Networker.getClient().connect(5000, ipTextField.text, portTextField.text.toInt())
                } catch (e: Exception) {
                    println(e.message)
                }
            }
        })
        this.confirmButton.setPosition(this.uiContainer.width * 0.9f, this.uiContainer.height * 0.1f, Align.center)
        //Adds all ui components to the uiContainer
        this.uiContainer.addActor(this.portTextField)
        this.uiContainer.addActor(ipTextField)
        this.uiContainer.addActor(localIpAutofillButton)
        this.uiContainer.addActor(cancelButton)
        this.uiContainer.addActor(this.confirmButton)
    }

    /**
     * Checks continually whether a connection has been made: if there is a connection, moves the screen to the next screen
     */
    override fun draw(delta: Float) {
        this.confirmButton.isDisabled = !this.portTextField.isValid
        if (this.connectionId == null) {
            return
        }
        this.game.screen = ClientLoadingScreen(this.game)
        this.dispose()
    }

    override fun leave() {}

}