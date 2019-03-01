package com.theapache64.pigeon.ui.activities.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.theapache64.pigeon.data.repositories.UserRepository
import javax.inject.Inject

class LogInViewModel @Inject constructor(
    userRepository: UserRepository
) : ViewModel() {

}