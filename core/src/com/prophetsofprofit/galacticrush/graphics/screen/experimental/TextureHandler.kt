package com.prophetsofprofit.galacticrush.graphics.screen.experimental

import com.badlogic.gdx.graphics.Texture
import com.prophetsofprofit.galacticrush.baseDroneImage
import com.prophetsofprofit.galacticrush.logic.drone.Drone
import com.prophetsofprofit.galacticrush.logic.drone.DroneId
import com.prophetsofprofit.galacticrush.logic.map.Planet

/**
 * An object that generates/gets textures for a given game object
 */
class TextureHandler {

    private val planetTextures = mutableMapOf<Int, Texture>()
    private val droneTextures = mutableMapOf<DroneId, Texture>()

    /**
     * Generates a texture for a planet
     */
    fun getTexture(planet: Planet): Texture {
        if (!this.planetTextures.containsKey(planet.id)) {
            this.planetTextures[planet.id] = Texture("image/planets/planet${(Math.random() * 5).toInt()}.png")
        }
        return this.planetTextures[planet.id]!!
    }

    /**
     * Generates a texture for a drone
     */
    fun getTexture(drone: Drone): Texture {
        if (!this.droneTextures.containsKey(drone.id)) {
            this.droneTextures[drone.id] = baseDroneImage
        }
        return this.droneTextures[drone.id]!!
    }

}