package com.theapache64.pigeon.data.repositories

import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import com.theapache64.pigeon.data.remote.responses.GetApiKeyResponse
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val moshi: Moshi
) {
    fun getUserFromPref(): GetApiKeyResponse.User? {
        // getting json theUser string from pref
        val userJson: String? = sharedPreferences.getString(GetApiKeyResponse.User.KEY, null)
        var user: GetApiKeyResponse.User? = null
        if (userJson != null) {
            // converting JSON to Model
            val moshiAdapter = moshi.adapter(GetApiKeyResponse.User::class.java)
            user = moshiAdapter.fromJson(userJson)
        }

        return user
    }
}