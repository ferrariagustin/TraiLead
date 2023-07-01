package com.aferrari.trailead.app.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.aferrari.trailead.R
import com.aferrari.trailead.app.viewmodel.HomeViewModel
import com.aferrari.trailead.app.viewmodel.HomeViewModelFactory
import com.aferrari.trailead.common.UserState
import com.aferrari.trailead.common.StringUtils
import com.aferrari.trailead.common.StringUtils.JOIN_DEEPLINK
import com.aferrari.trailead.common.StringUtils.LEADER_KEY
import com.aferrari.trailead.common.StringUtils.TRAINEE_KEY
import com.aferrari.trailead.common.ui.TraileadDialog
import com.aferrari.trailead.domain.datasource.LocalDataSource
import com.aferrari.trailead.domain.datasource.RemoteDataSource
import com.aferrari.trailead.domain.models.User
import com.aferrari.trailead.domain.repository.UserRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Moved to other package or module
 */
@AndroidEntryPoint
class RouterHomeActivity : AppCompatActivity() {

    private lateinit var homeViewModel: HomeViewModel

    @Inject
    lateinit var remoteDataSource: RemoteDataSource

    @Inject
    lateinit var localDataSource: LocalDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        val repository = UserRepository(localDataSource, remoteDataSource)
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
                UserState.LEADER -> goLeaderScreen()
                UserState.TRAINEE -> goTraineeScreen()
                UserState.ERROR -> goLoginScreen()
            }
        }
    }

    private fun goLoginScreen() {
        TraileadDialog().showDialog(
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
                this.putExtra(TRAINEE_KEY, homeViewModel.user)
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