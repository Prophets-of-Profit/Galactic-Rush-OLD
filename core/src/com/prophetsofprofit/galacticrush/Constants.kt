package com.prophetsofprofit.galacticrush

import com.badlogic.gdx.Gdx

//The file where the user options are stored
val optionsFile = Gdx.files.local("UserOptions.cfg")!!
//The default options that the user will have
val defaultOptions = Options(.25f, .5f)

//The default tcp port
val defaultTcpPort = 6669
//The local hosting IP
val localHostIp = "127.0.0.1"