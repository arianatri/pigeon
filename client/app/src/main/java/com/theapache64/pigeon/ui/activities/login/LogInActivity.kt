package com.theapache64.pigeon.ui.activities.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.theapache64.pigeon.R
import com.theapache64.pigeon.databinding.ActivityLogInBinding
import com.theapache64.twinkill.utils.extensions.bindContentView
import com.theapache64.twinkill.utils.extensions.toast

class LogInActivity : AppCompatActivity(), LogInView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = bindContentView<ActivityLogInBinding>(R.layout.activity_log_in)
        binding.contentLogIn.view = this
    }

    override fun onStartClicked() {
        toast("Start clicked")
    }


}
