package com.aferrari.trailead.app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aferrari.trailead.common.UserState
import com.aferrari.trailead.common.common_enum.UserType
import com.aferrari.trailead.domain.models.User
import com.aferrari.trailead.domain.repository.UserRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: UserRepository) : ViewModel() {
    var user: User? = null
    var homeState = MutableLiveData<UserState>()

    fun getUser(user: User) {
        this.user = user
        viewModelScope.launch {
            when (user.userType) {
                UserType.LEADER -> homeState.value = UserState.LEADER
                UserType.TRAINEE -> homeState.value = UserState.TRAINEE
                else -> homeState.value = UserState.ERROR
            }
        }
    }

}
