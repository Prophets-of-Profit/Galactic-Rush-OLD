package com.prophetsofprofit.galacticrush.graphics.screen.networkinguserinterface

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
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
        val fieldAndLabelWidth = this.uiContainer.width * 0.15f

        //Sets up the direct connect to address field
        val directConnectLabel = Label("Address", Scene2DSkin.defaultSkin)
        directConnectLabel.width = fieldAndLabelWidth
        directConnectLabel.setPosition(this.uiContainer.width * 0.9f, this.uiContainer.height * 0.9f, Align.center)
        directConnectLabel.setAlignment(Align.center)
        val directConnectField = TextField(localHostIp, Scene2DSkin.defaultSkin)
        directConnectField.width = fieldAndLabelWidth
        directConnectField.setPosition(this.uiContainer.width * 0.9f, this.uiContainer.height * 0.85f, Align.center)
        directConnectField.setAlignment(Align.center)

        //Sets up the port text field
        val portLabel = Label("Port", Scene2DSkin.defaultSkin)
        portLabel.width = fieldAndLabelWidth
        portLabel.setPosition(this.uiContainer.width * 0.1f, this.uiContainer.height * 0.9f, Align.center)
        portLabel.setAlignment(Align.center)
        portTextField.width = fieldAndLabelWidth
        this.portTextField.setPosition(this.uiContainer.width * 0.1f, this.uiContainer.height * 0.85f, Align.center)
        this.portTextField.setAlignment(Align.center)

        //Sets up confirmButton
        this.confirmButton.width = fieldAndLabelWidth
        this.confirmButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                try {
                    Networker.getClient().connect(5000, directConnectField.text, portTextField.text.toInt())
                } catch (e: Exception) {
                    println(e.message)
                }
            }
        })
        this.confirmButton.setPosition(this.uiContainer.width * 0.9f, this.uiContainer.height * 0.77f, Align.center)

        //All local addresses on the network
        val localAddresses = mutableListOf<String>()

        //Sets up the list to display saved and local addresses
        var acceptChange = true
        val selectableAddressesList = List<String>(Scene2DSkin.defaultSkin)
        selectableAddressesList.setAlignment(Align.center)
        selectableAddressesList.addListener(object: ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                if (!acceptChange) {
                    return
                }
                try {
                    directConnectField.text = Networker.savedAdresses[selectableAddressesList.selected]
                } catch (ignored: Exception) {
                    try {
                        directConnectField.text = localAddresses[selectableAddressesList.selected.split(" ")[1].toInt()]
                    } catch (ignoredAgain: Exception) {
                    }
                }
            }
        })
        val selectableAddressesPane = ScrollPane(selectableAddressesList)
        selectableAddressesPane.width = this.uiContainer.width / 2
        selectableAddressesPane.height = this.uiContainer.height / 2
        selectableAddressesList.width = this.uiContainer.width / 2
        selectableAddressesList.setPosition(this.uiContainer.width / 2, this.uiContainer.height / 2, Align.center)
        selectableAddressesPane.setPosition(this.uiContainer.width / 2, this.uiContainer.height / 2, Align.center)

        //Sets up the thread to look for local connections and update the list
        Thread {
            while (this.connectionId == null) {
                Networker.updateSavedAdresses()
                val allNames: MutableList<String> = Networker.savedAdresses.keys.toMutableList()
                allNames.addAll(localAddresses.mapIndexed { index: Int, _ -> "LOCAL: $index" })
                acceptChange = false
                val prevSelected = selectableAddressesList.selectedIndex
                selectableAddressesList.setItems(com.badlogic.gdx.utils.Array<String>(allNames.toTypedArray()))
                selectableAddressesList.selectedIndex = prevSelected //TODO: what if there are less items and prevSelected is now too high?
                acceptChange = true
                localAddresses.clear()
                try {
                    Networker.getClient().discoverHosts(this.portTextField.text.toInt(), 5000).mapTo(localAddresses) { it.toString() }
                } catch (ignored: Exception) {
                    Thread.sleep(5000)
                }
            }
        }.start()

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

        //Sets up cancelButton
        val cancelButton = TextButton("Cancel", Scene2DSkin.defaultSkin)
        cancelButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                game.screen = MainMenuScreen(game)
                dispose()
            }
        })
        cancelButton.setPosition(this.uiContainer.width * 0.1f, this.uiContainer.height * 0.1f, Align.center)

        //Adds all ui components to the uiContainer
        this.uiContainer.addActor(portLabel)
        this.uiContainer.addActor(this.portTextField)
        this.uiContainer.addActor(directConnectLabel)
        this.uiContainer.addActor(directConnectField)
        this.uiContainer.addActor(cancelButton)
        this.uiContainer.addActor(this.confirmButton)
        this.uiContainer.addActor(selectableAddressesPane)
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

    /**
     * When the screen is left, the addresses that the user has saved are written to file
     */
    override fun leave() {
        Networker.saveAdresses()
    }

}