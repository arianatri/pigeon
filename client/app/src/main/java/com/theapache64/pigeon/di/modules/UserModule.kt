package com.theapache64.pigeon.di.modules

import com.theapache64.pigeon.data.remote.responses.GetApiKeyResponse
import com.theapache64.pigeon.data.repositories.UserRepository
import com.theapache64.twinkill.utils.extensions.debug
import dagger.Module
import dagger.Provides

@Module
class UserModule {

    @Provides
    fun provideUser(userRepository: UserRepository): GetApiKeyResponse.User? {
        debug("Getting user in user module " + userRepository.getUser())
        return userRepository.getUser()
    }
}