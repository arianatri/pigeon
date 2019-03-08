package com.theapache64.pigeon.utils.firebase

import com.google.firebase.messaging.FirebaseMessagingService
import com.theapache64.pigeon.data.repositories.PreferenceRepository
import com.theapache64.twinkill.utils.extensions.info
import dagger.android.AndroidInjection
import javax.inject.Inject

class FirebaseService : FirebaseMessagingService() {

    @Inject
    lateinit var preferenceRepository: PreferenceRepository

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)
    }

    override fun onNewToken(token: String?) {
        info("New fcm token is $token")
        token?.let { fcmId ->
            preferenceRepository.save(PreferenceRepository.KEY_FCM_ID, fcmId)
        }
    }
}