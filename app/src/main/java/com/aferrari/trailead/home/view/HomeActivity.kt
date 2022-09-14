package com.aferrari.trailead.home.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aferrari.login.db.UserDataBase
import com.aferrari.login.db.UserRepository
import com.aferrari.login.session.SessionManagement
import com.aferrari.login.utils.StringUtils.DEEPLINK_LOGIN
import com.aferrari.login.utils.StringUtils.USER_ID_KEY
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.HomeActivityBinding
import com.aferrari.trailead.home.viewmodel.HomeState
import com.aferrari.trailead.home.viewmodel.HomeViewModel
import com.aferrari.trailead.home.viewmodel.HomeViewModelFactory

/**
 * Moved to other package or module
 */
// TODO: don't working back and restore, remove session
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: HomeActivityBinding

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.home_activity)
        val dao = UserDataBase.getInstance(this).userDao
        val repository = UserRepository(dao)
        val factory = HomeViewModelFactory(repository)
        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
        binding.lifecycleOwner = this
        initComponent()
        initListeners()
    }

    override fun onStart() {
        super.onStart()

    }

    private fun initListeners() {
        homeViewModel.homeState.observe(this, Observer {
            when (it) {
                HomeState.LEADER -> goLeaderScreen()
                HomeState.TRAINEE -> goTraineeScreen()
                HomeState.NONE -> goErrorScreen()
            }
        })
        binding.logoutBtn.setOnClickListener {
            logout()
        }
    }

    private fun goErrorScreen() {
        Toast.makeText(this, "Dialog de error y show login", Toast.LENGTH_SHORT).show()
    }

    private fun goTraineeScreen() {
        Toast.makeText(this, "Flujo de Trainee", Toast.LENGTH_SHORT).show()
    }

    private fun goLeaderScreen() {
        Toast.makeText(this, "Flujo de Leader", Toast.LENGTH_SHORT).show()
    }

    private fun logout() {
        SessionManagement(this).removeSession()
        goLogin()
    }

    private fun goLogin() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(DEEPLINK_LOGIN))
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun initComponent() {
        homeViewModel.getUser(intent.extras?.get(USER_ID_KEY) as Int)
    }
}