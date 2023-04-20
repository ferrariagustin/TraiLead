package com.aferrari.trailead.home.viewmodel.trainee

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aferrari.trailead.home.viewmodel.trainee.home.HomeTraineeViewModel

class TraineeViewModelFactory(private val traineeViewModel: TraineeViewModel) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeTraineeViewModel::class.java)) {
            HomeTraineeViewModel(traineeViewModel) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}