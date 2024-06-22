package com.aferrari.trailead.app.viewmodel.trainee.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aferrari.trailead.app.viewmodel.IMaterial
import com.aferrari.trailead.app.viewmodel.trainee.TraineeViewModel
import com.aferrari.trailead.common.common_enum.StatusUpdateInformation
import com.aferrari.trailead.domain.models.Category
import com.aferrari.trailead.domain.models.Material
import com.aferrari.trailead.domain.models.Pdf
import com.aferrari.trailead.domain.models.Trainee
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HomeTraineeViewModel(val viewModel: TraineeViewModel) : ViewModel(), IMaterial {

    val setCategories = MutableLiveData<MutableSet<Category>>()
    val materialsByCategory = MutableLiveData<MutableList<Material>>()
    private var categorySelected: Category? = null

    private var traineeSelected: Trainee = viewModel.getTraineeSelected()

    val statusUpdatePdf = MutableLiveData<StatusUpdateInformation>()

    var pdfFileSelected: File? = null

    init {
        statusUpdatePdf.value = StatusUpdateInformation.NONE
    }

    fun getTraineeName() = viewModel.traineeName.value

    /**
     * Get Categories selected for trainee
     */
    fun getCategories() {
        if (hasLeader()) {
            viewModelScope.launch {
                setCategories.value =
                    viewModel.materialRepository.getCategoriesSelected(traineeSelected.userId)
                        .toMutableSet()
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
                val materialsByCategoryJoin = mutableListOf<Material>()
                materialsByCategoryJoin.addAll(getAllYouTubeVideoByCategory())
                materialsByCategoryJoin.addAll(getAllLinkByTrainee())
                materialsByCategoryJoin.addAll(getAllPdfByTrainee())
                materialsByCategory.value = materialsByCategoryJoin
            }
        }
    }

    private suspend fun getAllPdfByTrainee(): List<Pdf> =
        viewModel.materialRepository.getPdfByCategory(
            traineeSelected.leaderId!!,
            categorySelected!!.id
        ).first()

    private suspend fun getAllYouTubeVideoByCategory() =
        viewModel.materialRepository.getYoutubeVideoByCategory(
            traineeSelected.leaderId!!,
            categorySelected!!.id
        )

    private suspend fun getAllLinkByTrainee() = viewModel.materialRepository.getLinksByCategory(
        traineeSelected.leaderId!!,
        categorySelected!!.id
    )

    /**
     * Validate if has category selected
     */
    private fun hasCategorySelected() = categorySelected != null

    /**
     * Validate if has a leader
     */
    private fun hasLeader(): Boolean = traineeSelected.leaderId != null

    override fun setSelectedMaterial(material: Material) {
        TODO("Not yet implemented")
    }

    override fun deleteMaterialSelected() {
        TODO("Not yet implemented")
    }

    fun getRefresh() = viewModel.refresh
    fun restorePdf(pdf: Pdf?) {
        statusUpdatePdf.value = StatusUpdateInformation.LOADING
        if (pdf == null) {
            statusUpdatePdf.value = StatusUpdateInformation.FAILED
            return
        }

        if (!hasCategorySelected()) {
            statusUpdatePdf.value = StatusUpdateInformation.FAILED
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            viewModel.materialRepository.getPdf(pdf).collect {
                if (it != null) {
                    pdfFileSelected = it
                    statusUpdatePdf.postValue(StatusUpdateInformation.SUCCESS)
                } else {
                    statusUpdatePdf.postValue(StatusUpdateInformation.FAILED)
                }
            }
        }
    }

    fun setStateRefreshingScreen(isEnabled: Boolean) {
        viewModel.isRefreshingEnabled.value = isEnabled
    }

}
