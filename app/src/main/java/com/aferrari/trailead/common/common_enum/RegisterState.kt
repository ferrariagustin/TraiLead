package com.aferrari.trailead.common.common_enum

enum class RegisterState {
    STARTED, IN_PROGRESS, SUCCESS, FAILED, FAILED_USER_EXIST, CANCEL
}

//sealed class UIState {
//    data object Loading : UIState()
//    data class Success(val data: String) : UIState()
//    data class Error(val exception: Exception) : UIState()
//}

enum class RegisterErrorState {
    DONE, ERROR
}