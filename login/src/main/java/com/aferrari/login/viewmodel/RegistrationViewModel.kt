package com.aferrari.login.viewmodel

import androidx.lifecycle.ViewModel
import com.aferrari.login.db.User
import com.aferrari.login.db.UserRepository

class RegistrationViewModel(private val repository: UserRepository): ViewModel() {
    lateinit var user: User
        private set

    val users = repository.users
}