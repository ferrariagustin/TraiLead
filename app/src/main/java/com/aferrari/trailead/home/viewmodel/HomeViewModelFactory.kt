package com.aferrari.trailead.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aferrari.login.data.user.repository.UserRepository
import com.aferrari.login.data.material.repository.MaterialRepository
import com.aferrari.trailead.home.viewmodel.leader.LeaderViewModel
import com.aferrari.trailead.home.viewmodel.trainee.TraineeViewModel

class HomeViewModelFactory(
    private val repository: UserRepository,
    private val materialRepository: MaterialRepository? = null
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository) as T
        }
        if (materialRepository != null && modelClass.isAssignableFrom(LeaderViewModel::class.java)) {
            return LeaderViewModel(repository, materialRepository) as T
        }
        if (materialRepository != null && modelClass.isAssignableFrom(TraineeViewModel::class.java)) {
            return TraineeViewModel(repository, materialRepository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}
