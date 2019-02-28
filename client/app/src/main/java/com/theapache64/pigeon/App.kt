package com.theapache64.pigeon

import android.app.Activity
import android.app.Application
import com.theapache64.pigeon.di.components.DaggerAppComponent
import com.theapache64.twinkill.TwinKill
import com.theapache64.twinkill.utils.Font
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class App : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingAndroidInjector

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.builder()
            .build()
            .inject(this)

        // TwinKill init
        TwinKill.init(
            TwinKill.builder()
                .setDefaultFont(Font.GoogleSansRegular)
                .build()
        )
    }
}