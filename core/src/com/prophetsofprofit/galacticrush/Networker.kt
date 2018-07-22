package com.prophetsofprofit.galacticrush

import com.badlogic.gdx.graphics.Color
import com.esotericsoftware.kryonet.Client
import com.esotericsoftware.kryonet.Server
import com.prophetsofprofit.galacticrush.logic.Change
import com.prophetsofprofit.galacticrush.logic.Game
import com.prophetsofprofit.galacticrush.logic.drone.Drone
import com.prophetsofprofit.galacticrush.logic.facility.ConstructionFacility
import com.prophetsofprofit.galacticrush.logic.facility.HomeBase
import com.prophetsofprofit.galacticrush.logic.facility.ProgrammingFacility
import com.prophetsofprofit.galacticrush.logic.map.Attribute
import com.prophetsofprofit.galacticrush.logic.map.CosmicHighway
import com.prophetsofprofit.galacticrush.logic.map.Galaxy
import com.prophetsofprofit.galacticrush.logic.map.Planet
import com.prophetsofprofit.galacticrush.logic.player.LocalPlayer
import com.prophetsofprofit.galacticrush.logic.player.NetworkPlayer
import com.prophetsofprofit.galacticrush.logic.player.Player
import java.util.*
import kotlin.collections.HashMap

/**
 * The utility object that handles networking
 * Can only ever be either a client or a server at once, but not both
 */
object Networker {

    //A hashmap of the user's stored addresses
    var savedAdresses = hashMapOf("Localhost" to localHostIp)

    //The client of the networker (if it is a client); will be null if networker is server
    private var client: Client? = null
    //The server of the networker (if it is a server); will be null if networker is client
    private var server: Server? = null
    //What the Networker is
    var isClient: Boolean? = null
        private set(value) {
            field = value
        }

    /**
     * Initializes the networker
     */
    fun init(isClient: Boolean) {
        if (this.client != null || this.server != null) {
            throw Error("Networker has already been initialized!")
        }
        this.isClient = isClient
        val bufferSize = Int.MAX_VALUE / 1024
        val kryo = if (isClient) {
            this.client = Client(bufferSize, bufferSize)
            this.client!!.kryo
        } else {
            this.server = Server(bufferSize, bufferSize)
            this.server!!.kryo
        }
        //Registers the classes that the kryo will be sending
        kryo.register(Array<Int>::class.java)
        kryo.register(Array<Player>::class.java)
        kryo.register(ArrayList::class.java)
        kryo.register(Attribute::class.java)
        kryo.register(Change::class.java)
        kryo.register(Color::class.java)
        kryo.register(ConstructionFacility::class.java)
        kryo.register(CosmicHighway::class.java)
        kryo.register(Date::class.java)
        kryo.register(Drone::class.java)
        kryo.register(Galaxy::class.java)
        kryo.register(Game::class.java)
        kryo.register(HomeBase::class.java)
        kryo.register(LinkedHashMap::class.java)
        kryo.register(LocalPlayer::class.java)
        kryo.register(NetworkPlayer::class.java)
        kryo.register(Planet::class.java)
        kryo.register(ProgrammingFacility::class.java)
        this.client?.start()
        this.server?.start()
    }

    /**
     * Resets the networker so that it must be initialized again befeore usage
     */
    fun reset() {
        try {
            this.client?.close()
            this.server?.close()
            this.client?.stop()
            this.server?.stop()
            Thread.sleep(50)
            this.client?.dispose()
            this.server?.dispose()
            this.client = null
            this.server = null
            this.isClient = null
        } catch (ignored: Exception) {
        }
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

    /**
     * Saves the hashmap of saved addresses to the json file specified in constants
     */
    fun saveAdresses() {
        userAddressesFile.writeString(jsonObject.prettyPrint(this.savedAdresses), false)
    }

    /**
     * Tries to retrieve the user's saved addresses
     */
    fun updateSavedAdresses() {
        try {
            this.savedAdresses = jsonObject.fromJson(HashMap::class.java, userAddressesFile.readString()) as HashMap<String, String>
        } catch (ignored: Exception) {
        }
    }

}