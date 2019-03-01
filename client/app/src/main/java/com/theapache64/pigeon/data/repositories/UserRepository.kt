package com.theapache64.pigeon.data.repositories

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.squareup.moshi.Moshi
import com.theapache64.pigeon.data.remote.ApiInterface
import com.theapache64.pigeon.data.remote.responses.GetApiKeyResponse
import com.theapache64.twinkill.utils.Resource
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiInterface: ApiInterface,
    private val sharedPreferences: SharedPreferences,
    private val moshi: Moshi
) {

    /**
     * Get user from preference
     */
    fun getUser(): GetApiKeyResponse.User? {

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

    fun logIn(name: String, deviceHash: String, imei: String): LiveData<Resource<GetApiKeyResponse>> {
        return apiInterface.getApiKey(name, deviceHash, imei)
    }

}