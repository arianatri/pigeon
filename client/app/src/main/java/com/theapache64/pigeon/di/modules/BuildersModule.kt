package com.theapache64.pigeon.di.modules

import com.theapache64.pigeon.ui.activities.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity

}