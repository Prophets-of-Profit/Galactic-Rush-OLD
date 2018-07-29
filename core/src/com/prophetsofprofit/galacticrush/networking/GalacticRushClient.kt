package com.prophetsofprofit.galacticrush.networking

import com.esotericsoftware.kryonet.Client
import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.prophetsofprofit.galacticrush.bufferSize
import com.prophetsofprofit.galacticrush.logic.Game
import com.prophetsofprofit.galacticrush.logic.player.NetworkPlayer
import com.prophetsofprofit.galacticrush.registerAllClasses

/**
 * The object that handles client-side networking
 */
object GalacticRushClient : Client(bufferSize, bufferSize) {

    //The player that the networking client is working with or representing
    var player: NetworkPlayer? = null
    //Whether the initialListener is currently added or not
    var initialListenerAdded = false
    //The initial listener for what happens when the client starts a connection
    val initialListener = object : Listener() {
        override fun received(connection: Connection?, obj: Any?) {
            if (obj is NetworkPlayer) {
                player = obj
            }
            removeListener(this)
            initialListenerAdded = false
            addMainListeners()
        }
    }

    /**
     * Upon creation, the client can handle sending/receiving any classes defined in Constants
     */
    init {
        registerAllClasses(this.kryo)
    }

    /**
     * Initializes the client and adds all of the necessary listeners
     * Tries connecting to the given address, the given TCP port, and a calculated UDP port for 10 seconds
     * UDP port that is used is one more than the TCP port
     */
    fun connect(ipAddress: String, tcpPort: Int) {
        val timeout = 10_000
        this.connect(timeout, ipAddress, tcpPort, tcpPort + 1)
        if (!this.initialListenerAdded) {
            this.addListener(this.initialListener)
            this.initialListenerAdded = true
        }
    }

    /**
     * Adds the main listeners to the client where the bulk of client networking logic is actually handled
     */
    private fun addMainListeners() {
        this.removeListener(this.initialListener)
        this.addListener(object : Listener() {
            override fun received(connection: Connection?, obj: Any?) {
                if (obj is Game) {
                    player!!.game = obj
                }
            }
        })
    }

}