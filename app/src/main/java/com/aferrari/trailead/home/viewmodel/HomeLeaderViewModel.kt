package com.aferrari.trailead.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aferrari.login.db.Leader
import com.aferrari.login.db.Trainee
import com.aferrari.login.db.UserRepository
import kotlinx.coroutines.launch

class HomeLeaderViewModel(private val repository: UserRepository) : ViewModel() {
    private lateinit var leader: Leader

    val nameUser = MutableLiveData<String>()

    val lastNameUser = MutableLiveData<String>()

    val emailUser = MutableLiveData<String>()

    val listAllTrainee = MutableLiveData<List<Trainee>>()

    fun initComponent() {
        nameUser.value = leader.name
        lastNameUser.value = leader.lastName
        emailUser.value = leader.email
    }

    fun setLeader(leader: Leader?) {
        leader?.let {
            this.leader = it
        }
    }

    fun getAllTrainee() {
        viewModelScope.launch {
            listAllTrainee.value = repository.getAllTrainee()
        }
    }

}