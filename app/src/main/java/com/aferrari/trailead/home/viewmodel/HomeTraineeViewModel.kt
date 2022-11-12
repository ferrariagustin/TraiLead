package com.aferrari.trailead.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aferrari.login.db.Leader
import com.aferrari.login.db.Trainee
import com.aferrari.login.db.UserRepository
import kotlinx.coroutines.launch

class HomeTraineeViewModel(private val repository: UserRepository) : ViewModel() {

    private lateinit var trainee: Trainee

    private val traineeLeader = MutableLiveData<Leader>()
    val traineeName = MutableLiveData<String>()
    val traineeLastName = MutableLiveData<String>()
    val traineeCompleteName = MutableLiveData<String>()
    val traineeEmail = MutableLiveData<String>()
    val traineePosition = MutableLiveData<String>()
    val traineeAssignedLeader = MutableLiveData<String>()

    fun setTrainee(trainee: Trainee?) {
        trainee?.let {
            this.trainee = trainee
            traineeName.value = trainee.name
            traineeLastName.value = trainee.lastName
            traineeEmail.value = trainee.email
            traineePosition.value = trainee.position.name
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

    companion object {
        private const val EMPTY_LEADER = "No tiene líder asignado"
    }
}
