package com.prophetsofprofit.galacticrush.graphics.screen.maingame.menu

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.prophetsofprofit.galacticrush.graphics.Direction
import com.prophetsofprofit.galacticrush.graphics.ModalWindow
import com.prophetsofprofit.galacticrush.graphics.screen.MainMenuScreen
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.MainGameScreen
import ktx.scene2d.Scene2DSkin

/**
 * A class that is a modal window that appears when the user is doing something outside of the gameplay
 * Doesn't pause the game for everyone, but just essentially pauses the game for the user
 */
class PauseMenu(gameScreen: MainGameScreen) : ModalWindow(gameScreen, "Pause Menu", gameScreen.uiCamera.viewportWidth / 3, gameScreen.uiCamera.viewportHeight / 2) {

    /**
     * Initializes all of the components of the Pause Menu and positions them
     */
    init {
        val resumeButton = TextButton("Resume", Scene2DSkin.defaultSkin).also {
            it.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    disappear(Direction.POP, 1f)
                }
            })
        }
        val optionsButton = TextButton("Options", Scene2DSkin.defaultSkin).also {
            it.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    disappear(Direction.POP, 0.5f)
                    Thread {
                        Thread.sleep(505)
                        if (!isVisible) {
                            gameScreen.optionsMenu.appear(Direction.POP, 1f)
                        }
                    }.start()
                }
            })
        }
        val quitButton = TextButton("Quit", Scene2DSkin.defaultSkin).also {
            it.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    gameScreen.game.screen = MainMenuScreen(gameScreen.game)
                    gameScreen.dispose()
                }
            })
        }
        this.add(resumeButton).expandY().row()
        this.add(optionsButton).expandY().row()
        this.add(quitButton).expandY()
    }

}