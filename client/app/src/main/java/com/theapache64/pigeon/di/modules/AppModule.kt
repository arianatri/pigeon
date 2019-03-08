package com.theapache64.pigeon.di.modules

import android.app.Application
import com.theapache64.twinkill.di.modules.PreferenceModule
import dagger.Module
import dagger.Provides
import dagger.android.support.AndroidSupportInjectionModule

@Module(
    includes = [
        AndroidSupportInjectionModule::class,
        NetworkModule::class,
        ActivityBuildersModule::class,
        ServiceBuildersModule::class,
        ViewModelModule::class,
        PreferenceModule::class
    ]
)
class AppModule(private val application: Application) {

    @Provides
    fun provideApplication(): Application {
        return this.application
    }

}