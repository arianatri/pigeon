package com.theapache64.pigeon.data.remote

import androidx.lifecycle.LiveData
import com.theapache64.pigeon.data.remote.responses.GetApiKeyResponse
import com.theapache64.twinkill.data.remote.base.BaseApiResponse
import com.theapache64.twinkill.utils.Resource
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiInterface {

    /**
     * To get API key
     */
    @FormUrlEncoded
    @POST("get_api_key")
    fun getApiKey(
        @Field("name") name: String,
        @Field("imei") imei: String,
        @Field("device_hash") deviceHash: String,
        @Field("fcm_id") fcmId: String
    ): LiveData<Resource<GetApiKeyResponse>>


    /**
     * To update fcm
     */
    @FormUrlEncoded
    @POST("update_fcm")
    fun updateFCM(
        @Field("fcm_id") fcmId: String
    ): Single<BaseApiResponse<Any>>
}