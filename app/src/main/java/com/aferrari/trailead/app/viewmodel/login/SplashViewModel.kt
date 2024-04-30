package com.aferrari.trailead.app.viewmodel.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aferrari.trailead.domain.repository.MaterialRepository
import com.aferrari.trailead.domain.repository.UserRepository

class SplashViewModel(
    private val userRepository: UserRepository,
    private val materialRepository: MaterialRepository
) : ViewModel() {

    var initDatabase: MutableLiveData<Boolean> = MutableLiveData()
}