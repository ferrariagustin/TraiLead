package com.aferrari.trailead.app.viewmodel.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aferrari.trailead.domain.repository.MaterialRepository
import com.aferrari.trailead.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class SplashViewModel(
    private val userRepository: UserRepository,
    private val materialRepository: MaterialRepository
) : ViewModel() {

    var initDatabase: MutableLiveData<Boolean> = MutableLiveData()

    init {
        initLocalDataBase()
    }

    private fun initLocalDataBase() {
        viewModelScope.launch(Dispatchers.Main + SupervisorJob()) {
            userRepository.initLocalDataSource()
            materialRepository.initLocalDataSource()
            initDatabase.postValue(true)
        }
    }
}