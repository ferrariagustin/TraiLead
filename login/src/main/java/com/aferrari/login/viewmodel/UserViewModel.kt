package com.aferrari.login.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.aferrari.login.db.User
import com.aferrari.login.db.UserRepository
import com.aferrari.login.repository.DataSource
import com.aferrari.login.session.SessionManagement

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    private lateinit var login: LiveData<User>
    private lateinit var userViewModel: User

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