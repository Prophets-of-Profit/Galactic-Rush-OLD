package com.prophetsofprofit.galacticrush.graphics.screen.maingame.panel

import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import com.prophetsofprofit.galacticrush.graphics.Direction
import com.prophetsofprofit.galacticrush.graphics.Panel
import com.prophetsofprofit.galacticrush.graphics.screen.maingame.MainGameScreen
import com.prophetsofprofit.galacticrush.logic.base.Facility
import com.prophetsofprofit.galacticrush.logic.drone.Drone
import ktx.scene2d.Scene2DSkin

/**
 * The class for the panel that will handle displaying all of the information of the currently selected base
 */
class BaseInformationPanel(gameScreen: MainGameScreen) : Panel(gameScreen, "Base Information", 0f, 0.9f * gameScreen.uiCamera.viewportHeight, 0.25f * gameScreen.uiCamera.viewportWidth, 0.5f * gameScreen.uiCamera.viewportHeight, Align.topLeft) {

    /**
     * Initializes the BaseInfomationPanel with all of the widgets and positionings and behaviours
     */
    init {
        //A label that shows the name of the selected planet's base
        val nameLabel = Label("", Scene2DSkin.defaultSkin).also {
            it.setAlignment(Align.center)
            it.addAction(object : Action() {
                override fun act(delta: Float): Boolean {
                    if (!canBeUsed || gameScreen.selectedPlanet?.base == null) {
                        return false
                    }
                    it.setText(gameScreen.selectedPlanet!!.base!!.name)
                    return false
                }
            })
        }

        //A scrollpanel containing a list of the selected planet's base's facilities
        val facilitiesScrollPanel = ScrollPane(List<String>(Scene2DSkin.defaultSkin).also {
            it.setAlignment(Align.center)
            //Keeps the list up to date with all of the facilities
            it.addAction(object : Action() {
                override fun act(delta: Float): Boolean {
                    if (!canBeUsed) {
                        return false
                    }
                    it.setItems(Array(gameScreen.selectedPlanet?.base?.facilityHealths?.keys?.map { "$it" }?.toTypedArray()
                            ?: arrayOf()))
                    return false
                }
            })
        }, Scene2DSkin.defaultSkin)

        //The button to modify the base
        val modifyBaseButton = TextButton("Modify Base", Scene2DSkin.defaultSkin).also {
            it.align(Align.center)
            it.addAction(object : Action() {
                override fun act(delta: Float): Boolean {
                    if (!canBeUsed) {
                        return false
                    }
                    it.isVisible = gameScreen.selectedPlanet?.base?.ownerId == gameScreen.player.id
                    return false
                }
            })
            it.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    //TODO: open base modification panel
                }
            })
        }

        //The button to modify a drone on the planet
        val modifyDroneButton = TextButton("", Scene2DSkin.defaultSkin).also {
            it.align(Align.center)
            var containsProgrammingFacility = false
            //Prompts the user to buy a programming facility if they don't have one; if they do, prompts them to modify a drone
            it.addAction(object : Action() {
                override fun act(delta: Float): Boolean {
                    if (!canBeUsed) {
                        return false
                    }
                    it.isVisible = gameScreen.selectedPlanet?.base?.ownerId == gameScreen.player.id
                    containsProgrammingFacility = gameScreen.selectedPlanet?.base?.facilityHealths?.containsKey(Facility.PROGRAMMING) == true
                    it.setText(if (containsProgrammingFacility) "Modify a Drone" else "Buy a Programming Facility")
                    it.isDisabled = !containsProgrammingFacility && gameScreen.mainGame.money[gameScreen.player.id]!! < 0 //TODO: instead of check for less than 0, check for less than cost of a programming facility
                    return false
                }
            })
            //Either buys a programming facility or opens up the drone modification menu
            it.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    if (containsProgrammingFacility) {
                        //TODO: open drone modification panel
                        return
                    }
                    gameScreen.selectedPlanet?.base?.addFacility(Facility.PROGRAMMING)
                    //TODO: subtract cost of programming facility from player money
                }
            })
        }

        //The button to create a drone on the planet
        val createDroneButton = TextButton("", Scene2DSkin.defaultSkin).also {
            it.align(Align.center)
            var containsConstructionFacility = false
            //Prompts the user to buy a construction facility if they don't have one; if they do, prompts them to buy a drone
            it.addAction(object : Action() {
                override fun act(delta: Float): Boolean {
                    if (!canBeUsed) {
                        return false
                    }
                    it.isVisible = gameScreen.selectedPlanet?.base?.ownerId == gameScreen.player.id
                    containsConstructionFacility = gameScreen.selectedPlanet?.base?.facilityHealths?.containsKey(Facility.CONSTRUCTION) == true
                    it.setText(if (containsConstructionFacility) "Create Drone" else "Buy a Construction Facility")
                    val currentMoney = gameScreen.mainGame.money[gameScreen.player.id]!!
                    it.isDisabled = (containsConstructionFacility && currentMoney < 0) || (!containsConstructionFacility && currentMoney < 0) //TODO: replace 0 with actual values
                    return false
                }
            })
            //Either buys a construction facility or opens up the drone modification menu
            it.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    if (containsConstructionFacility) {
                        val newDrone = Drone(gameScreen.player.id, gameScreen.selectedPlanetId!!)
                        gameScreen.mainGame.galaxy.getPlanetWithId(newDrone.locationId)!!.drones.add(newDrone)
                        gameScreen.player.currentChanges.changedDrones.add(newDrone)
                        //TODO: subtract cost of new drone from player money
                    } else {
                        gameScreen.selectedPlanet?.base?.addFacility(Facility.CONSTRUCTION)
                        //TODO: subtract cost of construction facility from player money
                    }
                }
            })
        }

        //Sets up the panel to appear whenever a planet with a base is selected, and disappear otherwise
        this.addAction(object : Action() {
            override fun act(delta: Float): Boolean {
                if (!canBeUsed) {
                    return false
                }
                if (gameScreen.selectedPlanet?.base != null) {
                    children.forEach { it.act(delta) }
                    appear(Direction.LEFT, 1f)
                } else if (gameScreen.selectedPlanet?.base == null) {
                    disappear(Direction.LEFT, 1f)
                }
                return false
            }
        })

        this.add(nameLabel).expandY().row()
        this.add(facilitiesScrollPanel).expandY().row()
        this.add(modifyBaseButton).expandY().row()
        this.add(modifyDroneButton).expandY().row()
        this.add(createDroneButton).expandY()
    }

}