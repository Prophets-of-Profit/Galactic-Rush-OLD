package com.prophetsofprofit.galacticrush.graphics.screen.maingame.menu

import com.prophetsofprofit.galacticrush.graphics.ModalWindow
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.MainGameScreen

/**
 * The popup that will appear when a draft is happening for the current player
 * Allows them to see their options and choose an instruction
 */
class DraftPopup(gameScreen: MainGameScreen) : ModalWindow(gameScreen, "Draft Options", gameScreen.uiCamera.viewportWidth / 2, gameScreen.uiCamera.viewportHeight / 2)