package com.aferrari.login.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.aferrari.login.R
import com.aferrari.login.databinding.LoginActivityBinding
import com.aferrari.login.model.User
import com.aferrari.login.session.SessionManagement
import com.aferrari.login.utils.StringUtils.DEEPLINK_HOME
import com.aferrari.login.viewmodel.UserViewModel

class LoginActivity : AppCompatActivity(), Login {

    private lateinit var binding: LoginActivityBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.login_activity)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        binding.loginBtn.setOnClickListener {
            val user = binding.userIdLogin.text.toString()
            val pass = binding.passwordIdLogin.text.toString()
            login(user, pass)
        }
    }

    override fun onStart() {
        super.onStart()

        checkSession()
    }

    /**
     * Validate if exist some session in sharedPrefernce.
     * If exist a session, redirect to home
     */
    private fun checkSession() {
        val userId = SessionManagement(this).getSession()
        if (userId != -1) {
            val user = userViewModel.getUser(userId, this)
            if (user != null) {
                goHome(user)
            }
        }
    }

    override fun login(user: String, pass: String) {
        userViewModel.loginUser(user, pass)
        userViewModel.getUserLogin().observe({ lifecycle }, {
            SessionManagement(this).saveSession(it)
            goHome(it)
        })
    }

    override fun goHome(user: User) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(DEEPLINK_HOME)).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra("user_name", user.name)
            putExtra("user_surname", user.surname)
        }
        startActivity(intent)
    }
}