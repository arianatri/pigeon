package com.theapache64.pigeon.data.remote.responses

import com.squareup.moshi.Json
import com.theapache64.twinkill.data.remote.base.BaseApiResponse

class GetApiKeyResponse(error: Boolean, message: String, data: User?) :
    BaseApiResponse<GetApiKeyResponse.User>(error, message, data) {

    class User(
        @Json(name = "id") val id: String,
        @Json(name = "api_key") val apiKey: String
    ) {
        companion object {
            const val KEY = "user"
        }
    }
}