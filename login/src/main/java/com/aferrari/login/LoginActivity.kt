package com.aferrari.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.aferrari.login.databinding.LoginActivityBinding
import com.aferrari.login.model.User
import com.aferrari.login.session.SessionManagement
import kotlin.random.Random

class LoginActivity : AppCompatActivity(), Login {

    companion object {
        const val DEEPLINK_HOME = "trailead://home"
        const val DEEPLINK_LOGIN = "trailead://login"
    }

    private lateinit var loginActivityBinding: LoginActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginActivityBinding = DataBindingUtil.setContentView(this, R.layout.login_activity)

        loginActivityBinding.loginBtn.setOnClickListener {
            login()
            goHome()
        }
    }

    override fun onStart() {
        super.onStart()

        checkSession()
    }

    private fun checkSession() {
        val userId = SessionManagement(this).getSession()
        if (userId != -1) {
            goHome()
        }
    }

    override fun login() {
        val user = User(Random(10).nextInt(), "Agustin", "ferrari")
        SessionManagement(this).saveSession(user)
    }

    override fun goHome() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(DEEPLINK_HOME))
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_CLEAR_TASK or
                Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}