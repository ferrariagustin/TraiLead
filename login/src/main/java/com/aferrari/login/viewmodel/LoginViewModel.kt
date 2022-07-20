package com.aferrari.login.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.aferrari.login.db.User
import com.aferrari.login.db.UserRepository
import com.aferrari.login.repository.DataSource
import com.aferrari.login.session.SessionManagement
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private lateinit var login: LiveData<User>

    lateinit var user: User
        private set

    val users = repository.users

    val inputName = MutableLiveData<String>()

    val inputPass = MutableLiveData<String>()

    var stateLogin = MutableLiveData<StateLogin>()

    init {
        stateLogin.value = StateLogin.STARTED
    }

    fun login() {
        if (validateInput()) return
        getUser(inputName.value!!, inputPass.value!!)
    }

    private fun validateInput(): Boolean {
        stateLogin.value = StateLogin.IN_PROGRESS
        if (inputName.value.isNullOrEmpty()) {
            // TODO: handle case
            Log.e("TRAILEAD", "Debe agregar ingresar un nombre")
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

    private fun getUser(inputName: String, inputPass: String) {
        viewModelScope.launch {
            when (val user = repository.get(inputName, inputPass)) {
                null -> failedLogin()
                else -> goLogin(user)
            }
        }
    }

    private fun insertUser(user: User) {
        viewModelScope.launch {
            val result = repository.insert(user)
            Log.e("TRAILEAD", "Insert result = $result")
            login()
        }
    }

    fun register() {
        if (validateInput()) return
        val user = User(
            0,
            name = inputName.value!!,
            pass = inputPass.value!!,
            email = "test_email"
        )
        insertUser(user)
    }

    private fun failedLogin() {
        stateLogin.value = StateLogin.FAILED
    }

    private fun goLogin(user: User) {
        this.user = user
        stateLogin.value = StateLogin.SUCCESS
    }

    fun loginUser(user: String, pass: String) {
        login = validateUser(user, pass)
//        when (response) {
//            LoginResult.SUCCESS -> loginLiveData.value = response.isSuccess
//            LoginResult.FAILED -> loginLiveData.value = null
//        }
    }

    fun getUserLogin(): LiveData<User> = login

    /**
     * Validate if exist user for user-pass
     */
    private fun validateUser(user: String, pass: String): LiveData<User> = liveData {
        this@LoginViewModel.user = DataSource().login(user, pass)
        emit(this@LoginViewModel.user)
    }

    /**
     * return User for userId
     */
    fun getUser(userId: Int, context: Context): User? = SessionManagement(context).getUser(userId)

}