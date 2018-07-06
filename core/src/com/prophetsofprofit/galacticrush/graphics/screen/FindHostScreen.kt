package com.prophetsofprofit.galacticrush.graphics.screen

import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.prophetsofprofit.galacticrush.Main
import com.prophetsofprofit.galacticrush.Networker
import com.prophetsofprofit.galacticrush.defaultTcpPort
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

    /**
     * Initializes the networker as a client
     */
    init {
        //Sets up the networker as a client
        fun setUpNetworker() {
            Networker.init(true)
            Networker.getClient().addListener(object : Listener() {
                override fun connected(connection: Connection?) {
                    //TODO: make
                    println("YES! Connected as a client to host ${connection?.id}")
                }
            })
        }
        setUpNetworker()

        //Adds all ui components to the uiContainer
        this.uiContainer.addActor(this.portTextField)
        this.uiContainer.addActor(this.ipTextField)
        this.uiContainer.addActor(this.localIpAutofillButton)
    }

    override fun draw(delta: Float) {}
    override fun leave() {}

}