package com.aferrari.trailead.app.viewmodel.login

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aferrari.trailead.common.IntegerUtils
import com.aferrari.trailead.common.StringUtils.isValidEmail
import com.aferrari.trailead.common.common_enum.LoginState
import com.aferrari.trailead.common.common_enum.StatusCode
import com.aferrari.trailead.common.common_enum.StatusUpdateInformation
import com.aferrari.trailead.common.email.GMailSender
import com.aferrari.trailead.domain.models.User
import com.aferrari.trailead.domain.repository.UserRepository
import kotlinx.coroutines.launch

open class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    lateinit var user: User
        private set

    val inputEmail = MutableLiveData<String>()

    val inputPass = MutableLiveData<String>()

    var loginState = MutableLiveData<LoginState>()

    var visibilityPassDrawable = MutableLiveData<Int>()

    var restorePasswordEmail = MutableLiveData<String>()

    var sendEmailStatus = MutableLiveData<StatusCode>()

    var restoreKeyAccess = MutableLiveData<StatusUpdateInformation>()

    init {
        sendEmailStatus.value = StatusCode.INIT
        loginState.value = LoginState.STARTED
        visibilityPassDrawable.value = View.GONE
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


    fun setDrawableVisibilityPass() {
        if (visibilityPassDrawable.value == View.GONE) {
            visibilityPassDrawable.value = View.VISIBLE
        } else {
            visibilityPassDrawable.value = View.GONE
        }
    }

    fun sendMail() {
        viewModelScope.launch {
            // ver como enviar mail
            try {
                if (!isValidEmail(restorePasswordEmail.value)) {
                    sendEmailStatus.value = StatusCode.ERROR
                    return@launch
                }
                val resultUser = repository.getUser(restorePasswordEmail.value!!)
                if (resultUser == null) {
                    sendEmailStatus.value = StatusCode.NOT_FOUND
                    return@launch
                }
                user = resultUser
                sendAccessKey()
            } catch (e: Exception) {
                Log.e("SendMail", e.message, e)
                sendEmailStatus.value = StatusCode.ERROR
            }
        }
    }

    private suspend fun sendAccessKey() {
        val accessKey = IntegerUtils().getRandomInteger()
        repository.updateAccessKey(user, accessKey).let {
            when (it) {
                StatusCode.SUCCESS.value -> {
                    val sender = GMailSender("trailead.ar@gmail.com", "xrykimnglzyeoglc")
                    sender.sendMail(
                        "TraiLead",
                        "Bienvenido a TraiLead, ingrese el siguiente código de seguridad para poder restaurar su contraseña: $accessKey",
                        restorePasswordEmail.value,
                        restorePasswordEmail.value!!
                    )
                    sendEmailStatus.value = StatusCode.SUCCESS
                }

                else -> {
                    sendEmailStatus.value = StatusCode.ERROR
                }
            }
        }
    }

    fun restorePassword() {
        TODO("Not yet implemented")
    }

    fun validateAccessKey(
        first: String, second: String, third: String, four: String
    ) {
        if (first.isEmpty() || second.isEmpty() || third.isEmpty() || four.isEmpty()) {
            restoreKeyAccess.value = StatusUpdateInformation.FAILED
            return
        }
        isValidAccessKey(IntegerUtils().convertKeyAccessToInteger(first, second, third, four))
    }

    /**
     * Validate access key on backend
     */
    private fun isValidAccessKey(accessKey: Int) {
        viewModelScope.launch {
            // TODO: validate user is null when send mail
            val result = repository.validateAccessKey(user, accessKey)
            restoreKeyAccess.value =
                if (result == StatusCode.SUCCESS.value) StatusUpdateInformation.SUCCESS else StatusUpdateInformation.FAILED
        }
    }

}