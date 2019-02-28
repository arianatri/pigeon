package com.theapache64.pigeon.di.modules

import androidx.lifecycle.ViewModel
import com.theapache64.pigeon.ui.activities.main.MainViewModel
import com.theapache64.twinkill.di.modules.BaseViewModelModule
import com.theapache64.twinkill.utils.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [BaseViewModelModule::class])
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindSplashViewModel(viewModel: MainViewModel): ViewModel
}