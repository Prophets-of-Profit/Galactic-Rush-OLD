package com.prophetsofprofit.galacticrush.networking

import com.prophetsofprofit.galacticrush.jsonObject
import com.prophetsofprofit.galacticrush.localHostIp
import com.prophetsofprofit.galacticrush.userAddressesFile

/**
 * A utility object that handles saving and reading saved addresses for user convenience
 */
object SavedHostAddresses {

    //The host addresses that the user has saved; keys are name of host and values are address
    var savedAddresses = mutableMapOf("Localhost" to localHostIp)

    /**
     * Saves the hashmap of saved addresses to the json file specified in constants
     */
    fun saveAddresses() {
        userAddressesFile.writeString(jsonObject.prettyPrint(this.savedAddresses), false)
    }

    /**
     * Tries to retrieve the user's saved addresses
     */
    fun updateSavedAddresses() {
        try {
            this.savedAddresses = jsonObject.fromJson(HashMap::class.java, userAddressesFile.readString()) as MutableMap<String, String>
        } catch (ignored: Exception) {
        }
    }

}