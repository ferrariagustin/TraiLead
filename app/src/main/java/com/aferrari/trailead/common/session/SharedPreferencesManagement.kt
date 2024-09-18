package com.aferrari.trailead.common.session

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManagement(context: Context) {

    companion object {
        const val SHARED_PREF_NAME: String = "user_data"
        const val USER_EMAIL: String = "email"
        const val NOTIF: String = "notif"
        const val USER_TOKEN: String = "token"
        const val DEFAULT_SESSION: String = ""
    }

    private var session: SharedPreferences =
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    private var editorSession: SharedPreferences.Editor = session.edit()

    fun saveSession(email: String, token: String) {
        editorSession.putString(USER_EMAIL, email).commit()
        editorSession.putString(USER_TOKEN, token).commit()
        enableNotification()
    }

    fun getSession(): Pair<String, String>? {
        val userId = session.getString(USER_EMAIL, DEFAULT_SESSION)
        val token = session.getString(USER_TOKEN, DEFAULT_SESSION)
        return if (userId != null && token != null) Pair(userId, token)
        else null
    }

    fun removeSession() {
        editorSession.putString(USER_EMAIL, DEFAULT_SESSION).commit()
        enableNotification()
    }

    fun enableNotification() {
        editorSession.putBoolean(NOTIF, true).commit()
    }

    fun disableNotification() {
        editorSession.putBoolean(NOTIF, false).commit()
    }

    fun isNotificationEnabled(): Boolean {
        return session.getBoolean(NOTIF, true)
    }
}
