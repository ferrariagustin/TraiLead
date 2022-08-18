package com.aferrari.login.session

import android.content.Context
import android.content.SharedPreferences
import com.aferrari.login.db.User

class SessionManagement(context: Context) {

    companion object {
        const val SHARED_PREF_NAME: String = "session"
        const val SESSION_KEY: String = "user_session"
        const val SESSION_KEY_NAME: String = "user_session_name"
        const val SESSION_KEY_EMAIL: String = "user_session_email"
        const val SESSION_KEY_PASSWORD: String = "user_session_password"
        const val DEFAULT_SESSION: Int = -1
    }

    private var session: SharedPreferences =
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    private var editorSession: SharedPreferences.Editor = session.edit()

    fun saveSession(user: User) {
        editorSession.putInt(SESSION_KEY, user.id).commit()
//        editorSession.putString(SESSION_KEY_NAME, user.name).commit()
//        editorSession.putString(SESSION_KEY_EMAIL, user.email).commit()
//        editorSession.putString(SESSION_KEY_PASSWORD, user.pass).commit()
    }

    fun getSession(): Int = session.getInt(SESSION_KEY, DEFAULT_SESSION)

    fun removeSession() {
        editorSession.putInt(SESSION_KEY, DEFAULT_SESSION).commit()
    }

//    fun getUser(userId: Int): User? {
//        val userId = getSession()
//        if (userId != DEFAULT_SESSION) {
//            return User(
//                userId,
//                session.getString(SESSION_KEY_NAME, "") as String,
//                session.getString(SESSION_KEY_EMAIL, "") as String,
//                session.getString(SESSION_KEY_PASSWORD, "") as String
//            )
//        }
//        return null
//    }


}