package com.aferrari.trailead.home.viewmodel.leader.listTrainee

import androidx.lifecycle.ViewModel
import com.aferrari.trailead.home.viewmodel.leader.HomeLeaderViewModel

class ListTraineeViewModel(private val homeLeaderViewModel: HomeLeaderViewModel) : ViewModel() {

    init {
        homeLeaderViewModel.getAllMaterials()
        homeLeaderViewModel.getAllCategoryForLeader()
    }

    fun getAllCategoryToString(): MutableList<String> {
        val mutableList = mutableListOf<String>()
        homeLeaderViewModel.listCategory.value?.forEach {
            mutableList.add(it.name)
        }
        return mutableList
    }
}