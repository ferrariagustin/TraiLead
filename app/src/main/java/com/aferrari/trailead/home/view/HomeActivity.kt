package com.aferrari.trailead.home.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.aferrari.login.db.UserDataBase
import com.aferrari.login.db.UserRepository
import com.aferrari.login.session.SessionManagement
import com.aferrari.login.utils.StringUtils.DEEPLINK_LOGIN
import com.aferrari.login.utils.StringUtils.USER_NAME_KEY
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.HomeActivityBinding
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
        binding.logoutBtn.setOnClickListener {
            logout()
        }
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
        homeViewModel.getUser(intent.extras?.get(USER_NAME_KEY) as String)
    }
}