package com.aferrari.trailead.app.viewmodel.leader.listTrainee

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aferrari.trailead.app.viewmodel.leader.LeaderViewModel
import com.aferrari.trailead.domain.models.Category
import com.aferrari.trailead.domain.models.Material
import com.aferrari.trailead.domain.models.Trainee
import com.aferrari.trailead.common.common_enum.StatusUpdateInformation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListTraineeViewModel(private val leaderViewModel: LeaderViewModel) : ViewModel() {

    val statusUpdateRadioButtonSelectedAll = MutableLiveData<StatusUpdateInformation>()

    val setCategorySelected = MutableLiveData<MutableSet<Category>>()

    val allCategorySelectedSize = MutableLiveData<Int>()

    lateinit var traineeSelected: Trainee

    init {
        statusUpdateRadioButtonSelectedAll.value = StatusUpdateInformation.NONE
        leaderViewModel.getAllMaterials()
        leaderViewModel.getAllCategoryForLeader()
        leaderViewModel.traineeSelected?.let {
            traineeSelected = it
        }
        // TODO: Enteder porque no se esta inicializando correctamente el set category selected. Aparece el set nulo
//        getCategoriesSelected()
    }

    fun getAllCategoryToString(): MutableList<String> {
        val mutableList = mutableListOf<String>()
        leaderViewModel.listCategory.value?.forEach {
            mutableList.add(it.name)
        }
        return mutableList
    }

    /**
     * Get all materials by category
     */
    fun getMaterialsBy(category: String): List<Material>? {
        val categorySelected = leaderViewModel.getCategoryBy(category)
        categorySelected?.let { categorySelected ->
            return leaderViewModel.listAllMaterials.value?.filter { it.categoryId == categorySelected.id }
        }
        return null
    }

    /**
     * Return list of materials for lider
     */
    fun getMaterials(): List<Material>? = leaderViewModel.listAllMaterials.value

    /**
     * Return list of category for lider
     */
    fun getCategories(): List<Category>? = leaderViewModel.listCategory.value

    /**
     * Selected all category card views
     */
    fun selectedAllCategory() {
        statusUpdateRadioButtonSelectedAll.value = StatusUpdateInformation.SUCCESS
    }

    /**
     * Unselected all category card views
     */
    fun unselectedAllCategory() {
        statusUpdateRadioButtonSelectedAll.value = StatusUpdateInformation.FAILED
    }

    /**
     * Save all category with trainee selected
     */
    fun saveCategorySelected() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                setCategorySelected.value?.let {
                    leaderViewModel.materialRepository.setLinkedCategory(
                        traineeSelected.userId,
                        it
                    )
                }
            }
        }
    }

    /**
     * Save all category with trainee selected
     */
    fun getCategoriesSelected() {
        viewModelScope.launch {
            val result =
                leaderViewModel.materialRepository.getCategoriesSelected(traineeSelected.userId)
            setCategorySelected.value = result.toMutableSet()
            updateSizeCategorySelected()
        }
    }

    fun addCategorySelected(category: Category) {
        setCategorySelected.value?.add(category)
        updateSizeCategorySelected()
    }

    fun removeCategorySelected(category: Category) {
        setCategorySelected.value?.remove(category)
        updateSizeCategorySelected()
    }

    private fun updateSizeCategorySelected() {
        allCategorySelectedSize.value = setCategorySelected.value?.size
    }
}