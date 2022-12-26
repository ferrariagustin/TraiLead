package com.aferrari.trailead.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aferrari.login.db.*
import kotlinx.coroutines.launch

class HomeLeaderViewModel(private val repository: UserRepository) : ViewModel() {
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

    val listMaterialCategory = MutableLiveData<List<Category>>()

    //    Material Category
    private val listCategoryList =
        arrayListOf(Category(1, "Session 1"), Category(1, "Session 2"))
    val statusUpdateNewCategory = MutableLiveData<StatusUpdateInformation>()
    val statusUpdateEditCategory = MutableLiveData<StatusUpdateInformation>()

    // Use with premium mode
    val listunlinkedTrainees = MutableLiveData<List<Trainee>>()

    fun init() {
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

    fun getAllTrainee() {
        viewModelScope.launch {
            listAllTrainee.value = repository.getAllTrainee()
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
        updatepassword(leader.id, pass)
    }

    private fun updatepassword(leaderId: Int, pass: String) {
        viewModelScope.launch {
            repository.updateLeaderPass(leaderId, pass)
            setLeader(repository.get(leaderId) as Leader)
            statusUpdatePassword.value = StatusUpdateInformation.SUCCESS
        }
    }

    fun getMaterialCategory() {
        viewModelScope.launch {
            listMaterialCategory.value = listCategoryList
        }
    }

    fun addCategory(nameCategory: String) {
        val newCategory = Category(3, nameCategory)
        if (newCategory.name.isEmpty()) {
            statusUpdateNewCategory.value = StatusUpdateInformation.FAILED
            return
        }
        listCategoryList.add(newCategory)
        getMaterialCategory()
        statusUpdateNewCategory.value = StatusUpdateInformation.SUCCESS
    }

    fun removeCategory() {
        categorySelected?.let {
            listCategoryList.remove(categorySelected)
            listMaterialCategory.value = listCategoryList
        }
    }

    fun editCategory(newCategory: String) {
        if (newCategory.isNullOrEmpty()) {
            statusUpdateEditCategory.value = StatusUpdateInformation.FAILED
            return
        }
        listCategoryList.forEach {
            if (it == categorySelected) {
                it.name = newCategory
            }
        }
        statusUpdateEditCategory.value = StatusUpdateInformation.SUCCESS
    }

}