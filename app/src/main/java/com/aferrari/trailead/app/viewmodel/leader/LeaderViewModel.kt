package com.aferrari.trailead.app.viewmodel.leader

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aferrari.trailead.app.viewmodel.IMaterial
import com.aferrari.trailead.common.IntegerUtils
import com.aferrari.trailead.common.UrlUtils
import com.aferrari.trailead.common.common_enum.Position
import com.aferrari.trailead.common.common_enum.StatusCode
import com.aferrari.trailead.common.common_enum.StatusUpdateInformation
import com.aferrari.trailead.common.common_enum.toStatusUpdateInformation
import com.aferrari.trailead.domain.models.Category
import com.aferrari.trailead.domain.models.Leader
import com.aferrari.trailead.domain.models.Link
import com.aferrari.trailead.domain.models.Material
import com.aferrari.trailead.domain.models.Trainee
import com.aferrari.trailead.domain.models.YouTubeVideo
import com.aferrari.trailead.domain.repository.MaterialRepository
import com.aferrari.trailead.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

open class LeaderViewModel(
    val repository: UserRepository,
    val materialRepository: MaterialRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), IMaterial {
    private lateinit var leader: Leader
    val nameUser = MutableLiveData<String>()

    val lastNameUser = MutableLiveData<String>()

    val emailUser = MutableLiveData<String>()

    val passUser = MutableLiveData<String>()

    private val listAllTrainee = MutableLiveData<List<Trainee>>()

    val listLinkedTrainees = MutableLiveData<List<Trainee>>()

    val statusUpdateTraineeRol = MutableLiveData<StatusUpdateInformation>()

    val statusUpdatePassword = MutableLiveData<StatusUpdateInformation>()

    var traineeSelected: Trainee? = null

    var radioRolCheck: Position? = null

    var categorySelected: Category? = null

    var materialSelected: Material? = null

    val refresh = MutableLiveData<Boolean>()

    //    Category
    val listCategory = MutableLiveData<List<Category>>()

    val statusUpdateNewCategory = MutableLiveData<StatusUpdateInformation>()
    val statusUpdateEditCategory = MutableLiveData<StatusUpdateInformation>()
    val statusUpdateDeleteCategory = MutableLiveData<StatusUpdateInformation>()

    //    Material
    val statusUpdateYoutubeVideo = MutableLiveData<StatusUpdateInformation>()
    val statusUpdateDeleteMaterial = MutableLiveData<StatusUpdateInformation>()
    var listAllMaterials = MutableLiveData<List<Material>>()

    // Use with premium mode
    val listunlinkedTrainees = MutableLiveData<List<Trainee>>()

    val bottomNavigationViewVisibility = MutableLiveData(View.VISIBLE)

    fun init() {
        refresh.value = false
        bottomNavigationViewVisibility.value = View.VISIBLE
        statusUpdateDeleteCategory.value = StatusUpdateInformation.NONE
        statusUpdateYoutubeVideo.value = StatusUpdateInformation.NONE
        statusUpdateNewCategory.value = StatusUpdateInformation.NONE
        statusUpdateEditCategory.value = StatusUpdateInformation.NONE
        statusUpdateTraineeRol.value = StatusUpdateInformation.NONE
        statusUpdatePassword.value = StatusUpdateInformation.NONE
    }

    fun setLeader(leader: Leader?) {
        leader?.let {
            this.leader = it
            nameUser.value = leader.name
            lastNameUser.value = leader.lastName
            emailUser.value = leader.email
        }
    }

    override fun onCleared() {
        saveState()
        super.onCleared()
    }

    private fun saveState() {
        // TODO: review if is neccesary
//        savedStateHandle[CATEGORY_SELECTED] = categorySelected
//        savedStateHandle[TRAINEE_SELECTED] = traineeSelected
//        savedStateHandle[MATERIAL_SELECTED] = materialSelected
    }

    fun restoreState() {
        // TODO: review if is neccesary
//        categorySelected = savedStateHandle[CATEGORY_SELECTED]
//        traineeSelected = savedStateHandle[TRAINEE_SELECTED]
//        materialSelected = savedStateHandle[MATERIAL_SELECTED]
    }

    fun getLeaderId() = leader.userId

    fun getUnLinkedTrainees() {
        viewModelScope.launch {
            listunlinkedTrainees.value = repository.getUnlinkedTrainees()
        }
    }

    fun setLinkedTrainee(trainee: Trainee) {
        viewModelScope.launch {
            repository.setLinkedTrainee(trainee, leader)
            updateTraineeList()
        }
    }

    /**
     * return trainee list of leader session now
     */
    fun getLinkedTrainees() {
        viewModelScope.launch {
            listLinkedTrainees.value = repository.getLinkedTrainees(leader)
        }
    }

    /**
     * Removed linked leaderId with trainee
     */
    fun setUnlinkedTrainee(trainee: Trainee) {
        viewModelScope.launch {
            repository.setUnlinkedTrainee(trainee)
            updateTraineeList()
        }
    }

    /**
     * Removed linked leaderId with trainee
     */
    fun setUnlinkedTrainee() {
        traineeSelected?.let { trainee ->
            viewModelScope.launch {
                repository.setUnlinkedTrainee(trainee)
                updateTraineeList()
            }
        }
    }

    private fun updateTraineeList() {
        getUnLinkedTrainees()
        getLinkedTrainees()
    }

    /**
     * The selected trainee rol is updated
     */
    fun updateTraineeRol() {
        traineeSelected?.let { trainee ->
            radioRolCheck?.let { position ->
                viewModelScope.launch {
                    repository.updateTraineePosition(trainee, position)
                    repository.getUser(trainee.userId)
                        .flowOn(Dispatchers.IO).collect {
                            traineeSelected = it as Trainee
                        }
                    statusUpdateTraineeRol.value = StatusUpdateInformation.SUCCESS
                }
                return
            }
        }
        statusUpdateTraineeRol.value = StatusUpdateInformation.FAILED
    }

    fun removeTraineeSelected() {
        traineeSelected?.let { trainee ->
            viewModelScope.launch {
                repository.deleteTrainee(trainee)
            }
        }
    }

    fun updateInformation(name: String, lastName: String) {
        viewModelScope.launch {
            if (name.isNotEmpty()) {
                repository.updateLeaderName(leader.userId, name)
                nameUser.value = name
            }
            if (lastName.isNotEmpty()) {
                repository.updateLeaderLastName(leader.userId, lastName)
                lastNameUser.value = lastName
            }
        }
    }

    fun updatePassword(pass: String, repeatPass: String) {
        if (pass.isNullOrEmpty() || repeatPass.isNullOrEmpty() || pass != repeatPass) {
            statusUpdatePassword.value = StatusUpdateInformation.FAILED
            return
        }
        setPassword(pass)
    }

    private fun setPassword(pass: String) {
        viewModelScope.launch {
            repository.updateUserPass(pass).apply {
                when (this) {
                    StatusCode.SUCCESS -> {
                        statusUpdatePassword.value = StatusUpdateInformation.SUCCESS
                    }

                    StatusCode.ERROR -> {
                        statusUpdatePassword.value = StatusUpdateInformation.FAILED
                    }

                    else -> {
                        statusUpdatePassword.value = StatusUpdateInformation.FAILED
                    }
                }
            }
        }
    }

    fun getAllCategoryForLeader() {
        viewModelScope.launch {
            listCategory.value = materialRepository.getAllCategory(leader)
        }
    }

    fun insertCategory(nameCategory: String) {
        if (nameCategory.isEmpty()) {
            statusUpdateNewCategory.value = StatusUpdateInformation.FAILED
            return
        }
        if (getCategoryBy(nameCategory) != null) {
            statusUpdateNewCategory.value = StatusUpdateInformation.FAILED
            return
        }
        val category = Category(IntegerUtils().createObjectId(), nameCategory, leader.userId)
        insertCategory(category)
    }

    private fun insertCategory(category: Category) {
        viewModelScope.launch {
            materialRepository.insertCategory(category).flowOn(Dispatchers.IO).collect { state ->
                when (state.value.toStatusUpdateInformation()) {
                    StatusUpdateInformation.SUCCESS -> {
                        getAllCategoryForLeader()
                        statusUpdateNewCategory.value = StatusUpdateInformation.SUCCESS
                    }

                    StatusUpdateInformation.FAILED -> {
                        statusUpdateNewCategory.value = StatusUpdateInformation.FAILED
                    }

                    StatusUpdateInformation.INTERNET_CONECTION -> {
                        statusUpdateNewCategory.value = StatusUpdateInformation.INTERNET_CONECTION
                    }

                    else -> {
                        statusUpdateNewCategory.value = StatusUpdateInformation.FAILED
                    }
                }
            }
        }
    }

    fun removeCategory() {
        viewModelScope.launch {
            categorySelected?.let {
                deleteCategory(it.id)
            }
        }
    }

    private suspend fun deleteCategory(idCategory: Int) {
        when (materialRepository.deleteCategory(idCategory).toStatusUpdateInformation()) {
            StatusUpdateInformation.SUCCESS -> {
                getAllCategoryForLeader()
                statusUpdateDeleteCategory.value = StatusUpdateInformation.SUCCESS
            }

            StatusUpdateInformation.FAILED -> {
                statusUpdateDeleteCategory.value = StatusUpdateInformation.FAILED
            }

            StatusUpdateInformation.INTERNET_CONECTION -> {
                statusUpdateDeleteCategory.value = StatusUpdateInformation.INTERNET_CONECTION
            }

            else -> {
                statusUpdateDeleteCategory.value = StatusUpdateInformation.FAILED
            }
        }
    }

    fun editCategory(newCategory: String) = viewModelScope.launch {
        if (newCategory.isNullOrEmpty() || categorySelected == null) {
            statusUpdateEditCategory.value = StatusUpdateInformation.FAILED
            return@launch
        }
        updateCategory(newCategory)
    }


    private suspend fun updateCategory(newCategory: String) {
        statusUpdateEditCategory.value =
            materialRepository.updateCategory(categorySelected!!.id, newCategory)
                .toStatusUpdateInformation()
    }

    /**
     * You can gone or visibility the bottomNavigation
     */
    fun setBottomNavigationVisibility(newVisibility: Int) {
        bottomNavigationViewVisibility.value = when (newVisibility) {
            View.VISIBLE -> {
                View.VISIBLE
            }

            View.INVISIBLE -> {
                View.INVISIBLE
            }

            View.GONE -> {
                View.GONE
            }

            else -> {
                View.VISIBLE
            }
        }
    }

    fun insertYoutubeVideo(title: String, url: String) {
        if (!UrlUtils().isYoutubeUrl(url)) {
            statusUpdateYoutubeVideo.value = StatusUpdateInformation.FAILED
            return
        }
        val youtubeId: String? = UrlUtils().getYouTubeId(url)
        if (youtubeId.isNullOrEmpty()) {
            statusUpdateYoutubeVideo.value = StatusUpdateInformation.FAILED
            return
        }
        if (categorySelected == null) {
            statusUpdateYoutubeVideo.value = StatusUpdateInformation.FAILED
            return
        }
        addNewYoutubeMaterial(title, youtubeId)
    }

    private fun addNewYoutubeMaterial(title: String, youtubeId: String) {
        val newYouTubeVideo = YouTubeVideo(
            id = IntegerUtils().createObjectId(),
            title = title,
            url = youtubeId,
            categoryId = categorySelected?.id,
            leaderMaterialId = leader.userId
        )
        insertYoutubeVideo(newYouTubeVideo)
    }

    fun updateMaterialsCategoryFilter() = viewModelScope.launch {
        val listAllMaterlialJoin = mutableListOf<Material>()
        listAllMaterlialJoin.addAll(getAllYouTubeVideosCategoryFilter())
        listAllMaterlialJoin.addAll(getAllLinkCategoryFilter())
        listAllMaterials.value = listAllMaterlialJoin
    }

    private suspend fun getAllYouTubeVideosCategoryFilter() =
        materialRepository.getAllYoutubeVideo(leader)
            .filter { it.categoryId == categorySelected?.id }

    private suspend fun getAllLinkCategoryFilter() = materialRepository.getAllLink(leader)
        .filter { it.categoryId == categorySelected?.id }


    fun getAllMaterials() = viewModelScope.launch {
        listAllMaterials.value = materialRepository.getAllYoutubeVideo(leader)
    }

    private fun getAllYoutubeVideo(newYouTubeVideo: YouTubeVideo) {
        viewModelScope.launch {
            val tempListMaterial = materialRepository.getAllYoutubeVideo(leader)
            if (tempListMaterial.contains(newYouTubeVideo)) {
                listAllMaterials.value = tempListMaterial.toMutableList()
                statusUpdateYoutubeVideo.value = StatusUpdateInformation.SUCCESS
            } else {
                statusUpdateYoutubeVideo.value = StatusUpdateInformation.FAILED
            }
        }
    }

    /**
     * Comunicate with dataSource for insert new material to DB
     */
    private fun insertYoutubeVideo(newYouTubeVideo: YouTubeVideo) {
        viewModelScope.launch {
            when (materialRepository.insertYoutubeVideo(newYouTubeVideo)) {
                StatusCode.SUCCESS.value -> {
                    getAllYoutubeVideo(newYouTubeVideo)
                }

                StatusCode.ERROR.value -> {
                    statusUpdateYoutubeVideo.value = StatusUpdateInformation.FAILED
                }

                StatusCode.INTERNET_CONECTION.value -> {
                    statusUpdateYoutubeVideo.value = StatusUpdateInformation.INTERNET_CONECTION
                }

                else -> {
                    statusUpdateYoutubeVideo.value = StatusUpdateInformation.FAILED
                }
            }
        }
    }

    override fun deleteMaterialSelected() {
        viewModelScope.launch {
            materialSelected?.let {
                val result = when (it) {
                    is YouTubeVideo -> {
                        materialRepository.deleteYoutubeVideo(it)
                    }

                    is Link -> {
                        materialRepository.deleteLink(it)
                    }

                    else -> {
                        StatusCode.SUCCESS.value
                    }
                }
                updateStatusDeleteMaterial(result)
            }
        }
    }

    private fun updateStatusDeleteMaterial(result: Long) {
        when (result) {
            StatusCode.SUCCESS.value -> {
                updateMaterialsCategoryFilter()
                statusUpdateDeleteMaterial.value = StatusUpdateInformation.SUCCESS
            }

            StatusCode.ERROR.value -> {
                statusUpdateDeleteMaterial.value = StatusUpdateInformation.FAILED
            }

            StatusCode.INTERNET_CONECTION.value -> {
                statusUpdateDeleteMaterial.value = StatusUpdateInformation.INTERNET_CONECTION
            }

            else -> {
                statusUpdateDeleteMaterial.value = StatusUpdateInformation.FAILED
            }
        }
    }

    /**
     * is selected Material clicked
     */
    override fun setSelectedMaterial(material: Material) {
        this.materialSelected = material
    }

    fun getCategoryBy(category: String): Category? {
        listCategory.value?.forEach {
            if (it.name == category) return it
        }
        return null
    }

    fun refresh() {
        viewModelScope.launch {
            refresh.value = true
            delay(3000)
            refresh.value = false
        }
    }

    fun updateProfile() {
        viewModelScope.launch {
            setLeader(repository.getLeader(leader.userId))
        }
    }

    private companion object {
        const val CATEGORY_SELECTED = "categorySelected"
        const val TRAINEE_SELECTED = "traineeSelected"
        const val MATERIAL_SELECTED = "materialSelected"
    }
}
