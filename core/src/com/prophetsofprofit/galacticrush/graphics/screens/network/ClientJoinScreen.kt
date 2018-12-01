package com.prophetsofprofit.galacticrush.graphics.screens.network

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.utils.Align
import com.prophetsofprofit.galacticrush.Main
import com.prophetsofprofit.galacticrush.graphics.GalacticRushScreen
import com.prophetsofprofit.galacticrush.graphics.SelectionList
import com.prophetsofprofit.galacticrush.graphics.onClick
import com.prophetsofprofit.galacticrush.graphics.screens.menu.MainMenuScreen
import ktx.scene2d.Scene2DSkin

/**
 * A screen where a client can join a host
 */
class ClientJoinScreen(main: Main) : GalacticRushScreen(main) {

    /**
     * Adds all the UI elements to the client join screen
     */
    init {
        /*
         * TITLE LABEL
         */
        val titleLabel = Label("JOIN A GAME", Scene2DSkin.defaultSkin, "title")
        titleLabel.setSize(this.uiContainer.width * 0.9f, titleLabel.height)
        titleLabel.setPosition(this.uiContainer.width / 2f, this.uiContainer.height * 0.9f, Align.center)
        titleLabel.setAlignment(Align.center)
        this.uiContainer.addActor(titleLabel)

        /*
         * ADDRESSES LIST
         */
        val addressesList = SelectionList()
        addressesList.setSize(this.uiContainer.width * 0.9f, this.uiContainer.height / 2f)
        addressesList.setPosition(this.uiContainer.width / 2f, this.uiContainer.height * 0.4f, Align.center)
        this.uiContainer.addActor(addressesList)

        /*
         * ADDRESS LABEL
         */
        val addressLabel = Label("Address", Scene2DSkin.defaultSkin)
        addressLabel.setSize(this.uiContainer.width * 0.4f, addressLabel.height * 3)
        addressLabel.setPosition(titleLabel.x, (titleLabel.y + addressesList.top) / 2f + addressLabel.height / 2f)
        addressLabel.setAlignment(Align.center)
        this.uiContainer.addActor(addressLabel)

        /*
         * ADDRESS FIELD
         */
        val addressField = TextField("", Scene2DSkin.defaultSkin)
        addressField.setSize(this.uiContainer.width * 0.4f, addressField.height * 3)
        addressField.setPosition(titleLabel.x, addressLabel.y - addressField.height - 15f)

        this.uiContainer.addActor(addressField)

        /*
         * BACK BUTTON
         */
        val backButton = TextButton("Back", Scene2DSkin.defaultSkin)
        backButton.setSize(this.uiContainer.width * 0.9f, backButton.height)
        backButton.setPosition(this.uiContainer.width / 2f, this.uiContainer.height * 0.1f, Align.center)
        this.uiContainer.addActor(backButton)
        backButton.onClick { this.main.screen = MainMenuScreen(this.main) }
    }

    override fun draw(delta: Float) {
    }

    override fun leave() {
    }

}