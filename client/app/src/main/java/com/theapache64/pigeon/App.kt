package com.theapache64.pigeon

import android.app.Activity
import android.app.Application
import android.app.Service
import com.theapache64.pigeon.di.components.DaggerAppComponent
import com.theapache64.pigeon.di.modules.AppModule
import com.theapache64.twinkill.TwinKill
import com.theapache64.twinkill.di.modules.BaseNetworkModule
import com.theapache64.twinkill.di.modules.ContextModule
import com.theapache64.twinkill.utils.Font
import com.theapache64.twinkill.utils.retrofit.interceptors.CurlInterceptor
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
import javax.inject.Inject

class App : Application(), HasActivityInjector, HasServiceInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var serviceInjector: DispatchingAndroidInjector<Service>

    override fun serviceInjector(): AndroidInjector<Service> = serviceInjector

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .contextModule(ContextModule(this))
            //.baseNetworkModule(BaseNetworkModule("http://theapache64.com/pigeon/v1/"))
            .baseNetworkModule(BaseNetworkModule("http://192.168.2.177:8080/pigeon/v1/"))
            .build()
            .inject(this)

        // TwinKill init
        TwinKill.init(
            TwinKill.builder()
                .addOkHttpInterceptor()
                .addOkHttpInterceptor(CurlInterceptor())
                .setDefaultFont(Font.GoogleSansRegular)
                .build()
        )
    }
}