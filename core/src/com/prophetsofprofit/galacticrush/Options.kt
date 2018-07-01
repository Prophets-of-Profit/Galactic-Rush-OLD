package com.prophetsofprofit.galacticrush

import java.io.Serializable

/**
 * A data class that represents various options the user can change or control
 * Changes shouldn't change an options object, but should set the main class's options field to a new options object with different fields
 */
data class Options(val musicVolume: Int, val soundVolume: Int): Serializable