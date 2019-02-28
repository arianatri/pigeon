package com.theapache64.pigeon.ui.activities.splash

import androidx.lifecycle.ViewModel
import com.theapache64.twinkill.utils.livedata.SingleLiveEvent
import javax.inject.Inject

class SplashViewModel @Inject constructor() : ViewModel() {

    private val launchActivityEvent = SingleLiveEvent<Int>()

    fun checkUser() {

    }

    fun getLaunchActivityEvent(): SingleLiveEvent<Int> = launchActivityEvent

}