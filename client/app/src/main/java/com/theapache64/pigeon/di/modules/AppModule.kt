package com.theapache64.pigeon.di.modules

import android.app.Application
import com.theapache64.twinkill.di.modules.BaseNetworkModule
import dagger.Module
import dagger.Provides
import dagger.android.support.AndroidSupportInjectionModule

@Module(
    includes = [
        AndroidSupportInjectionModule::class,
        BaseNetworkModule::class,
        BuildersModule::class,
        ViewModelModule::class
    ]
)
class AppModule(val application: Application) {

    @Provides
    fun provideApplication(): Application {
        return this.application
    }

}