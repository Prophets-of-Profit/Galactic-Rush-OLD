package com.prophetsofprofit.galacticrush

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonWriter
import com.esotericsoftware.kryo.Kryo
import com.prophetsofprofit.galacticrush.logic.Change
import com.prophetsofprofit.galacticrush.logic.DroneTurnChange
import com.prophetsofprofit.galacticrush.logic.Game
import com.prophetsofprofit.galacticrush.logic.base.Base
import com.prophetsofprofit.galacticrush.logic.base.Facility
import com.prophetsofprofit.galacticrush.logic.drone.Drone
import com.prophetsofprofit.galacticrush.logic.drone.instruction.Instruction
import com.prophetsofprofit.galacticrush.logic.drone.instruction.InstructionInstance
import com.prophetsofprofit.galacticrush.logic.map.Attribute
import com.prophetsofprofit.galacticrush.logic.map.CosmicHighway
import com.prophetsofprofit.galacticrush.logic.map.Galaxy
import com.prophetsofprofit.galacticrush.logic.map.Planet
import com.prophetsofprofit.galacticrush.logic.player.LocalPlayer
import com.prophetsofprofit.galacticrush.logic.player.NetworkPlayer
import com.prophetsofprofit.galacticrush.logic.player.Player
import java.util.*

//The object that handles reading/writing JSON
val jsonObject = Json(JsonWriter.OutputType.json).also { it.setUsePrototypes(false) }
//The object that handles serializing and deserializing objects; is used to clone objects as well
val kryo = Kryo()

//The file where the user options are stored
val optionsFile = Gdx.files.local("UserOptions.json")!!
//The file where the user's saved addresses are stored
val userAddressesFile = Gdx.files.local("SavedAddresses.json")!!

//The default tcp port
val defaultTcpPort = 6669
//The local hosting IP
val localHostIp = "127.0.0.1"

//The size of the buffers for clients and servers
const val bufferSize = Int.MAX_VALUE / 1024

//A list of default drone names; current default names are names of Roman Emperors (super edgy)
val defaultDroneNames = arrayOf(
        "Tiberius",
        "Caligula",
        "Claudius",
        "Nero",
        "Galba",
        "Otho",
        "Aulus Vitellius",
        "Vespasian",
        "Titus",
        "Domitian",
        "Nerva",
        "Trajan",
        "Hadrian",
        "Antoninus Pius",
        "Marcus Aurelius",
        "Lucius Verus",
        "Commodus",
        "Publius Helvius Pertinax",
        "Marcus Didius Severus Julianus",
        "Septimius Severus",
        "Caracalla",
        "Publius Septimius Geta",
        "Macrinus",
        "Elagabalus",
        "Severus Alexander",
        "Maximinus",
        "Gordian I",
        "Gordian II",
        "Pupienus Maximus",
        "Balbinus",
        "Gordian III",
        "Philip",
        "Decius",
        "Hostilian",
        "Gallus",
        "Aemilian",
        "Valerian",
        "Gallienus",
        "Claudius II Gothicus",
        "Quintillus",
        "Aurelian",
        "Tacitus",
        "Florian",
        "Probus",
        "Carus",
        "Numerian",
        "Carinus",
        "Diocletian",
        "Maximian",
        "Constantius I",
        "Galerius",
        "Severus",
        "Maxentius",
        "Constantine I",
        "Galerius Valerius Maximinus",
        "Licinius",
        "Constantine II",
        "Constantius II",
        "Constans I",
        "Gallus Caesar",
        "Julian",
        "Jovian",
        "Valentinian I",
        "Valens",
        "Gratian",
        "Valentinian II",
        "Theodosius I",
        "Arcadius",
        "Magnus Maximus",
        "Honorius",
        "Theodosius II",
        "Constantius III",
        "Valentinian III",
        "Marcian",
        "Petronius Maximus",
        "Avitus",
        "Majorian",
        "Libius Severus",
        "Anthemius",
        "Olybrius",
        "Glycerius",
        "Julius Nepos",
        "Romulus Augustulus",
        "Leo I",
        "Leo II",
        "Zeno"
)

/**
 * Registers all of the classes that KryoNet will need to serialize and send over the network
 * Registered classes must have a constructor that takes in no arguments or an empty constructor
 */
fun registerAllClasses(kryo: Kryo) {
    kryo.register(Array<Int>::class.java)
    kryo.register(Array<Player>::class.java)
    kryo.register(ArrayList::class.java)
    kryo.register(Attribute::class.java)
    kryo.register(Base::class.java)
    kryo.register(Change::class.java)
    kryo.register(Color::class.java)
    kryo.register(CosmicHighway::class.java)
    kryo.register(Date::class.java)
    kryo.register(Drone::class.java)
    kryo.register(DroneTurnChange::class.java)
    kryo.register(Facility::class.java)
    kryo.register(Galaxy::class.java)
    kryo.register(Game::class.java)
    kryo.register(Instruction::class.java)
    kryo.register(InstructionInstance::class.java)
    kryo.register(LinkedHashMap::class.java)
    kryo.register(LocalPlayer::class.java)
    kryo.register(NetworkPlayer::class.java)
    kryo.register(Planet::class.java)
}