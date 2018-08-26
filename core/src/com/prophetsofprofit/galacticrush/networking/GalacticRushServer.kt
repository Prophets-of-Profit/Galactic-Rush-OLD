package com.prophetsofprofit.galacticrush.networking

import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.esotericsoftware.kryonet.Server
import com.prophetsofprofit.galacticrush.bufferSize
import com.prophetsofprofit.galacticrush.logic.Game
import com.prophetsofprofit.galacticrush.logic.GameCreationOptions
import com.prophetsofprofit.galacticrush.logic.change.DroneChange
import com.prophetsofprofit.galacticrush.logic.change.InstructionChange
import com.prophetsofprofit.galacticrush.logic.map.Galaxy
import com.prophetsofprofit.galacticrush.networking.player.NetworkPlayer
import com.prophetsofprofit.galacticrush.networking.player.Player
import com.prophetsofprofit.galacticrush.registerAllClasses

/**
 * The object that handles server-side networking and game-engine logic
 */
object GalacticRushServer : Server(bufferSize, bufferSize) {

    //The main actual game that is being run and hosted; clients and hosts versions of this game may be slightly out of date at some points in time
    var hostedGame: Game? = null

    /**
     * Upon creation, the server can handle sending/receiving any classes defined in Constants
     */
    init {
        registerAllClasses(this.kryo)
    }

    /**
     * Initializes the server and adds all of the necessary listeners
     * UDP port that is used is one more than the TCP port
     */
    fun usePort(tcpPort: Int) {
        this.bind(tcpPort, tcpPort + 1)
    }

    /**
     * The location of the actual game engine where all game logic is handled
     */
    fun runGame(players: Array<Player>, options: GameCreationOptions) {
        this.hostedGame = Game(players.map { it.id }.toTypedArray(), Galaxy(options.galaxySize, players.map { it.id }))
        //Gives the game to all of the players and gives the network players their player objects that contains the game
        players.forEach {
            it.game = this.hostedGame!!
            if (it is NetworkPlayer) {
                this.sendToTCP(it.connectionId, it)
            }
        }
        //Listens for incoming change objects
        this.addListener(object : Listener() {
            override fun received(connection: Connection?, obj: Any?) {
                if (obj is DroneChange) {
                    hostedGame!!.collectDroneChange(obj)
                } else if (obj is InstructionChange) {
                    hostedGame!!.collectInstructionChange(obj)
                }
            }
        })
        //Actual engine for the game that is constantly running
        while (this.connections.isNotEmpty()) {
            if (!this.hostedGame!!.gameChanged) {
                Thread.sleep(50)
                continue
            }
            this.hostedGame!!.gameChanged = false
            this.hostedGame!!.currentDrafter = 1 //TODO: fix
            this.hostedGame!!.draftOptions = this.hostedGame!!.drawInstructions()
            players.first { it.id == this.hostedGame!!.currentDrafter }.receiveNewInstructions(this.hostedGame!!.draftOptions!!)
            players.forEach { it.receiveNewGameState(this.hostedGame!!) }
            while (!this.hostedGame!!.doDroneTurn()) {
            }
            players.forEach { it.receiveNewGameState(this.hostedGame!!) }
        }
    }

}