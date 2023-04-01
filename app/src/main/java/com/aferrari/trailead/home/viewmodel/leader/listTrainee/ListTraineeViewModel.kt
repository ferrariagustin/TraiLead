package com.aferrari.trailead.home.viewmodel.leader.listTrainee

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aferrari.login.db.Category
import com.aferrari.login.db.Material
import com.aferrari.login.db.Trainee
import com.aferrari.trailead.home.viewmodel.StatusUpdateInformation
import com.aferrari.trailead.home.viewmodel.leader.HomeLeaderViewModel

class ListTraineeViewModel(private val homeLeaderViewModel: HomeLeaderViewModel) : ViewModel() {

    var categorySelected: Category? = null
    val statusUpdateRadioButtonSelectedAll = MutableLiveData<StatusUpdateInformation>()
    lateinit var traineeSelected: Trainee

    init {
        statusUpdateRadioButtonSelectedAll.value = StatusUpdateInformation.FAILED
        homeLeaderViewModel.getAllMaterials()
        homeLeaderViewModel.getAllCategoryForLeader()
        homeLeaderViewModel.traineeSelected?.let {
            traineeSelected = it
        }
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
        val categorySelected = homeLeaderViewModel.getCategoryBy(category)
        categorySelected?.let { categorySelected ->
            return homeLeaderViewModel.listAllMaterials.value?.filter { it.categoryId == categorySelected.id }
        }
        return null
    }

    /**
     * Return list of materials for lider
     */
    fun getMaterials(): List<Material>? = homeLeaderViewModel.listAllMaterials.value

    /**
     * Return list of category for lider
     */
    fun getCategories(): List<Category>? = homeLeaderViewModel.listCategory.value

    /**
     * Linked all material from category selected with de trainee selected
     */
    fun linkedAllMaterialFromCategorySelected(categorySpinnerSelected: String?) {
        if (categorySpinnerSelected == null) {
            return
        }
        categorySelected = homeLeaderViewModel.getCategoryBy(categorySpinnerSelected)


    }
}