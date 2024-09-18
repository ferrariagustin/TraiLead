package com.aferrari.trailead.app.viewmodel.leader.listTrainee

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aferrari.trailead.app.viewmodel.leader.LeaderViewModel
import com.aferrari.trailead.common.common_enum.StatusUpdateInformation
import com.aferrari.trailead.common.common_enum.toStatusUpdateInformation
import com.aferrari.trailead.common.email.MailAPI
import com.aferrari.trailead.domain.models.Category
import com.aferrari.trailead.domain.models.Material
import com.aferrari.trailead.domain.models.Trainee
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListTraineeViewModel(private val leaderViewModel: LeaderViewModel) : ViewModel() {

    val linkMaterialTraineeState = MutableLiveData<StatusUpdateInformation>()
    val statusUpdateRadioButtonSelectedAll = MutableLiveData<StatusUpdateInformation>()

    val setCategorySelected = MutableLiveData<MutableSet<Category>>()

    val allCategorySelectedSize = MutableLiveData<Int>()

    lateinit var traineeSelected: Trainee

    init {
        linkMaterialTraineeState.value = StatusUpdateInformation.NONE
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
            setCategorySelected.value?.let {
                linkMaterialTraineeState.value =
                    leaderViewModel.materialRepository.setLinkedCategory(
                        traineeSelected.userId,
                        it
                    ).toStatusUpdateInformation()
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

    fun notifyTrainee() {
        viewModelScope.launch(Dispatchers.IO) {
            MailAPI(
                traineeSelected.email,
                TITLE_MESSAGE,
                MESSAGE,
            ).sendMessage()
            leaderViewModel.repository.getTokenByUser(traineeSelected.userId).collect { toToken ->
                leaderViewModel.repository.getTokenByUser(leaderViewModel.getLeaderId())
                    .collect { fromToken ->
                        leaderViewModel.repository.sendNotify(
                            fromToken,
                            toToken,
                            TITLE_MESSAGE,
                            MESSAGE
                        )
                    }
            }
        }
    }

    private companion object {
        const val MESSAGE = "Se ha actualizado tu contenido. ¡Es hora de entrenar!"
        const val TITLE_MESSAGE = "TraiLead"
    }
}