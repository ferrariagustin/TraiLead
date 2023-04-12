package com.aferrari.trailead.home.viewmodel.leader.listTrainee

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aferrari.login.db.Category
import com.aferrari.login.db.Material
import com.aferrari.login.db.Trainee
import com.aferrari.trailead.home.viewmodel.StatusUpdateInformation
import com.aferrari.trailead.home.viewmodel.leader.HomeLeaderViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListTraineeViewModel(private val homeLeaderViewModel: HomeLeaderViewModel) : ViewModel() {

    val statusUpdateRadioButtonSelectedAll = MutableLiveData<StatusUpdateInformation>()

    val setCategorySelected = MutableLiveData<MutableSet<Category>>(mutableSetOf())

    lateinit var traineeSelected: Trainee

    init {
        statusUpdateRadioButtonSelectedAll.value = StatusUpdateInformation.NONE
        homeLeaderViewModel.getAllMaterials()
        homeLeaderViewModel.getAllCategoryForLeader()
        homeLeaderViewModel.traineeSelected?.let {
            traineeSelected = it
        }
        // TODO: Enteder porque no se esta inicializando correctamente el set category selected. Aparece el set nulo
        getCategoriesSelected()
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
                    homeLeaderViewModel.repository.setLinkedCategory(
                        traineeSelected.id,
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
            val result = withContext(Dispatchers.Main) {
                homeLeaderViewModel.repository.getCategoriesSelected(
                    traineeSelected.id
                )
            }
            setCategorySelected.value?.addAll(result)
        }
    }


    fun addCategorySelected(category: Category) {
        setCategorySelected.value?.add(category)
    }

    fun removeCategorySelected(category: Category) {
        setCategorySelected.value?.remove(category)
    }
}