package com.aferrari.trailead.home.viewmodel.trainee

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aferrari.login.data.user.Leader
import com.aferrari.login.data.user.Trainee
import com.aferrari.login.data.user.repository.UserRepository
import com.aferrari.login.data.material.repository.MaterialRepository
import com.aferrari.trailead.home.viewmodel.StatusUpdateInformation
import com.aferrari.trailead.home.viewmodel.StatusVisibilityPassword
import kotlinx.coroutines.launch

class TraineeViewModel(val repository: UserRepository, val materialRepository: MaterialRepository) :
    ViewModel() {

    private lateinit var trainee: Trainee

    private val traineeLeader = MutableLiveData<Leader>()
    val statusEditProfilePass = MutableLiveData(StatusUpdateInformation.NONE)
    val statusVisibilityPassword = MutableLiveData(StatusVisibilityPassword.INVISIBLE)

    val traineeName = MutableLiveData<String>()
    val traineeLastName = MutableLiveData<String>()
    val traineeCompleteName = MutableLiveData<String>()
    val traineeEmail = MutableLiveData<String>()
    val traineePosition = MutableLiveData<String>()
    val traineePassword = MutableLiveData<String>()
    val traineeAssignedLeader = MutableLiveData<String>()

    fun setTrainee(trainee: Trainee?) {
        trainee?.let {
            this.trainee = trainee
            traineeName.value = trainee.name
            traineeLastName.value = trainee.lastName
            traineeEmail.value = trainee.email
            traineePosition.value = trainee.position.name
            traineePassword.value = trainee.pass
            traineeCompleteName.value = trainee.name + " " + trainee.lastName
            getLeaderFromTrainee(trainee.leaderId)
        }
    }

    private fun getLeaderFromTrainee(leaderId: Int?) {
        viewModelScope.launch {
            if (leaderId != null) {
                traineeLeader.value = repository.get(leaderId) as? Leader
            }
            traineeAssignedLeader.value = getAssignedLeader()
        }
    }

    private fun getAssignedLeader(): String = if (traineeLeader.value != null) {
        traineeLeader.value?.name + " " + traineeLeader.value?.lastName
    } else {
        EMPTY_LEADER
    }

    fun updateInformation(name: String, lastName: String) {
        viewModelScope.launch {
            if (name.isNotEmpty()) {
                repository.updateTraineeName(trainee.id, name)
                traineeName.value = name
            }
            if (lastName.isNotEmpty()) {
                repository.updateTraineeLastName(trainee.id, lastName)
                traineeLastName.value = lastName
            }
            traineeCompleteName.value = traineeName.value + SPACE_STRING + traineeLastName.value
        }
    }

    fun updatePassword(password: String, repeatPassword: String) {
        if (password.isEmpty() or repeatPassword.isEmpty() or (password != repeatPassword)) {
            statusEditProfilePass.value = StatusUpdateInformation.FAILED
            return
        }
        viewModelScope.launch {
            repository.updateTraineePassword(password, trainee.id)
            traineePassword.value = password
            statusEditProfilePass.value = StatusUpdateInformation.SUCCESS
        }
    }

    fun getTraineeSelected() = trainee

    fun init() {
        statusEditProfilePass.value = StatusUpdateInformation.NONE
        statusVisibilityPassword.value = StatusVisibilityPassword.INVISIBLE
    }

    companion object {
        private const val EMPTY_LEADER = "No tiene líder asignado"
        private const val SPACE_STRING = " "
    }
}
