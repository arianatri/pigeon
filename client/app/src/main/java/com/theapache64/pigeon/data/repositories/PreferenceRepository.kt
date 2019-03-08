package com.theapache64.pigeon.data.repositories

import android.content.SharedPreferences
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

    companion object {
        const val KEY_FCM_ID = "fcm_id"
    }
}