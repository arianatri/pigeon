package com.theapache64.pigeon.di.modules

import com.theapache64.pigeon.utils.firebase.FirebaseService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ServiceBuildersModule {

    @ContributesAndroidInjector
    abstract fun getFirebaseService(): FirebaseService
}