package com.prophetsofprofit.galacticrush.graphics.screen.networkinguserinterface

import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.prophetsofprofit.galacticrush.defaultTcpPort
import ktx.scene2d.Scene2DSkin

/**
 * A text field used for inputting ports
 */
class PortTextField: TextField("$defaultTcpPort", Scene2DSkin.defaultSkin) {

    //Whether the text inside the field is a valid port
    var isValid = true

    /**
     * Default field settings
     */
    init {
        this.maxLength = 5
        this.setTextFieldFilter { _, newChar -> newChar.isDigit() }
        this.setTextFieldListener { _, _ ->
            this.isValid = try {
                val value = this.text.toInt()
                value in 1025..65535
            } catch (ignored: Exception) {
                false
            }
        }

    }

}