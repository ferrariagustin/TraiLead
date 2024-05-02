package com.aferrari.trailead.app.viewmodel.login

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aferrari.trailead.common.IntegerUtils
import com.aferrari.trailead.common.PasswordUtil
import com.aferrari.trailead.common.StringUtils.isValidEmail
import com.aferrari.trailead.common.common_enum.LoginState
import com.aferrari.trailead.common.common_enum.StatusCode
import com.aferrari.trailead.common.common_enum.StatusUpdateInformation
import com.aferrari.trailead.common.common_enum.toStatusUpdateInformation
import com.aferrari.trailead.common.email.GMailSender
import com.aferrari.trailead.common.session.SessionManagement
import com.aferrari.trailead.domain.models.User
import com.aferrari.trailead.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

open class LoginViewModel(val repository: UserRepository) :
    ViewModel() {

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

    fun login(context: Context) {
        if (validateInput()) return
        signInWithEmail(inputEmail.value!!, inputPass.value!!, context)
    }

    fun login(email: String, token: String) {
        signInWithToken(email, token)
    }

    private fun signInWithToken(email: String, token: String) {
        viewModelScope.launch {
            repository.signInWithToken(email, token).collect { state ->
                when (state.value.toStatusUpdateInformation()) {
                    StatusUpdateInformation.SUCCESS -> {
                        val uId = FirebaseAuth.getInstance().currentUser?.uid
                        if (uId != null)
                            goLogin(uId)
                        else
                            goLoginError()
                    }

                    StatusUpdateInformation.FAILED -> goLoginError()
                    StatusUpdateInformation.INTERNET_CONECTION -> goLoginError()
                    else -> goLoginError()
                }
            }
        }
    }

    private fun signInWithEmail(email: String, pass: String, context: Context) {
        viewModelScope.launch {
            val hashPassword = PasswordUtil.hashSHA256Base36(pass)
            repository.signInWithEmail(email, hashPassword).collect { status ->
                when (status.value.toStatusUpdateInformation()) {
                    StatusUpdateInformation.SUCCESS -> {
                        val uId = FirebaseAuth.getInstance().currentUser?.uid
                        if (uId != null) {
                            saveSession(context, email, hashPassword)
                            goLogin(uId)
                        } else goLoginError()
                    }

                    StatusUpdateInformation.FAILED -> goLoginError()
                    StatusUpdateInformation.INTERNET_CONECTION -> goLoginErrorInternetConection()
                    else -> goLoginError()
                }
            }
        }
    }

    private fun goLoginErrorInternetConection() {
        loginState.value = LoginState.INTERNET_CONECTION
    }

    private fun validateInput(): Boolean {
        loginState.value = LoginState.IN_PROGRESS
        if (inputEmail.value.isNullOrEmpty()) {
            // TODO: handle case
            Log.e("TRAILEAD", "Debe agregar ingresar un email")
            goLoginError()
            return true
        }
        if (inputPass.value.isNullOrEmpty()) {
            // TODO: handle case
            goLoginError()
            Log.e("TRAILEAD", "Debe agregar ingresar un password")
            return true
        }
        return false
    }

    fun goRegister() {
        loginState.value = LoginState.REGISTER
    }

    private fun goLoginError() {
        loginState.value = LoginState.FAILED
    }

    private fun goLogin(userId: String) {
        viewModelScope.launch {
            try {
                repository.getUser(userId).collect {
                    it?.let {
                        goLoginSuccess(it)
                    }
                }
            } catch (e: Exception) {
                goLoginError()
            }
        }
    }

    private fun goLoginSuccess(user: User) {
        this.user = user
        loginState.value = LoginState.SUCCESS
    }

    /**
     * return User for userId
     */
    fun getUser(userId: String) {
        viewModelScope.launch {
            repository.getUser(userId).collect {
                it?.let {
                    user = it
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
                val resultUser = repository.getUserByEmail(restorePasswordEmail.value!!)
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

    /**
     * Save session in shared preferences
     */
    private fun saveSession(context: Context, email: String, hashPassword: String) {
        SessionManagement(context).saveSession(email, hashPassword)
    }

    /**
     * Validate if exist some session in sharedPrefernce.
     * If exist a session, redirect to home
     */
    fun validateSession(context: Context) {
        loginState.value = LoginState.IN_PROGRESS
        val pairSession = SessionManagement(context).getSession()
        if (pairSession != null && pairSession.first.isNotEmpty() && pairSession.second.isNotEmpty()) {
            login(pairSession.first, pairSession.second)
        } else {
            loginState.value = LoginState.STARTED
        }
    }
}