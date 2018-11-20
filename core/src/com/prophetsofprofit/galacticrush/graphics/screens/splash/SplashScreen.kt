package com.prophetsofprofit.galacticrush.graphics.screens.splash

import com.prophetsofprofit.galacticrush.Main
import com.prophetsofprofit.galacticrush.graphics.GalacticRushScreen
import com.prophetsofprofit.galacticrush.graphics.screens.menu.MainMenuScreen

/**
 * The opening screen of the game
 * Displays that Prophets of Profit made the game
 */
class SplashScreen(main: Main) : GalacticRushScreen(main) {

    //Keeps track of how long the screen has been opened
    var time = 0f

    /**
     * Checks how much time has elapsed since screen creation and then moves to the next screen when necessary
     */
    override fun draw(delta: Float) {
        this.time += delta
        if (this.time > 5f) {
            this.main.screen = MainMenuScreen(this.main)
        }
    }

    /**
     * Nothing is done on the splash screen's leave
     */
    override fun leave() {}

}