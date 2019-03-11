package com.theapache64.pigeon.ui.activities.splash

import androidx.lifecycle.ViewModel
import com.theapache64.pigeon.data.remote.responses.GetApiKeyResponse
import com.theapache64.pigeon.data.repositories.UserRepository
import com.theapache64.twinkill.utils.livedata.SingleLiveEvent
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val launchActivityEvent = SingleLiveEvent<ActivityID>()

    fun checkUser() {
        userRepository.getUserFromPref().let { user ->
            if (user == null) {

                // not logged, move to login
                launchActivityEvent.notifyFinished(ActivityID.LOGIN)

            } else {

                // logged in , move to main
                launchActivityEvent.notifyFinished(ActivityID.MAIN)
            }
        }
    }

    fun getLaunchActivityEvent(): SingleLiveEvent<ActivityID> = launchActivityEvent

    enum class ActivityID {
        MAIN, LOGIN
    }

}