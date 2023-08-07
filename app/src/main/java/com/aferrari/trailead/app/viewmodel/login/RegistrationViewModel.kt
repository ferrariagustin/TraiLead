package com.aferrari.trailead.app.viewmodel.login

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aferrari.trailead.common.IntegerUtils
import com.aferrari.trailead.common.StringUtils.EMPTY_STRING
import com.aferrari.trailead.common.common_enum.RegisterErrorState
import com.aferrari.trailead.common.common_enum.RegisterState
import com.aferrari.trailead.common.common_enum.UserType
import com.aferrari.trailead.domain.models.Leader
import com.aferrari.trailead.domain.models.Trainee
import com.aferrari.trailead.domain.models.User
import com.aferrari.trailead.domain.repository.UserRepository
import java.util.*
import kotlinx.coroutines.launch

class RegistrationViewModel(private val repository: UserRepository) : ViewModel() {
    lateinit var user: User
        private set

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

    var visibilityPassDrawable = MutableLiveData<Int>()

    private var userType: UserType = UserType.TRAINEE

    init {
        visibilityPassDrawable.value = View.GONE
        registerState.value = RegisterState.STARTED
    }

    /**
     * Binding method to Cancel button
     */
    fun cancel() {
        restoreInputState()
        restoreInputStateValues()
        goLogin()
    }

    /**
     * Binding method to Registration button
     */
    fun initRegistration() {
        restoreInputState()
        if (!validateInput()) {
            registerState.value = RegisterState.FAILED
            return
        }
        register()
    }

    private fun register() {
        viewModelScope.launch {
            if (existUser()) {
                registerState.value = RegisterState.FAILED_USER_EXIST
                return@launch
            }
            var result: Long? = null
            if (userType == UserType.TRAINEE) {
                result = insertTrainee()
            }
            if (userType == UserType.LEADER) {
                result = insertLeader()
            }
            Log.e("TRAILEAD", "Register result = $result")
            registerState.value = RegisterState.SUCCESS
            restoreInputState()
            restoreInputStateValues()
        }
    }

    /**
     * Validate if exist the user in DB
     */
    private suspend fun existUser(): Boolean {
        val result = repository.getUser(inputEmail.value!!)
        Log.e("TRAILEAD", "Register result = $result")
        return result != null
    }

    private suspend fun insertLeader(): Long = repository.insertLeader(
        Leader(
            id = IntegerUtils().createObjectId(),
            name = inputName.value!!,
            lastName = inputLastName.value!!,
            email = inputEmail.value!!,
            pass = inputPass.value!!
        )
    )

    private suspend fun insertTrainee(): Long = repository.insertTrainee(
        Trainee(
            id = IntegerUtils().createObjectId(),
            name = inputName.value!!,
            lastName = inputLastName.value!!,
            email = inputEmail.value!!,
            pass = inputPass.value!!,
        )
    )

    /**
     * Restore all components on registration fragment
     */
    private fun restoreInputState() {
        inputNameError.value = RegisterErrorState.DONE
        inputLastNameError.value = RegisterErrorState.DONE
        inputEmailError.value = RegisterErrorState.DONE
        inputPassError.value = RegisterErrorState.DONE
        inputRepeatPassError.value = RegisterErrorState.DONE
    }

    /**
     * Restore all components on registration fragment
     */
    private fun restoreInputStateValues() {
        inputName.value = EMPTY_STRING
        inputLastName.value = EMPTY_STRING
        inputEmail.value = EMPTY_STRING
        inputPass.value = EMPTY_STRING
        inputRepeatPass.value = EMPTY_STRING
    }

    /**
     * navigate to Login
     */
    private fun goLogin() {
        registerState.value = RegisterState.CANCEL
    }

    /**
     * Update label on switch and apply lowercase
     */
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

    fun setDrawableVisibilityPass() {
        if (visibilityPassDrawable.value == View.GONE) {
            visibilityPassDrawable.value = View.VISIBLE
        } else {
            visibilityPassDrawable.value = View.GONE
        }
    }

    /**
     * Validate input on textInput Registration fragment
     */
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
        if (!isValidEmail()) {
            inputEmailError.value = RegisterErrorState.ERROR
            isValidInput = false
        }
        if (!isValidInput(inputPass)) {
            inputPassError.value = RegisterErrorState.ERROR
            isValidInput = false
        }
        if (!isValidRepeatPass()) {
            inputRepeatPassError.value = RegisterErrorState.ERROR
            isValidInput = false
        }
        return isValidInput
    }

    private fun isValidRepeatPass(): Boolean =
        !inputRepeatPass.value.isNullOrEmpty() && inputRepeatPass.value.equals(inputPass.value)

    private fun isValidEmail(): Boolean {
        return !inputEmail.value.isNullOrEmpty()
                && android.util.Patterns.EMAIL_ADDRESS.matcher(inputEmail.value).matches()
    }

    private fun isValidInput(input: MutableLiveData<String>): Boolean {
        if (input.value.isNullOrEmpty()) {
            return false
        }
        return true
    }
}