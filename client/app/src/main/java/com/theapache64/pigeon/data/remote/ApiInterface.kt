package com.theapache64.pigeon.data.remote

import androidx.lifecycle.LiveData
import com.theapache64.pigeon.data.remote.responses.GetApiKeyResponse
import com.theapache64.twinkill.utils.Resource
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiInterface {

    @FormUrlEncoded
    @POST("get_api_key")
    fun getApiKey(
        @Field("name") name: String,
        @Field("imei") imei: String,
        @Field("device_hash") deviceHash: String,
        @Field("fcm_id") fcmId: String
    ): LiveData<Resource<GetApiKeyResponse>>

    @FormUrlEncoded
    @POST("update_fcm")
    fun updateFCM(
        @Field("fcm_id") fcmId: String
    )
}