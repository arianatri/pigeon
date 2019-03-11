package com.theapache64.pigeon.data.repositories

import androidx.lifecycle.LiveData
import com.theapache64.pigeon.data.remote.ApiInterface
import com.theapache64.pigeon.data.remote.responses.GetApiKeyResponse
import com.theapache64.twinkill.utils.Resource
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiInterface: ApiInterface
) {

    /**
     * Process login via REST API
     */
    fun logIn(name: String, imei: String, deviceHash: String, fcmId: String): LiveData<Resource<GetApiKeyResponse>> {
        return apiInterface.getApiKey(name, imei, deviceHash, fcmId)
    }

    fun updateFCM(fcmId: String): LiveData<Resource<GetApiKeyResponse>> {
        return apiInterface.updateFCM(fcmId)
    }

}