package org.example.mandm

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import com.russhwolf.settings.get

object AppPreferences {

    private val settings: Settings = Settings()

    // --------------------------
    // Generic Save/Get Methods
    // --------------------------

    fun saveString(key: String, value: String) {
        settings[key] = value
    }

    fun getString(key: String, default: String? = null): String? =
        settings.getStringOrNull(key) ?: default

    fun saveInt(key: String, value: Int) {
        settings[key] = value
    }

    fun getInt(key: String, default: Int = 0): Int =
        settings.getInt(key, default)

    fun saveJson(key: String, json: String) {
        settings[key] = json
    }

    fun getJson(key: String): String? = settings.getStringOrNull(key)

    fun saveBoolean(key: String, value: Boolean) {
        settings[key] = value
    }

    fun getBoolean(key: String, default: Boolean = false): Boolean =
        settings.getBoolean(key, default)

    // --------------------------
    // Specific App Fields
    // --------------------------

    private object Keys {
        const val USER_ID = "user_id"
        const val IS_LOGGED_IN = "is_logged_in"
        const val PROFILE_JSON = "profile_json"
        const val USER_NAME = "user_name"
        const val CONTACT_NUMBER = "contact_number"
        const val ADDRESS = "address"
        const val VILLAGE = "village"
        const val ROLE = "role"
        const val IS_SUBSCRIBED = "is_subscribed"
    }

    fun setUserId(value: String) = saveString(Keys.USER_ID, value)
    fun getUserId(): String? = getString(Keys.USER_ID)

    fun setIsUserLoggedIn(value: Boolean) = saveBoolean(Keys.IS_LOGGED_IN, value)
    fun isUserLoggedIn(): Boolean = getBoolean(Keys.IS_LOGGED_IN, false)

    fun setProfileJson(value: String) = saveJson(Keys.PROFILE_JSON, value)
    fun getProfileJson(): String? = getJson(Keys.PROFILE_JSON)

    fun setUserName(value: String) = saveString(Keys.USER_NAME, value)
    fun getUserName(): String? = getString(Keys.USER_NAME)

    fun setContactNumber(value: String) = saveString(Keys.CONTACT_NUMBER, value)
    fun getContactNumber(): String? = getString(Keys.CONTACT_NUMBER)

    fun setAddress(value: String) = saveString(Keys.ADDRESS, value)
    fun getAddress(): String? = getString(Keys.ADDRESS)

    fun setVillage(value: String) = saveString(Keys.VILLAGE, value)
    fun getVillage(): String? = getString(Keys.VILLAGE)

    fun setRole(value: String) = saveString(Keys.ROLE, value)
    fun getRole(): String? = getString(Keys.ROLE)

    fun setIsSubscribed(value: Boolean) = saveBoolean(Keys.IS_SUBSCRIBED, value)
    fun isSubscribed(): Boolean = getBoolean(Keys.IS_SUBSCRIBED, false)

    // Clear everything
    fun clear() {
        settings.clear()
    }
}
