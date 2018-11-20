package com.prophetsofprofit.galacticrush.graphics.screens.splash

import com.prophetsofprofit.galacticrush.Main
import com.prophetsofprofit.galacticrush.graphics.GalacticRushScreen

/**
 * The opening screen of the game
 */
class SplashScreen(main: Main) : GalacticRushScreen(main) {

    //Keeps track of how long the screen has been opened
    var time = 0f

    override fun draw(delta: Float) {
        this.time += delta
        if (this.time > 5) this.main.screen = null
    }

    override fun leave() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}