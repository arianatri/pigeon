package com.theapache64.pigeon.ui.activities.splash

import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.theapache64.pigeon.R
import com.theapache64.pigeon.ui.activities.login.LogInActivity
import com.theapache64.pigeon.ui.activities.main.MainActivity
import com.theapache64.twinkill.ui.activities.base.BaseAppCompatActivity
import com.theapache64.twinkill.utils.extensions.toast
import dagger.android.AndroidInjection
import javax.inject.Inject

class SplashActivity : BaseAppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val viewModel = ViewModelProviders.of(this, factory).get(SplashViewModel::class.java)

        // Watching for next activity launch
        viewModel.getLaunchActivityEvent().observe(this, Observer {
            when (it) {

                SplashViewModel.ActivityID.MAIN -> {
                    startActivity(MainActivity.getStartIntent(this))
                }

                SplashViewModel.ActivityID.LOGIN -> {
                    startActivity(LogInActivity.getStartIntent(this))
                }

                else -> {
                    toast("Undefined activity launch id $it")
                }
            }

            // end splash
            finish()
        })

        Handler().postDelayed({
            viewModel.checkUser()
        }, 2000)
    }

}
