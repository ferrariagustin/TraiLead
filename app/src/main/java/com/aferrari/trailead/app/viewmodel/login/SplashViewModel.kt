package com.aferrari.trailead.app.viewmodel.login

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aferrari.trailead.domain.repository.MaterialRepository
import com.aferrari.trailead.domain.repository.UserRepository

class SplashViewModel(
    private val userRepository: UserRepository,
    private val materialRepository: MaterialRepository
) : ViewModel() {
    fun validateSession(context: Context) {
    }

    var initDatabase: MutableLiveData<Boolean> = MutableLiveData()
}