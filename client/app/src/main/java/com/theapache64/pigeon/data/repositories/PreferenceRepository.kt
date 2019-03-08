package com.theapache64.pigeon.data.repositories

import android.content.SharedPreferences
import androidx.core.content.edit
import com.theapache64.twinkill.utils.extensions.info
import javax.inject.Inject

class PreferenceRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {


    /**
     * Gets string
     */
    fun getString(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    fun save(key: String, value: String) {
        sharedPreferences.edit {
            putString(key, value)
            info("Saved new  pref - `$key` : `$value`")
        }
    }

    companion object {
        const val KEY_FCM_ID = "fcm_id"
    }
}