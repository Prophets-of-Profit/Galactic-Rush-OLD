package com.prophetsofprofit.galacticrush.graphics.screens.network

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import com.prophetsofprofit.galacticrush.Main
import com.prophetsofprofit.galacticrush.defaultTcpPort
import com.prophetsofprofit.galacticrush.graphics.GalacticRushScreen
import com.prophetsofprofit.galacticrush.graphics.SelectionList
import com.prophetsofprofit.galacticrush.graphics.act
import com.prophetsofprofit.galacticrush.graphics.onClick
import com.prophetsofprofit.galacticrush.graphics.screens.menu.MainMenuScreen
import com.prophetsofprofit.galacticrush.networking.GalacticRushClient
import ktx.scene2d.Scene2DSkin

/**
 * A screen where a client can join a host
 */
class ClientJoinScreen(main: Main) : GalacticRushScreen(main) {

    //A variable that represents whether the client is in the middle of attempting a connection or not
    var isInConnectAttempt = false

    /**
     * Adds all the UI elements to the client join screen
     */
    init {
        /*
         * TITLE LABEL
         */
        val titleLabel = Label("JOIN A GAME", Scene2DSkin.defaultSkin, "title")
        titleLabel.setSize(this.uiContainer.width * 0.9f, titleLabel.height)
        titleLabel.setPosition(this.uiContainer.width / 2f, this.uiContainer.height * 0.9f, Align.center)
        titleLabel.setAlignment(Align.center)
        this.uiContainer.addActor(titleLabel)

        /*
         * ADDRESS LABEL
         */
        val addressLabel = Label("Address", Scene2DSkin.defaultSkin)
        addressLabel.setSize(this.uiContainer.width * 0.4f, addressLabel.height * 3)
        addressLabel.setPosition(titleLabel.x, (titleLabel.y + this.uiContainer.height * 0.7f) / 2f + addressLabel.height / 2f)
        addressLabel.setAlignment(Align.center)
        this.uiContainer.addActor(addressLabel)

        /*
         * PORT LABEL
         */
        val portLabel = Label("Port", Scene2DSkin.defaultSkin)
        portLabel.setSize(this.uiContainer.width * 0.25f, portLabel.height * 3)
        portLabel.setPosition(addressLabel.right + 15f, addressLabel.y)
        portLabel.setAlignment(Align.center)
        this.uiContainer.addActor(portLabel)

        /*
         * ADDRESS FIELD
         */
        val addressField = TextField("127.0.0.1", Scene2DSkin.defaultSkin)
        addressField.setSize(this.uiContainer.width * 0.4f, addressField.height * 3)
        addressField.setPosition(titleLabel.x, addressLabel.y - addressField.height - 15f)
        addressField.setAlignment(Align.center)
        this.uiContainer.addActor(addressField)

        /*
         * PORT FIELD
         */
        val portField = TextField("$defaultTcpPort", Scene2DSkin.defaultSkin)
        portField.setSize(this.uiContainer.width * 0.25f, portField.height * 3)
        portField.setPosition(portLabel.x, portLabel.y - portField.height - 15f)
        portField.setAlignment(Align.center)
        this.uiContainer.addActor(portField)

        /*
         * CONNECT BUTTON
         */
        val connectButton = TextButton("Connect", Scene2DSkin.defaultSkin)
        connectButton.setSize(this.uiContainer.width * 0.2f, portField.height)
        connectButton.setPosition(titleLabel.right, portField.y, Align.bottomRight)
        this.uiContainer.addActor(connectButton)
        connectButton.act {
            connectButton.isDisabled = this.isInConnectAttempt
        }
        connectButton.onClick {
            try {
                this.isInConnectAttempt = true
                GalacticRushClient.connect(addressField.text, portField.text.toIntOrNull() ?: -1)
            } catch (e: Exception) {
                this.isInConnectAttempt = false
            }
        }

        /*
         * ADDRESSES LIST
         */
        val addressesList = SelectionList()
        addressesList.setSize(this.uiContainer.width * 0.9f, this.uiContainer.height / 2f)
        addressesList.setPosition(this.uiContainer.width / 2f, this.uiContainer.height * 0.4f, Align.center)
        this.uiContainer.addActor(addressesList)
        var discoveredHosts = arrayOf<String>()
        Thread {
            Thread.sleep(50)
            while (this.main.screen == this) {
                discoveredHosts = GalacticRushClient.discoverHosts((portField.text.toIntOrNull()
                        ?: -2) + 1, 1000).map { address -> address.hostAddress }.toTypedArray()
            }
        }.start()
        addressesList.act {
            addressesList.list.setItems(Array(discoveredHosts))
            if (!addressesList.list.selected.isNullOrBlank()) {
                addressField.text = addressesList.list.selected
                addressesList.list.selection.clear()
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

    /**
     * The screen just checks if a connection has been attempted, and moves on to the next screen when successful
     */
    override fun draw(delta: Float) {
        //TODO: make
    }

    override fun leave() {}

}