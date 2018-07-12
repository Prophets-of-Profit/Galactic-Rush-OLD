package com.prophetsofprofit.galacticrush.graphics.screen.networkinguserinterface

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.prophetsofprofit.galacticrush.Main
import com.prophetsofprofit.galacticrush.Networker
import com.prophetsofprofit.galacticrush.graphics.screen.GalacticRushScreen
import com.prophetsofprofit.galacticrush.graphics.screen.MainMenuScreen
import com.prophetsofprofit.galacticrush.graphics.screen.loading.HostLoadingScreen
import com.prophetsofprofit.galacticrush.logic.player.LocalPlayer
import com.prophetsofprofit.galacticrush.logic.player.NetworkPlayer
import com.prophetsofprofit.galacticrush.logic.player.Player
import ktx.scene2d.Scene2DSkin

/**
 * The screen where the host of the game can wait for clients and set the game settings
 */
class WaitForClientScreen(game: Main) : GalacticRushScreen(game) {

    //The text field where the host enters in the hosting port
    val portTextField = PortTextField()
    //The button that either locks in the selected port or undoes the selection of the port
    val lockButton = TextButton("________________", Scene2DSkin.defaultSkin)
    //The id of the hosts's connection to the client
    var connectionId: Int? = null
    //The players of the game
    var players: Array<Player>? = null

    /**
     * Initializes the networker as a host and also initializes GUI components
     */
    init {
        //The button to leave this screen
        val cancelButton = TextButton("Cancel", Scene2DSkin.defaultSkin)
        //Sets up networker as a server
        Networker.init(false)
        Networker.getServer().addListener(object : Listener() {
            /**
             * What happens when someone tries to connect to the host
             */
            override fun connected(connection: Connection?) {
                connectionId = connection!!.id
                Networker.getServer().removeListener(this)
            }
        })
        //Sets up portTextField
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
                    //Unbinds the port
                    try { Networker.getServer().bind(0) } catch (ignored: Exception) {}
                    confirmPort
                } else {
                    Networker.getServer().bind(portTextField.text.toInt())
                    cancelSelection
                })
                portTextField.isDisabled = !portTextField.isDisabled
            }
        })
        this.lockButton.setPosition(this.uiContainer.width / 2, this.uiContainer.height * 0.9f - portTextField.height * 1.5f, Align.center)
        this.lockButton.align(Align.center)
        this.lockButton.setText(confirmPort)
        //Sets up cancelButton
        cancelButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                game.screen = MainMenuScreen(game)
                dispose()
            }
        })
        cancelButton.setPosition(this.uiContainer.width * 0.1f, this.uiContainer.height * 0.1f, Align.center)
        //Adds all widgets
        this.uiContainer.addActor(this.portTextField)
        this.uiContainer.addActor(this.lockButton)
        this.uiContainer.addActor(cancelButton)
    }

    /**
     * Checks whether a connection has been made and if it has, it moves to the next screen
     */
    override fun draw(delta: Float) {
        this.lockButton.isDisabled = !this.portTextField.isValid
        if (this.connectionId == null) {
            return
        }
        this.players = arrayOf(LocalPlayer(0), NetworkPlayer(1, this.connectionId!!))
        if (this.players == null) {
            return
        }
        this.game.screen = HostLoadingScreen(this.game, this.players!!)
        this.dispose()
    }

    override fun leave() {}

}