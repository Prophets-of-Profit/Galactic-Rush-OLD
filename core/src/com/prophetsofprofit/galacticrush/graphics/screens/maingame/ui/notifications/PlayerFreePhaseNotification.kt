package com.prophetsofprofit.galacticrush.graphics.screens.maingame.ui.notifications

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.prophetsofprofit.galacticrush.graphics.screens.maingame.ui.Notification
import ktx.scene2d.Scene2DSkin

/**
 * Tells the players the game is on!
 */
class PlayerFreePhaseNotification(width: Float, height: Float) : Notification(width, height, TextureRegionDrawable(TextureRegion(Texture("image/placeholder.png"))), "The Player Free Phase has begun", Scene2DSkin.defaultSkin) {

    override fun action() {
        this.parent!!.removeNotification(this)
    }

}