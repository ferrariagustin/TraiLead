package com.aferrari.trailead.home.viewmodel.trainee.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aferrari.login.data.Category
import com.aferrari.login.data.material.YouTubeVideo
import com.aferrari.login.data.Trainee
import com.aferrari.trailead.home.viewmodel.IMaterial
import com.aferrari.trailead.home.viewmodel.trainee.TraineeViewModel
import kotlinx.coroutines.launch

class HomeTraineeViewModel(val viewModel: TraineeViewModel) : ViewModel(), IMaterial {

    val setCategories = MutableLiveData<MutableSet<Category>>()

    val materialsByCategory = MutableLiveData<MutableList<YouTubeVideo>>()

    private var categorySelected: Category? = null

    private var traineeSelected: Trainee = viewModel.getTraineeSelected()

    fun getTraineeName() = viewModel.traineeName.value

    /**
     * Get Categories selected for trainee
     */
    fun getCategories() {
        if (hasLeader()) {
            viewModelScope.launch {
                setCategories.value =
                    viewModel.repository.getCategoriesSelected(traineeSelected.id).toMutableSet()
            }
        } else {
            reset()
        }
    }

    private fun reset() {
        setCategories.value = mutableSetOf()
        materialsByCategory.value = mutableListOf()
    }

    fun setCategorySelected(category: Category) {
        categorySelected = category
    }

    fun getMaterials() {
        if (hasLeader() && hasCategorySelected()) {
            viewModelScope.launch {
                materialsByCategory.value = viewModel.repository.getMaterialByTrainee(
                    traineeSelected.leaderId!!,
                    categorySelected!!.id
                ).toMutableList()
            }
        }
    }

    /**
     * Validate if has category selected
     */
    private fun hasCategorySelected() = categorySelected != null

    /**
     * Validate if has a leader
     */
    private fun hasLeader(): Boolean = traineeSelected.leaderId != null

    override fun setSelectedMaterial(youTubeVideo: YouTubeVideo) {
        TODO("Not yet implemented")
    }

    override fun deleteMaterialSelected() {
        TODO("Not yet implemented")
    }

}