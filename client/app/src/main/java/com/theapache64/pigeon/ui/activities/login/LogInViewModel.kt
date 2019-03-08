package com.theapache64.pigeon.ui.activities.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.theapache64.pigeon.R
import com.theapache64.pigeon.data.remote.responses.GetApiKeyResponse
import com.theapache64.pigeon.data.repositories.DeviceRepository
import com.theapache64.pigeon.data.repositories.UserRepository
import com.theapache64.twinkill.utils.DarkKnight
import com.theapache64.twinkill.utils.Resource
import javax.inject.Inject

class LogInViewModel @Inject constructor(
    application: Application,
    private val deviceRepository: DeviceRepository,
    private val userRepository: UserRepository
) : AndroidViewModel(application) {

    private val isReadyToReadApiKey = MutableLiveData<Boolean>()

    private val getApiKeyResponse = Transformations.switchMap(isReadyToReadApiKey) { isReadyToRead ->
        if (isReadyToRead) {
            // Generating unique id for the user
            val deviceHash = DarkKnight.getEncrypted("$name/$imei")
            userRepository.logIn(name, imei, deviceHash)
        } else {
            null
        }
    }

    var name = ""
    var imei = ""

    fun readImei() {
        this.imei = deviceRepository.getImei(getApplication())
    }

    fun readDeviceOwnerName() {
        this.name = deviceRepository.getOwnerName(getApplication())
    }

    fun readApiKey(onFailed: (stringRes: Int) -> Unit) {

        if (name.isEmpty()) {
            return onFailed(R.string.login_error_name)
        }

        if (imei.isEmpty()) {
            return onFailed(R.string.login_error_imei)
        }

        this.isReadyToReadApiKey.value = true
    }

    fun getApiKeyResponse(): LiveData<Resource<GetApiKeyResponse>> = getApiKeyResponse
}