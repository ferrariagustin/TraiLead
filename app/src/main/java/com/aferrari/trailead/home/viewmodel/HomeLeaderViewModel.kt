package com.aferrari.trailead.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aferrari.login.db.Leader
import com.aferrari.login.db.UserRepository

class HomeLeaderViewModel(repository: UserRepository) : ViewModel() {
    private lateinit var leader: Leader

    fun getLeader(leader: Leader?) {
        leader?.let {
            this.leader = it
        }
    }

    fun initComponent() {
        nameUser.value = leader.name
        lastNameUser.value = leader.lastName
        emailUser.value = leader.email
    }

    val nameUser = MutableLiveData<String>()

    val lastNameUser = MutableLiveData<String>()

    val emailUser = MutableLiveData<String>()


}