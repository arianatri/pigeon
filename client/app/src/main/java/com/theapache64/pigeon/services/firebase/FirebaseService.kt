package com.theapache64.pigeon.services.firebase

import androidx.lifecycle.ViewModelProviders
import com.google.firebase.messaging.FirebaseMessagingService
import com.theapache64.pigeon.data.repositories.AuthRepository
import com.theapache64.pigeon.data.repositories.PreferenceRepository
import com.theapache64.twinkill.utils.extensions.info
import dagger.android.AndroidInjection
import javax.inject.Inject

class FirebaseService : FirebaseMessagingService() {

    @Inject
    lateinit var preferenceRepository: PreferenceRepository

    @Inject
    lateinit var authRepository: AuthRepository


    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)
    }

    override fun onNewToken(token: String?) {
        info("New fcm token is $token")

        token?.let { fcmId ->
            // Saving to pref
            preferenceRepository.save(PreferenceRepository.KEY_FCM_ID, fcmId)

            // Sending to server
            authRepository.updateFCM(fcmId)

        }
    }
}