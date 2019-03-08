package com.theapache64.pigeon.di.modules

import com.theapache64.pigeon.ui.activities.login.LogInActivity
import com.theapache64.pigeon.ui.activities.main.MainActivity
import com.theapache64.pigeon.ui.activities.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector
    abstract fun getSplashActivity(): SplashActivity

    @ContributesAndroidInjector
    abstract fun getMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun getLogInActivity(): LogInActivity

}