package com.aferrari.trailead.home.viewmodel.leader.listTrainee

import androidx.lifecycle.ViewModel
import com.aferrari.login.db.Category
import com.aferrari.login.db.Material
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

    /**
     * Get all materials by category
     */
    fun getMaterialsBy(category: String): List<Material>? {
        val categorySelected = getCategoryBy(category)
        categorySelected?.let { categorySelected ->
            return homeLeaderViewModel.listMaterialLeader.value?.filter { it.categoryId == categorySelected.id }
        }
        return null
    }


    private fun getCategoryBy(category: String): Category? {
        homeLeaderViewModel.listCategory.value?.forEach {
            if (it.name == category) return it
        }
        return null
    }
}