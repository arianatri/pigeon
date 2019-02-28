package com.theapache64.pigeon

import android.app.Activity
import android.app.Application
import com.theapache64.pigeon.di.components.DaggerAppComponent
import com.theapache64.twinkill.TwinKill
import com.theapache64.twinkill.di.modules.BaseNetworkModule
import com.theapache64.twinkill.utils.Font
import com.theapache64.twinkill.utils.retrofit.interceptors.CurlInterceptor
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
            .baseNetworkModule(BaseNetworkModule("http://theapache64.com/pigeon/v1/"))
            .build()
            .inject(this)

        // TwinKill init
        TwinKill.init(
            TwinKill.builder()
                .addOkHttpInterceptor(CurlInterceptor())
                .setDefaultFont(Font.GoogleSansRegular)
                .build()
        )
    }
}