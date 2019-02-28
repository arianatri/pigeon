package com.theapache64.pigeon.di.modules

import com.theapache64.pigeon.ui.activities.main.MainActivity
import com.theapache64.pigeon.ui.activities.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    abstract fun getSplashActivity(): SplashActivity

    @ContributesAndroidInjector
    abstract fun getMainActivity(): MainActivity

}