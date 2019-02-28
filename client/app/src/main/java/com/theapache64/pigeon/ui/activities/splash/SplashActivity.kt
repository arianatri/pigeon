package com.theapache64.pigeon.ui.activities.splash

import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.theapache64.pigeon.R
import com.theapache64.pigeon.data.remote.ApiInterface
import com.theapache64.twinkill.ui.activities.base.BaseAppCompatActivity
import com.theapache64.twinkill.utils.extensions.info
import dagger.android.AndroidInjection
import javax.inject.Inject

class SplashActivity : BaseAppCompatActivity() {

    @Inject
    lateinit var apiInterface: ApiInterface

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val viewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)

        // Watching for next activity launch
        viewModel.getLaunchActivityEvent().observe(this, Observer {
            TODO("Not implemented")
        })

        Handler().postDelayed({
            viewModel.checkUser()
        }, 2000)
    }

}
