package com.aferrari.trailead.home.viewmodel.leader.listTrainee

import androidx.lifecycle.ViewModel
import com.aferrari.trailead.home.viewmodel.leader.HomeLeaderViewModel

class ListTraineeViewModel(private val homeLeaderViewModel: HomeLeaderViewModel): ViewModel() {

    init {
        homeLeaderViewModel.getAllMaterials()
    }
}