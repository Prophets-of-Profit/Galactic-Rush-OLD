package com.prophetsofprofit.galacticrush.graphics.screen.networkinguserinterface

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import com.prophetsofprofit.galacticrush.Main
import com.prophetsofprofit.galacticrush.graphics.screen.GalacticRushScreen
import com.prophetsofprofit.galacticrush.graphics.screen.MainMenuScreen
import com.prophetsofprofit.galacticrush.graphics.screen.loading.ClientLoadingScreen
import com.prophetsofprofit.galacticrush.jsonObject
import com.prophetsofprofit.galacticrush.networking.GalacticRushClient
import com.prophetsofprofit.galacticrush.userAddressesFile
import ktx.scene2d.Scene2DSkin
import java.net.InetAddress
import kotlin.math.roundToInt

/**
 * The screen where you can find a host as a client
 */
class FindHostScreen(game: Main) : GalacticRushScreen(game) {

    //The host addresses that the user has saved; keys are name of host and values are address
    var savedAddresses = mutableMapOf("Localhost" to "127.0.0.1")
    //The text field where the client enters in the hosting port
    val portTextField = PortTextField()
    //The button that starts searching for the specified host
    val confirmButton = TextButton("Connect", Scene2DSkin.defaultSkin)
    //The ui list of selectable addresses; contains saved and local addresses
    val selectableAddressesList = List<String>(Scene2DSkin.defaultSkin)

    /**
     * Initializes the networker as a client
     */
    init {
        GalacticRushClient.start()
        try {
            this.savedAddresses = jsonObject.fromJson(HashMap::class.java, userAddressesFile.readString()) as MutableMap<String, String>
        } catch (ignored: Exception) {
        }
        val fieldAndLabelWidth = this.uiContainer.width * 0.15f

        //Sets up the direct connect to address field
        val directConnectLabel = Label("Address", Scene2DSkin.defaultSkin)
        directConnectLabel.width = fieldAndLabelWidth
        directConnectLabel.setPosition(this.uiContainer.width * 0.9f, this.uiContainer.height * 0.9f, Align.center)
        directConnectLabel.setAlignment(Align.center)
        val directConnectField = TextField(InetAddress.getLocalHost().hostAddress, Scene2DSkin.defaultSkin)
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
                    GalacticRushClient.connect(directConnectField.text, portTextField.text.toInt())
                } catch (e: Exception) {
                    println(e.message)
                }
            }
        })
        this.confirmButton.setPosition(this.uiContainer.width * 0.9f, this.uiContainer.height * 0.1f, Align.center)

        //All local addresses on the network
        var localAddresses = listOf<String>()
        val localAddressString = "LOCAL: "

        //Sets up the list to display saved and local addresses
        var acceptChange = true
        this.selectableAddressesList.setAlignment(Align.center)
        this.selectableAddressesList.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                if (!acceptChange) {
                    return
                }
                try {
                    directConnectField.text = if (savedAddresses.containsKey(selectableAddressesList.selected)) {
                        savedAddresses[selectableAddressesList.selected]
                    } else {
                        localAddresses[selectableAddressesList.selected.removePrefix(localAddressString).toInt()].removePrefix("/")
                    }
                } catch (ignored: Exception) {
                }
            }
        })
        val selectableAddressesPane = ScrollPane(this.selectableAddressesList)
        selectableAddressesPane.width = this.uiContainer.width / 2
        selectableAddressesPane.height = this.uiContainer.height / 2
        this.selectableAddressesList.width = this.uiContainer.width / 2
        selectableAddressesPane.setPosition(this.uiContainer.width / 2, this.uiContainer.height / 2, Align.center)

        //Sets up the thread to look for local connections and update the list
        fun updateSelectableAddressesList() {
            val allNames: MutableList<String> = savedAddresses.keys.toMutableList()
            allNames.addAll(localAddresses.mapIndexed { index: Int, _ -> "$localAddressString$index" })
            acceptChange = false
            val prevSelected = this.selectableAddressesList.selectedIndex
            this.selectableAddressesList.setItems(Array<String>(allNames.toTypedArray()))
            try {
                this.selectableAddressesList.selectedIndex = prevSelected
            } catch (ignored: Exception){
            }
            acceptChange = true
        }
        Thread {
            while (!GalacticRushClient.isConnected) {
                updateSelectableAddressesList()
                try {
                    localAddresses = GalacticRushClient.discoverHosts(this.portTextField.text.toInt() + 1, 5000).map { "$it" }
                } catch (ignored: Exception) {
                    Thread.sleep(5000)
                }
            }
        }.start()

        //Sets up the field and button to save address
        val nameField = TextField("AddressName", Scene2DSkin.defaultSkin)
        nameField.width = fieldAndLabelWidth
        nameField.setPosition(this.uiContainer.width * 0.9f, this.uiContainer.height * 0.6f, Align.center)
        nameField.setAlignment(Align.center)
        nameField.setTextFieldFilter { _, newChar -> newChar.isLetter() }
        val addButton = TextButton("Save Address", Scene2DSkin.defaultSkin)
        addButton.width = fieldAndLabelWidth
        addButton.setPosition(this.uiContainer.width * 0.9f, this.uiContainer.height * 0.52f, Align.center)
        addButton.align(Align.center)
        addButton.addListener(object: ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                savedAddresses[nameField.text] = directConnectField.text
                updateSelectableAddressesList()
            }
        })

        //Sets up the button to remove saved addresses
        val removeButton = TextButton("Remove Address", Scene2DSkin.defaultSkin)
        removeButton.width = fieldAndLabelWidth
        removeButton.setPosition(this.uiContainer.width * 0.1f, this.uiContainer.height * 0.56f, Align.center)
        removeButton.align(Align.center)
        removeButton.addListener(object: ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                savedAddresses.remove(selectableAddressesList.selected)
                updateSelectableAddressesList()
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
        this.uiContainer.addActor(nameField)
        this.uiContainer.addActor(addButton)
        this.uiContainer.addActor(removeButton)
    }

    /**
     * Checks continually whether a connection has been made: if there is a connection, moves the screen to the next screen
     */
    override fun draw(delta: Float) {
        this.confirmButton.isDisabled = !this.portTextField.isValid
        if (!GalacticRushClient.isConnected) {
            return
        }
        this.game.screen = ClientLoadingScreen(this.game)
        this.dispose()
    }

    override fun tap(x: Float, y: Float, count: Int, button: Int): Boolean {
        val mouseLocation = this.game.windowToCamera(x.roundToInt(), y.roundToInt(), this.uiCamera)
        if (this.uiContainer.hit(mouseLocation.x, mouseLocation.y, false) != null) {
            return false
        }
        this.selectableAddressesList.selection.clear()
        return true
    }

    /**
     * When the screen is left, the addresses that the user has saved are written to file
     */
    override fun leave() {
        userAddressesFile.writeString(jsonObject.prettyPrint(this.savedAddresses), false)
    }

}