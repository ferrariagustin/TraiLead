package com.aferrari.trailead.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aferrari.trailead.app.viewmodel.leader.LeaderViewModel
import com.aferrari.trailead.app.viewmodel.trainee.TraineeViewModel
import com.aferrari.trailead.domain.repository.MaterialRepository
import com.aferrari.trailead.domain.repository.UserRepository

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
