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
    private lateinit var userViewModel: User

    val users = repository.users

    val inputName = MutableLiveData<String>()

    val inputPass = MutableLiveData<String>()

    private val stateLogin = MutableLiveData<StateLogin>()

    init {
        stateLogin.value = StateLogin.STARTED
    }

    fun login() {
        if (inputName.value.isNullOrEmpty()) {
            // TODO: handle case
            Log.e("TRAILEAD", "Debe agregar ingresar un nombre")
            return
        }
        if (inputPass.value.isNullOrEmpty()) {
            // TODO: handle case
            Log.e("TRAILEAD", "Debe agregar ingresar un password")
            return
        }
        getUser(inputName.value!!, inputPass.value!!)
    }

    private fun getUser(inputName: String, inputPass: String) {
        viewModelScope.launch {
            repository.get(inputName, inputPass)
        }
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
        userViewModel = DataSource().login(user, pass)
        emit(userViewModel)
    }

    /**
     * return User for userId
     */
    fun getUser(userId: Int, context: Context): User? = SessionManagement(context).getUser(userId)

}