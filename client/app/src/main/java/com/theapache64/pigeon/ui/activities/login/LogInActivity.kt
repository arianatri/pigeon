package com.theapache64.pigeon.ui.activities.login

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.BasePermissionListener
import com.karumi.dexter.listener.single.CompositePermissionListener
import com.karumi.dexter.listener.single.PermissionListener
import com.karumi.dexter.listener.single.SnackbarOnDeniedPermissionListener
import com.theapache64.pigeon.R
import com.theapache64.pigeon.data.repositories.DeviceRepository
import com.theapache64.pigeon.databinding.ActivityLogInBinding
import com.theapache64.twinkill.utils.extensions.bindContentView
import com.theapache64.twinkill.utils.extensions.toast
import dagger.android.AndroidInjection
import javax.inject.Inject

class LogInActivity : AppCompatActivity(), LogInView {

    companion object {
        fun getStartIntent(context: Context): Intent {
            val intent = Intent(context, LogInActivity::class.java)
            return intent
        }
    }

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var deviceRepository: DeviceRepository

    private lateinit var binding: ActivityLogInBinding
    private lateinit var viewModel: LogInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        this.binding = bindContentView<ActivityLogInBinding>(R.layout.activity_log_in)
        this.viewModel = ViewModelProviders.of(this, factory).get(LogInViewModel::class.java)

        // hiding for the first time
        binding.contentLogIn.clpbLogin.hide()

        // Checking permission
        checkPermission()

    }

    private fun checkPermission() {
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.READ_PHONE_STATE)
            .withListener(getPermissionListener())
            .check()
    }

    private var isCheckPermissionOnResume: Boolean = false

    override fun onResume() {
        super.onResume()

        if (isCheckPermissionOnResume) {
            isCheckPermissionOnResume = false
            checkPermission()
        }
    }

    private fun getPermissionListener(): PermissionListener? {

        val sb =
            SnackbarOnDeniedPermissionListener.Builder.with(binding.clLogIn, R.string.permission_device_state)
                .withOpenSettingsButton(R.string.action_settings)
                .withDuration(Snackbar.LENGTH_INDEFINITE)
                .withCallback(object : Snackbar.Callback() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        isCheckPermissionOnResume = true
                    }
                })
                .build()

        val pl = object : BasePermissionListener() {
            override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                token!!.continuePermissionRequest()
            }

            override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                // Binding params
                /*binding.contentLogIn.view = this@LogInActivity
                val imei = deviceRepository.getImei(this@LogInActivity)
                viewModel.
                binding.contentLogIn.imei = viewModel.imei*/
            }
        }

        return CompositePermissionListener(sb, pl)
    }

    override fun onStartClicked() {

    }


}
