package com.aferrari.login.viewmodel.register

enum class RegisterState {
    STARTED, IN_PROGRESS, SUCCESS, FAILED, FAILED_USER_EXIST, CANCEL
}

enum class RegisterErrorState {
    DONE, ERROR
}