package com.prophetsofprofit.galacticrush.graphics.screens.maingame.ui.gameboard

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image

/**
 * A planet on the gameboard
 * Associates an image with  planet in the game
 */
class PlanetActor(val id: Int, image: Texture) : Image(image)