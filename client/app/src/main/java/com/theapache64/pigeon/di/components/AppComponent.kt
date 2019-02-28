package com.theapache64.pigeon.di.components

import com.theapache64.pigeon.App
import com.theapache64.pigeon.di.modules.AppModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class
    ]
)
interface AppComponent {
    fun inject(app: App)
}