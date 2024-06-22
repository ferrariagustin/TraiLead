package com.aferrari.trailead.common

import com.aferrari.trailead.common.common_enum.UserType

object StringUtils {

    const val JOIN_DEEPLINK = "://"
    const val LEADER_KEY = "leader_key"
    const val TRAINEE_KEY = "trainee_key"
    const val TAB_ID = "tab_id"
    const val TRAINEE_INSTANCE_KEY = "trainee_instance_key"
    const val ERROR_WEBVIEW_PATH = "file:///android_asset/error.html"

    const val DEEPLINK_HOME = "trailead://home"
    const val DEEPLINK_LOGIN = "trailead://login"
    const val USER_NAME_KEY = "user_name"
    const val USER_EMAIL_KEY = "user_email"
    const val USER_KEY = "user_key"
    const val EMPTY_STRING = ""
    const val PDF_KEY = "pdf_key"
    const val DOWNLOAD_PDF = "download_pdf"

    fun getUserType(userType: String?): UserType? {
        when (userType) {
            UserType.LEADER.name -> UserType.LEADER
            UserType.TRAINEE.name -> UserType.TRAINEE
        }
        return null
    }

    fun isValidEmail(email: String?): Boolean {
        return !email.isNullOrEmpty()
                && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}