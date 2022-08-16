package com.aferrari.login.viewmodel.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aferrari.login.db.User
import com.aferrari.login.db.UserRepository
import com.aferrari.login.db.UserType
import java.util.*

class RegistrationViewModel(private val repository: UserRepository) : ViewModel() {
    lateinit var user: User
        private set

    val users = repository.users

    var registerState = MutableLiveData<RegisterState>()

    val inputName = MutableLiveData<String>()
    val inputNameError = MutableLiveData(RegisterErrorState.DONE)

    val inputLastName = MutableLiveData<String>()
    val inputLastNameError = MutableLiveData(RegisterErrorState.DONE)

    val inputEmail = MutableLiveData<String>()
    val inputEmailError = MutableLiveData(RegisterErrorState.DONE)

    val inputPass = MutableLiveData<String>()
    val inputPassError = MutableLiveData(RegisterErrorState.DONE)

    val inputRepeatPass = MutableLiveData<String>()
    val inputRepeatPassError = MutableLiveData(RegisterErrorState.DONE)

    var inputUserType = MutableLiveData<String>()

    private var userType: UserType = UserType.TRAINEE

    init {
        registerState.value = RegisterState.STARTED
    }

    fun cancel() {
        cleanView()
        goLogin()
    }

    fun register() {
        restoreInputState()
        if (!validateInput()) {
            registerState.value = RegisterState.FAILED
        }
        // registrar user
        registerState.value = RegisterState.SUCCESS
    }

    private fun restoreInputState() {
        inputNameError.value = RegisterErrorState.DONE
        inputLastNameError.value = RegisterErrorState.DONE
        inputEmailError.value = RegisterErrorState.DONE
        inputPassError.value = RegisterErrorState.DONE
        inputRepeatPassError.value = RegisterErrorState.DONE
    }

    private fun goLogin() {
        registerState.value = RegisterState.CANCEL
    }

    private fun cleanView() {
        // TODO: Clean all components on register view
    }

    fun switchTypeUser() {
        userType = when (userType) {
            UserType.TRAINEE -> {
                UserType.LEADER
            }
            UserType.LEADER -> {
                UserType.TRAINEE
            }
        }
        inputUserType.value = userType.name.lowercase(Locale.ROOT)
    }

    private fun validateInput(): Boolean {
        var isValidInput = true
        registerState.value = RegisterState.IN_PROGRESS
        if (!isValidInput(inputName)) {
            inputNameError.value = RegisterErrorState.ERROR
            isValidInput = false
        }
        if (!isValidInput(inputLastName)) {
            inputLastNameError.value = RegisterErrorState.ERROR
            isValidInput = false
        }
        if (!isValidInput(inputEmail)) {
            inputEmailError.value = RegisterErrorState.ERROR
            isValidInput = false
        }
        if (!isValidInput(inputPass)) {
            inputPassError.value = RegisterErrorState.ERROR
            isValidInput = false
        }
        if (!isValidInput(inputRepeatPass)) {
            inputRepeatPassError.value = RegisterErrorState.ERROR
            isValidInput = false
        }
        return isValidInput
    }

    private fun isValidInput(input: MutableLiveData<String>): Boolean {
        if (input.value.isNullOrEmpty()) {
            return false
        }
        return true
    }
}