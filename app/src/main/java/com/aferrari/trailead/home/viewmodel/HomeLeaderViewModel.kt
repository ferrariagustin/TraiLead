package com.aferrari.trailead.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aferrari.login.db.Leader
import com.aferrari.login.db.Position
import com.aferrari.login.db.Trainee
import com.aferrari.login.db.UserRepository
import kotlinx.coroutines.launch

class HomeLeaderViewModel(private val repository: UserRepository) : ViewModel() {
    private lateinit var leader: Leader

    val nameUser = MutableLiveData<String>()

    val lastNameUser = MutableLiveData<String>()

    val emailUser = MutableLiveData<String>()

    val listAllTrainee = MutableLiveData<List<Trainee>>()

    val listLinkedTrainees = MutableLiveData<List<Trainee>>()

    val statusUpdateTraineeRol = MutableLiveData(StatusUpdateInformation.NONE)

    var traineeSelected: Trainee? = null

    var radioRolCheck: Position? = null


    // Use with premium mode
    val listunlinkedTrainees = MutableLiveData<List<Trainee>>()

    fun setLeader(leader: Leader?) {
        leader?.let {
            this.leader = it
            nameUser.value = leader.name
            lastNameUser.value = leader.lastName
            emailUser.value = leader.email
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
            updateList()
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
            updateList()
        }
    }

    private fun updateList() {
        getUnLinkedTrainees()
        getLinkedTrainees()
    }

    fun updateTraineeRol() {
        traineeSelected?.let { trainee ->
            radioRolCheck?.let { position ->
                viewModelScope.launch {
                    repository.updateTraineePosition(trainee, position)
                    statusUpdateTraineeRol.value = StatusUpdateInformation.SUCCESS
                }
                return
            }
        }
        statusUpdateTraineeRol.value = StatusUpdateInformation.FAILED
    }

}