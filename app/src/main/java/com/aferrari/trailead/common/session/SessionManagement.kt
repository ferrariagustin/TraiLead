package com.aferrari.trailead.common.session

import android.content.Context
import android.content.SharedPreferences

class SessionManagement(context: Context) {

    companion object {
        const val SHARED_PREF_NAME: String = "trailead_session"
        const val SESSION_KEY: String = "trailead_user_session"
        const val SESSION_KEY_NAME: String = "user_session_name"
        const val SESSION_KEY_EMAIL: String = "user_session_email"
        const val SESSION_KEY_PASSWORD: String = "user_session_password"
        val DEFAULT_SESSION: String = "0"
    }

    private var session: SharedPreferences =
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    private var editorSession: SharedPreferences.Editor = session.edit()

    fun saveSession(userId: String) {
        editorSession.putString(SESSION_KEY, userId).commit()
    }

    fun getSession(): String? = session.getString(SESSION_KEY, DEFAULT_SESSION)

    fun removeSession() {
        editorSession.putString(SESSION_KEY, DEFAULT_SESSION).commit()
    }

}