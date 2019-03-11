package com.theapache64.pigeon.data.repositories

import androidx.lifecycle.LiveData
import com.theapache64.pigeon.data.remote.ApiInterface
import com.theapache64.pigeon.data.remote.responses.GetApiKeyResponse
import com.theapache64.twinkill.data.remote.base.BaseApiResponse
import com.theapache64.twinkill.utils.Resource
import com.theapache64.twinkill.utils.extensions.debug
import com.theapache64.twinkill.utils.extensions.info
import com.theapache64.twinkill.utils.extensions.mistake
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
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

    fun updateFCM(fcmId: String) {
        return apiInterface
            .updateFCM(fcmId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<BaseApiResponse<Any>> {
                override fun onSuccess(t: BaseApiResponse<Any>) {
                    if (t.error) {
                        mistake("Failed to update fcm `${t.message}`")
                    } else {
                        info("FCM Updated")
                    }
                }

                override fun onSubscribe(d: Disposable) {
                    debug("Updating FCM")
                }

                override fun onError(e: Throwable) {
                    mistake("Failed to update FCM")
                }

            })
    }

}