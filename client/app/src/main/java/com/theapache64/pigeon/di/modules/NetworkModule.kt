package com.theapache64.pigeon.di.modules

import com.theapache64.pigeon.data.remote.ApiInterface
import com.theapache64.twinkill.di.modules.BaseNetworkModule
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [BaseNetworkModule::class])
class NetworkModule {

    // Interface
    @Singleton
    @Provides
    fun provideApiInterface(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }

}