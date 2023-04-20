package com.aferrari.trailead.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aferrari.login.data.UserRepository
import com.aferrari.trailead.home.viewmodel.leader.LeaderViewModel
import com.aferrari.trailead.home.viewmodel.trainee.TraineeViewModel

class HomeViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(LeaderViewModel::class.java)) {
            return LeaderViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(TraineeViewModel::class.java)) {
            return TraineeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}
