package com.aferrari.trailead.app.viewmodel.trainee

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aferrari.trailead.common.common_enum.StatusUpdateInformation
import com.aferrari.trailead.common.common_enum.toStatusUpdateInformation
import com.aferrari.trailead.domain.models.Leader
import com.aferrari.trailead.domain.models.Trainee
import com.aferrari.trailead.domain.repository.MaterialRepository
import com.aferrari.trailead.domain.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TraineeViewModel(
    val repository: UserRepository,
    val materialRepository: MaterialRepository,
    private val savedStateHandle: SavedStateHandle
) :
    ViewModel() {

    private lateinit var trainee: Trainee

    private val traineeLeader = MutableLiveData<Leader>()
    val statusEditProfilePass = MutableLiveData(StatusUpdateInformation.NONE)

    val traineeName = MutableLiveData<String>()
    val traineeLastName = MutableLiveData<String>()
    val traineeCompleteName = MutableLiveData<String>()
    val traineeEmail = MutableLiveData<String>()
    val traineePosition = MutableLiveData<String>()
    val traineePassword = MutableLiveData<String>()
    val traineeAssignedLeader = MutableLiveData<String>()

    val refresh = MutableLiveData<Boolean>()

    // TODO: Remove comment lines
    fun setTrainee(trainee: Trainee?) {
        trainee?.let {
            this.trainee = trainee
            traineeName.value = trainee.name
            traineeLastName.value = trainee.lastName
            traineeEmail.value = trainee.email
            traineePosition.value = trainee.position.name
//            traineePassword.value = trainee.pass
            traineeCompleteName.value = trainee.name
            getLeaderFromTrainee(trainee.leaderId)
        }
    }

    private fun getLeaderFromTrainee(leaderId: String?) {
        viewModelScope.launch {
            if (leaderId != null) {
                repository.getUser(leaderId)
                    .collect {
                        traineeLeader.value = it as? Leader
                    }
            }
            traineeAssignedLeader.value = getAssignedLeader()
        }
    }

    private fun getAssignedLeader(): String = if (traineeLeader.value != null) {
        traineeLeader.value?.name.toString()
    } else {
        EMPTY_LEADER
    }

    fun updateInformation(name: String, lastName: String) {
        viewModelScope.launch {
            if (name.isNotEmpty()) {
                repository.updateTraineeName(trainee.userId, name)
                traineeName.value = name
            }
            if (lastName.isNotEmpty()) {
                repository.updateTraineeLastName(trainee.userId, lastName)
                traineeLastName.value = lastName
            }
            traineeCompleteName.value = traineeName.value + SPACE_STRING + traineeLastName.value
        }
    }

    fun updatePassword(password: String, repeatPassword: String) {
        if (password.isEmpty() or repeatPassword.isEmpty() or (password != repeatPassword) or (password.length < 6) or (repeatPassword.length < 6)) {
            statusEditProfilePass.value = StatusUpdateInformation.FAILED
            return
        }
        viewModelScope.launch {
            statusEditProfilePass.value =
                repository.updateUserPass(password).value.toStatusUpdateInformation()
        }
    }

    fun getTraineeSelected() = trainee

    fun init() {
        statusEditProfilePass.value = StatusUpdateInformation.NONE
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
            setTrainee(repository.getTrainee(traineeId = trainee.userId))
        }
    }

    companion object {
        private const val EMPTY_LEADER = "No tiene líder asignado"
        private const val SPACE_STRING = " "
    }
}
