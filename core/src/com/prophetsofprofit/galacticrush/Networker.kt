package com.prophetsofprofit.galacticrush

import com.esotericsoftware.kryonet.Client
import com.esotericsoftware.kryonet.Server
import com.prophetsofprofit.galacticrush.logic.Change
import com.prophetsofprofit.galacticrush.logic.Game

/**
 * The utility object that handles networking
 * Can only ever be either a client or a server at once, but not both
 */
object Networker {

    //The client of the networker (if it is a client); will be null if networker is server
    private var client: Client? = null
    //The server of the networker (if it is a server); will be null if networker is client
    private var server: Server? = null

    /**
     * Initializes the networker
     */
    fun init(isClient: Boolean) {
        if (this.client != null || this.server != null) {
            throw Error("Networker has already been initialized!")
        }
        val kryo = if (isClient) {
            this.client = Client()
            this.client!!.kryo
        } else {
            this.server = Server()
            this.server!!.kryo
        }
        //Registers the classes that the kryo will be sending
        kryo.register(Game::class.java)
        kryo.register(Change::class.java)
    }

    /**
     * Resets the networker so that it must be initialized again befeore usage
     */
    fun reset() {
        this.client?.dispose()
        this.server?.dispose()
        this.client = null
        this.server = null
    }

    /**
     * Gets the client of this networker if the networker is a client
     */
    fun getClient(): Client {
        return this.client!!
    }

    /**
     * Gets the server of this networker if the networker is a server
     */
    fun getServer(): Server {
        return this.server!!
    }

}