package com.aferrari.login.session

import android.content.Context
import android.content.SharedPreferences
import com.aferrari.login.model.User

class SessionManagement(context: Context) {

    companion object {
        const val SHARED_PREF_NAME: String = "session"
        const val SESSION_KEY: String = "user_session"
        const val DEFAULT_SESSION: Int = -1
    }

    private var session: SharedPreferences =
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    private var editorSession: SharedPreferences.Editor = session.edit()

    fun saveSession(user: User) {
        editorSession.putInt(SESSION_KEY, user.id).commit()
    }

    fun getSession(): Int = session.getInt(SESSION_KEY, DEFAULT_SESSION)

    fun removeSession() {
        editorSession.putInt(SESSION_KEY, DEFAULT_SESSION).commit()
    }

}