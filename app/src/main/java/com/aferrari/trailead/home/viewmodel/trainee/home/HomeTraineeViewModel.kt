package com.aferrari.trailead.home.viewmodel.trainee.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aferrari.login.data.Category
import com.aferrari.login.data.Trainee
import com.aferrari.trailead.home.viewmodel.trainee.TraineeViewModel
import kotlinx.coroutines.launch

class HomeTraineeViewModel(val viewModel: TraineeViewModel) : ViewModel() {

    val setCategories = MutableLiveData<MutableSet<Category>>()

    private var traineeSelected: Trainee = viewModel.getTraineeSelected()

    fun getTraineeName() = viewModel.traineeName.value

    /**
     * Get Categories selected for trainee
     */
    fun getCategories() {
        viewModelScope.launch {
            setCategories.value =
                viewModel.repository.getCategoriesSelected(traineeSelected.id).toMutableSet()
        }
    }

}