package com.aferrari.trailead.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aferrari.login.db.User
import com.aferrari.login.db.UserRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: UserRepository) : ViewModel() {
    private val users = repository.users
    private val userLogged: User? = null

    fun getUser(userId: Int) {
        viewModelScope.launch {
            val user = repository.get(userId)
            // Validate if user is Trainee or Leader
        }
    }

}
