package com.aferrari.trailead.home.view.leader

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.aferrari.login.data.user.Leader
import com.aferrari.login.data.UserDataBase
import com.aferrari.login.data.user.repository.UserRepository
import com.aferrari.login.data.material.repository.MaterialRepository
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.LeaderActivityBinding
import com.aferrari.trailead.home.Utils.BundleUtils
import com.aferrari.trailead.home.Utils.StringUtils.LEADER_KEY
import com.aferrari.trailead.home.Utils.StringUtils.TAB_ID
import com.aferrari.trailead.home.viewmodel.HomeViewModelFactory
import com.aferrari.trailead.home.viewmodel.leader.LeaderViewModel

class LeaderActivity : AppCompatActivity() {

    lateinit var binding: LeaderActivityBinding

    private lateinit var leaderViewModel: LeaderViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.leader_activity)
        val dao = UserDataBase.getInstance(this).userDao
        val materialDao = UserDataBase.getInstance(this).materialDao
        val factory = HomeViewModelFactory(UserRepository(dao), MaterialRepository(materialDao))
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
    }

    private fun navigateToHome() {
        binding.fragmentContainerId.findNavController()
            .navigate(R.id.leaderHomeFragment, BundleUtils().getBundleTab(0))
    }

    private fun navigateToTrainee() {
        binding.fragmentContainerId.findNavController()
            .navigate(R.id.linkedTraineeListFragment, BundleUtils().getBundleTab(1))
    }

    private fun navigateToProfile() {
        binding.fragmentContainerId.findNavController()
            .navigate(R.id.leaderProfileFragment, BundleUtils().getBundleTab(2))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        binding.fragmentContainerId.findNavController().currentBackStackEntry?.arguments.let {
            if (it != null) {
                binding.bottomNavigationId.menu.getItem(it[TAB_ID] as Int).isChecked = true
            } else {
                binding.bottomNavigationId.menu.getItem(0).isChecked = true
            }
        }
    }
}
