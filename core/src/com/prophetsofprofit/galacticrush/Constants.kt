package com.prophetsofprofit.galacticrush

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonWriter
import com.esotericsoftware.kryo.Kryo

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