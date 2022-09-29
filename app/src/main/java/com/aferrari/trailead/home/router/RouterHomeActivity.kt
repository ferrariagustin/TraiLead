package com.aferrari.trailead.home.router

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.aferrari.login.R
import com.aferrari.login.db.User
import com.aferrari.login.db.UserDataBase
import com.aferrari.login.db.UserRepository
import com.aferrari.login.dialog.Dialog
import com.aferrari.login.utils.StringUtils
import com.aferrari.trailead.home.StringUtils.StringUtils.JOIN_DEEPLINK
import com.aferrari.trailead.home.StringUtils.StringUtils.LEADER_KEY
import com.aferrari.trailead.home.StringUtils.StringUtils.TRAINEE_KEY
import com.aferrari.trailead.home.viewmodel.HomeState
import com.aferrari.trailead.home.viewmodel.HomeViewModel
import com.aferrari.trailead.home.viewmodel.HomeViewModelFactory

/**
 * Moved to other package or module
 */
class RouterHomeActivity : AppCompatActivity() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        val dao = UserDataBase.getInstance(this).userDao
        val repository = UserRepository(dao)
        val factory = HomeViewModelFactory(repository)
        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
        initComponent()
        initListeners()
    }

    private fun initComponent() {
        homeViewModel.getUser(intent.extras?.get(StringUtils.USER_KEY) as User)
    }

    private fun initListeners() {
        homeViewModel.homeState.observe(this) {
            when (it) {
                HomeState.LEADER -> goLeaderScreen()
                HomeState.TRAINEE -> goTraineeScreen()
                HomeState.ERROR -> goLoginScreen()
            }
        }
    }

    private fun goLoginScreen() {
        Dialog().showDialog(
            resources.getString(R.string.title_error),
            resources.getString(R.string.error_login_home),
            this
        )

        val intent =
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    resources.getString(R.string.scheme_app_name) + JOIN_DEEPLINK +
                            resources.getString(R.string.host_login)
                )
            )
        startActivity(intent)
        finish()
    }

    private fun goTraineeScreen() {
        val intent =
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    resources.getString(R.string.scheme_app_name) + JOIN_DEEPLINK +
                            resources.getString(R.string.host_trainee_home)
                )
            ).apply {
                putExtra(TRAINEE_KEY, homeViewModel.user)
            }
        startActivity(intent)
        finish()
    }

    private fun goLeaderScreen() {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(
                resources.getString(R.string.scheme_app_name) + JOIN_DEEPLINK +
                        resources.getString(R.string.host_leader_home)
            )
        ).apply {
            putExtra(LEADER_KEY, homeViewModel.user)
        }
        startActivity(intent)
        finish()
    }
}