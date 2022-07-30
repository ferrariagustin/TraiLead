package com.aferrari.login.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.aferrari.login.R
import com.aferrari.login.databinding.LoginActivityBinding
import com.aferrari.login.db.User
import com.aferrari.login.db.UserDataBase
import com.aferrari.login.db.UserRepository
import com.aferrari.login.session.SessionManagement
import com.aferrari.login.utils.StringUtils.DEEPLINK_HOME
import com.aferrari.login.utils.StringUtils.USER_EMAIL_KEY
import com.aferrari.login.utils.StringUtils.USER_NAME_KEY
import com.aferrari.login.viewmodel.LoginViewModel
import com.aferrari.login.viewmodel.LoginViewModelFactory
import com.aferrari.login.viewmodel.StateLogin

class LoginActivity : AppCompatActivity(), Login, LifecycleOwner {

    private lateinit var binding: LoginActivityBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.login_activity)
        val dao = UserDataBase.getInstance(application).userDao
        val repository = UserRepository(dao)
        val factory = LoginViewModelFactory(repository)
        loginViewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)
        binding.loginViewModel = loginViewModel
        binding.lifecycleOwner = this

        observeLogin()
    }

    private fun observeLogin() {
        loginViewModel.stateLogin.observe(this) {
            when (it!!) {
                StateLogin.STARTED -> startLogin()
                StateLogin.IN_PROGRESS -> showProgressBar()
                StateLogin.FAILED -> errorLogin()
                StateLogin.SUCCESS -> successLogin()
            }
        }
    }

    private fun startLogin() {
        // do nothing
    }

    private fun successLogin() {
        binding.progressBar.visibility = View.GONE
        SessionManagement(this).saveSession(loginViewModel.user)
        goHome(loginViewModel.user)
    }

    private fun errorLogin() {
        Toast.makeText(this, "Login Error", Toast.LENGTH_SHORT).show()
        binding.progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
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
            val user = loginViewModel.getUser(userId, this)
            if (user != null) {
                goHome(user)
            }
        }
    }

    override fun goHome(user: User) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(DEEPLINK_HOME)).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            putExtra(USER_NAME_KEY, user.name)
            putExtra(USER_EMAIL_KEY, user.email)
        }
        startActivity(intent)
        finish()
    }
}