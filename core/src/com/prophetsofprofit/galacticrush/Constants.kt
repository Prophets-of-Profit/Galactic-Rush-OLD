package com.prophetsofprofit.galacticrush

import com.badlogic.gdx.Gdx

//The file where the user options are stored
val optionsFile = Gdx.files.local("UserOptions.cfg")!!
//The default options that the user will have
val defaultOptions = Options(.25f, .5f)