package com.prophetsofprofit.galacticrush

/**
 * A data class that represents various options the user can change or control
 * Changes shouldn't change an options object, but should set the main class's options field to a new options object with different fields
 * Parameters have default values
 */
data class Options(val musicVolume: Float = 0.25f, val soundVolume: Float = 0.5f)