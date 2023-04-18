package com.aferrari.trailead.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aferrari.login.data.User
import com.aferrari.login.data.UserRepository
import com.aferrari.login.data.UserType
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: UserRepository) : ViewModel() {
    var user: User? = null
    var homeState = MutableLiveData<HomeState>()

    fun getUser(user: User) {
        this.user = user
        viewModelScope.launch {
            when (user.userType) {
                UserType.LEADER -> homeState.value = HomeState.LEADER
                UserType.TRAINEE -> homeState.value = HomeState.TRAINEE
                else -> homeState.value = HomeState.ERROR
            }
        }
    }

}
