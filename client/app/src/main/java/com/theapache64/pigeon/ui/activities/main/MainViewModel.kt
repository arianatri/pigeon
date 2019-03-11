package com.theapache64.pigeon.ui.activities.main

import androidx.lifecycle.ViewModel
import com.theapache64.pigeon.data.repositories.UserPrefRepository
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val userRepository: UserPrefRepository
) : ViewModel() {

    val user = userRepository.getUserFromPref()
}
