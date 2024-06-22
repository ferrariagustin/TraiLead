package com.aferrari.trailead.app.ui.leader

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.aferrari.trailead.R
import com.aferrari.trailead.app.viewmodel.HomeViewModelFactory
import com.aferrari.trailead.app.viewmodel.leader.LeaderViewModel
import com.aferrari.trailead.common.BundleUtils
import com.aferrari.trailead.common.StringUtils.LEADER_KEY
import com.aferrari.trailead.common.StringUtils.TAB_ID
import com.aferrari.trailead.databinding.LeaderActivityBinding
import com.aferrari.trailead.domain.datasource.RemoteDataSource
import com.aferrari.trailead.domain.models.Leader
import com.aferrari.trailead.domain.repository.MaterialRepository
import com.aferrari.trailead.domain.repository.UserRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class LeaderActivity : AppCompatActivity() {

    @Inject
    lateinit var remoteDataSource: RemoteDataSource

    lateinit var binding: LeaderActivityBinding

    private lateinit var leaderViewModel: LeaderViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.leader_activity)
        val factory = HomeViewModelFactory(
            UserRepository(remoteDataSource),
            MaterialRepository(remoteDataSource)
        )
        leaderViewModel = ViewModelProvider(this, factory)[LeaderViewModel::class.java]
        binding.lifecycleOwner = this
        initComponent()
        initListener()
    }

    private fun initComponent() {
        leaderViewModel.setLeader(intent.extras?.get(LEADER_KEY) as? Leader)
    }

    private fun initListener() {
        binding.bottomNavigationId.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home_leader_menu -> if (!it.isChecked) {
                    navigateToHome()
                }

                R.id.trainee_list_leader_menu -> if (!it.isChecked) {
                    navigateToTrainee()
                }

                R.id.profile_leader_menu -> if (!it.isChecked) {
                    navigateToProfile()
                }
            }
            return@setOnItemSelectedListener true
        }
        leaderViewModel.bottomNavigationViewVisibility.observe(this) {
            binding.bottomNavigationId.visibility = it
        }
        leaderViewModel.isRefreshingEnabled.observe(this) {
            binding.leaderSwipeToRefresh.isEnabled = it
        }
        binding.leaderSwipeToRefresh.setOnRefreshListener {
            leaderViewModel.refresh()
            binding.leaderSwipeToRefresh.setColorSchemeResources(R.color.primaryColor)
            binding.leaderSwipeToRefresh.isRefreshing = false
        }
    }

    private fun navigateToHome() {
        binding.fragmentLeaderContainerId.findNavController()
            .navigate(R.id.leaderHomeFragment, BundleUtils().getBundleTab(0))
    }

    private fun navigateToTrainee() {
        binding.fragmentLeaderContainerId.findNavController()
            .navigate(R.id.linkedTraineeListFragment, BundleUtils().getBundleTab(1))
    }

    private fun navigateToProfile() {
        binding.fragmentLeaderContainerId.findNavController()
            .navigate(R.id.leaderProfileFragment, BundleUtils().getBundleTab(2))
    }

    override fun onResume() {
        super.onResume()
        leaderViewModel.refresh()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        binding.fragmentLeaderContainerId.findNavController().currentBackStackEntry?.arguments.let {
            if (it != null) {
                binding.bottomNavigationId.menu.getItem(it[TAB_ID] as Int).isChecked = true
            } else {
                binding.bottomNavigationId.menu.getItem(0).isChecked = true
            }
        }
    }
}
