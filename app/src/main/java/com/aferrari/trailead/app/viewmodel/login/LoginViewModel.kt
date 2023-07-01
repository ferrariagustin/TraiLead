package com.aferrari.trailead.app.viewmodel.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aferrari.trailead.common.common_enum.LoginState
import com.aferrari.trailead.domain.models.User
import com.aferrari.trailead.domain.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    lateinit var user: User
        private set

    val inputEmail = MutableLiveData<String>()

    val inputPass = MutableLiveData<String>()

    var loginState = MutableLiveData<LoginState>()

    init {
        loginState.value = LoginState.STARTED
    }

    fun login() {
        if (validateInput()) return
        getUser(inputEmail.value!!, inputPass.value!!)
    }

    private fun validateInput(): Boolean {
        loginState.value = LoginState.IN_PROGRESS
        if (inputEmail.value.isNullOrEmpty()) {
            // TODO: handle case
            Log.e("TRAILEAD", "Debe agregar ingresar un email")
            failedLogin()
            return true
        }
        if (inputPass.value.isNullOrEmpty()) {
            // TODO: handle case
            failedLogin()
            Log.e("TRAILEAD", "Debe agregar ingresar un password")
            return true
        }
        return false
    }

    private fun getUser(inputEmail: String, inputPass: String) {
        viewModelScope.launch {
            when (val user = repository.getUser(inputEmail, inputPass)) {
                null -> failedLogin()
                else -> goLogin(user)
            }
        }
    }

    fun goRegister() {
        loginState.value = LoginState.REGISTER
    }

    private fun failedLogin() {
        loginState.value = LoginState.FAILED
    }

    private fun goLogin(user: User) {
        this.user = user
        loginState.value = LoginState.SUCCESS
    }

    /**
     * return User for userId
     */
    fun getUser(userId: Int) {
        viewModelScope.launch {
            repository.getUser(userId)
                .collect {
                    when (it) {
                        null -> failedLogin()
                        else -> goLogin(it)
                    }
                }
        }
    }

}