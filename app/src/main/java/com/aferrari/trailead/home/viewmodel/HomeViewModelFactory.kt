package com.aferrari.trailead.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aferrari.login.db.UserRepository

class HomeViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(HomeLeaderViewModel::class.java)) {
            return HomeLeaderViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(HomeTraineeViewModel::class.java)) {
            return HomeTraineeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}
