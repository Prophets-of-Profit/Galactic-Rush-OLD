package com.prophetsofprofit.galacticrush.graphics.screen.maingame.menu

import com.prophetsofprofit.galacticrush.graphics.ModalWindow
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.MainGameScreen

/**
 * The menu where the user can modify their drone
 */
class DroneModificationMenu(gameScreen: MainGameScreen) : ModalWindow(gameScreen, "Drone Modification", gameScreen.uiCamera.viewportWidth / 2, gameScreen.uiCamera.viewportHeight / 2)