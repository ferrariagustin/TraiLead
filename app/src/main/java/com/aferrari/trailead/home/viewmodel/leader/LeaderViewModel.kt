package com.aferrari.trailead.home.viewmodel.leader

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aferrari.login.data.material.Category
import com.aferrari.login.data.user.Leader
import com.aferrari.login.data.material.YouTubeVideo
import com.aferrari.login.data.user.Position
import com.aferrari.login.data.user.Trainee
import com.aferrari.login.data.user.repository.UserRepository
import com.aferrari.login.data.material.repository.MaterialRepository
import com.aferrari.login.utils.IntegerUtils
import com.aferrari.trailead.home.Utils.UrlUtils
import com.aferrari.trailead.home.viewmodel.IMaterial
import com.aferrari.trailead.home.viewmodel.StatusUpdateInformation
import com.aferrari.trailead.home.viewmodel.StatusVisibilityPassword
import kotlinx.coroutines.launch

open class LeaderViewModel(val repository: UserRepository, val materialRepository: MaterialRepository) : ViewModel(), IMaterial {
    private lateinit var leader: Leader

    val nameUser = MutableLiveData<String>()

    val lastNameUser = MutableLiveData<String>()

    val emailUser = MutableLiveData<String>()

    val passUser = MutableLiveData<String>()

    private val listAllTrainee = MutableLiveData<List<Trainee>>()

    val listLinkedTrainees = MutableLiveData<List<Trainee>>()

    val statusUpdateTraineeRol = MutableLiveData<StatusUpdateInformation>()

    val statusUpdatePassword = MutableLiveData<StatusUpdateInformation>()

    var statusVisibilityPassword = StatusVisibilityPassword.INVISIBLE

    var traineeSelected: Trainee? = null

    var radioRolCheck: Position? = null

    var categorySelected: Category? = null

    var youTubeVideoSelected: YouTubeVideo? = null

    //    Category
    val listCategory = MutableLiveData<List<Category>>()
    val statusUpdateNewCategory = MutableLiveData<StatusUpdateInformation>()
    val statusUpdateEditCategory = MutableLiveData<StatusUpdateInformation>()

    //    Material
    val statusUpdateNewMaterial = MutableLiveData<StatusUpdateInformation>()
    var listAllMaterials = MutableLiveData<List<YouTubeVideo>>()

    // Use with premium mode
    val listunlinkedTrainees = MutableLiveData<List<Trainee>>()

    val bottomNavigationViewVisibility = MutableLiveData(View.VISIBLE)

    fun init() {
        bottomNavigationViewVisibility.value = View.VISIBLE
        statusUpdateNewMaterial.value = StatusUpdateInformation.NONE
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
            passUser.value = leader.pass
        }
    }

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
                    traineeSelected = repository.get(trainee.id) as Trainee
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
                repository.updateLeaderName(leader.id, name)
                nameUser.value = name
            }
            if (lastName.isNotEmpty()) {
                repository.updateLeaderLastName(leader.id, lastName)
                lastNameUser.value = lastName
            }
        }
    }

    fun updatePassword(pass: String, repeatPass: String) {
        if (pass.isNullOrEmpty() || repeatPass.isNullOrEmpty() || pass != repeatPass) {
            statusUpdatePassword.value = StatusUpdateInformation.FAILED
            return
        }
        updatePassword(leader.id, pass)
    }

    private fun updatePassword(leaderId: Int, pass: String) {
        viewModelScope.launch {
            repository.updateLeaderPass(leaderId, pass)
            setLeader(repository.get(leaderId) as Leader)
            statusUpdatePassword.value = StatusUpdateInformation.SUCCESS
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
        val category = Category(IntegerUtils().getUserId(), nameCategory, leader.id)
        insertCategory(category)
    }

    private fun insertCategory(category: Category) {
        viewModelScope.launch {
            materialRepository.insertCategory(category)
            getAllCategoryForLeader()
            statusUpdateNewCategory.value = StatusUpdateInformation.SUCCESS
        }
    }

    fun removeCategory() {
        categorySelected?.let {
            deleteCategory(it)
        }
    }

    private fun deleteCategory(category: Category) {
        viewModelScope.launch {
            materialRepository.deleteCategory(category)
            getAllCategoryForLeader()
        }
    }

    fun editCategory(newCategory: String) {
        if (newCategory.isNullOrEmpty()) {
            statusUpdateEditCategory.value = StatusUpdateInformation.FAILED
            return
        }
        updateCategory(newCategory)
    }

    private fun updateCategory(newCategory: String) {
        viewModelScope.launch {
            categorySelected?.let {
                materialRepository.updateCategory(it.id, newCategory)
                statusUpdateEditCategory.value = StatusUpdateInformation.SUCCESS
            }
        }
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

    fun insertMaterial(title: String, url: String) {
        if (!UrlUtils().isYoutubeUrl(url)) {
            statusUpdateNewMaterial.value = StatusUpdateInformation.FAILED
            return
        }
        val youtubeId: String? = UrlUtils().getYouTubeId(url)
        if (youtubeId.isNullOrEmpty()) {
            statusUpdateNewMaterial.value = StatusUpdateInformation.FAILED
            return
        }
        if (categorySelected == null) {
            statusUpdateNewMaterial.value = StatusUpdateInformation.FAILED
            return
        }
        addNewYoutubeMaterial(title, youtubeId)
    }

    private fun addNewYoutubeMaterial(title: String, youtubeId: String) {
        val newYouTubeVideo = YouTubeVideo(
            id = IntegerUtils().getUserId(),
            title = title,
            url = youtubeId,
            categoryId = categorySelected?.id,
            leaderMaterialId = leader.id
        )
        addNewMaterial(newYouTubeVideo)
    }

    fun getMaterialsCategoryFilter() = viewModelScope.launch {
        listAllMaterials.value =
            materialRepository.getAllYoutubeVideo(leader).filter { it.categoryId == categorySelected?.id }
    }


    fun getAllMaterials() = viewModelScope.launch {
        listAllMaterials.value = materialRepository.getAllYoutubeVideo(leader)
    }

    private fun getAllMaterial(newYouTubeVideo: YouTubeVideo) {
        viewModelScope.launch {
            val tempListMaterial = materialRepository.getAllYoutubeVideo(leader)
            if (tempListMaterial.contains(newYouTubeVideo)) {
                listAllMaterials.value = tempListMaterial.toMutableList()
                statusUpdateNewMaterial.value = StatusUpdateInformation.SUCCESS
            } else {
                statusUpdateNewMaterial.value = StatusUpdateInformation.FAILED
            }
        }
    }

    /**
     * Comunicate with dataSource for insert new material to DB
     */
    private fun addNewMaterial(newYouTubeVideo: YouTubeVideo) {
        viewModelScope.launch {
            materialRepository.insertYoutubeVideo(newYouTubeVideo)
            statusUpdateNewMaterial.value = StatusUpdateInformation.SUCCESS
            return@launch
        }
    }

    override fun deleteMaterialSelected() {
        viewModelScope.launch {
            youTubeVideoSelected?.let {
                materialRepository.deleteYoutubeVideo(it)
                getMaterialsCategoryFilter()
            }
        }
    }

    /**
     * is selected Material clicked
     */
    override fun setSelectedMaterial(youTubeVideo: YouTubeVideo) {
        youTubeVideoSelected = youTubeVideo
    }

    fun getCategoryBy(category: String): Category? {
        listCategory.value?.forEach {
            if (it.name == category) return it
        }
        return null
    }
}
