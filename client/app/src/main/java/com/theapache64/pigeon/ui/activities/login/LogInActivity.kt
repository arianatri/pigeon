package com.theapache64.pigeon.ui.activities.login

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener
import com.theapache64.pigeon.R
import com.theapache64.pigeon.databinding.ActivityLogInBinding
import com.theapache64.twinkill.utils.Resource
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

    private lateinit var binding: ActivityLogInBinding
    private lateinit var viewModel: LogInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        this.binding = bindContentView(R.layout.activity_log_in)
        this.viewModel = ViewModelProviders.of(this, factory).get(LogInViewModel::class.java)

        // hiding for the first time
        hideLoading()

        // Checking permission
        checkPermission()
    }

    private fun checkPermission() {
        Dexter.withActivity(this)
            .withPermissions(
                // To get IMEI
                Manifest.permission.READ_PHONE_STATE,

                // To get device owner name
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.GET_ACCOUNTS
            )
            .withListener(getPermissionListener())
            .check()
    }

    private var isCheckPermissionOnResume: Boolean = false

    override fun onResume() {
        super.onResume()

        // Checking if resume comes after permission request
        if (isCheckPermissionOnResume) {
            isCheckPermissionOnResume = false
            // checking permission again
            checkPermission()
        }
    }

    /**
     * Returns permission listener needed for Dexter
     */
    private fun getPermissionListener(): MultiplePermissionsListener {

        // permission listener
        val permissionListener = object : BaseMultiplePermissionsListener() {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                if (report!!.areAllPermissionsGranted()) {
                    init()
                }
            }
        }

        // permission denied listener
        val permissionDeniedListener =
            SnackbarOnAnyDeniedMultiplePermissionsListener.Builder.with(
                binding.clLogIn,
                R.string.permission_read_device_details
            )
                .withOpenSettingsButton(R.string.action_settings)
                .withDuration(Snackbar.LENGTH_INDEFINITE)
                .withCallback(object : Snackbar.Callback() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        isCheckPermissionOnResume = true
                    }
                })
                .build()

        // converting two permission into one
        return CompositeMultiplePermissionsListener(permissionListener, permissionDeniedListener)
    }

    private fun init() {

        viewModel.readDeviceOwnerName()
        viewModel.readImei()

        binding.contentLogIn.viewModel = viewModel
        binding.contentLogIn.view = this@LogInActivity

        // Watching for login response
        viewModel.getApiKeyResponse().observe(this, Observer { response ->

            when (response.status) {

                Resource.Status.LOADING -> {
                    binding.contentLogIn.clpbLogin.show()
                }

                Resource.Status.SUCCESS -> {
                    hideLoading()
                    toast(response.data!!.message)
                }

                Resource.Status.ERROR -> {
                    hideLoading()
                    toast(response.message!!)
                }
            }
        })

    }

    private fun hideLoading() {
        binding.contentLogIn.clpbLogin.hide()
    }

    override fun onStartClicked() {
        viewModel.readApiKey { failedReason ->
            toast(failedReason)
        }
    }


}
