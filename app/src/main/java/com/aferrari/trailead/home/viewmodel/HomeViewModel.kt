package com.aferrari.trailead.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aferrari.login.db.User
import com.aferrari.login.db.UserRepository
import com.aferrari.login.db.UserType
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: UserRepository) : ViewModel() {
    private val users = repository.users
    var userLogged: User? = null
    var homeState = MutableLiveData<HomeState>()

    fun getUser(userId: Int) {
        viewModelScope.launch {
            userLogged = repository.get(userId)
            when (userLogged?.userType) {
                UserType.LEADER -> homeState.value = HomeState.LEADER
                UserType.TRAINEE -> homeState.value = HomeState.TRAINEE
                else -> homeState.value = HomeState.NONE
            }
        }
    }

}
