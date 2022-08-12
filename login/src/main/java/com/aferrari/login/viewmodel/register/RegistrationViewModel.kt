package com.aferrari.login.viewmodel.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aferrari.login.db.User
import com.aferrari.login.db.UserRepository

class RegistrationViewModel(private val repository: UserRepository) : ViewModel() {
    lateinit var user: User
        private set

    val users = repository.users

    var registerState = MutableLiveData<RegisterState>()

    init {
        registerState.value = RegisterState.STARTED
    }

    fun cancel() {
        cleanView()
        goLogin()
    }

    private fun goLogin() {
        registerState.value = RegisterState.CANCEL
    }

    private fun cleanView() {
        // TODO: Clean all components on register view
    }
}