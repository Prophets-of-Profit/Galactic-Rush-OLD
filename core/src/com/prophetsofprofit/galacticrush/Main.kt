package com.prophetsofprofit.galacticrush

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.prophetsofprofit.galacticrush.graphics.screen.MainMenu

/**
 * GALACTIC RUSH
 * The object that is where/what the game is run in in a generalized context
 * No matter what platform, the game will be run and managed mainly by this class
 */
class Main : Game() {

    lateinit var batch: SpriteBatch //What will be used to draw sprites and textures and all game things
    lateinit var textDrawer: BitmapFont //What will be used to draw text that isn't a part of a label

    /**
     * Entry point of the game
     */
    override fun create() {
        this.batch = SpriteBatch()
        this.textDrawer = BitmapFont()
        this.screen = MainMenu(this)
    }

}
