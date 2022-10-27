package com.aferrari.trailead.home.view.leader

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.aferrari.login.db.Leader
import com.aferrari.login.db.UserDataBase
import com.aferrari.login.db.UserRepository
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.LeaderActivityBinding
import com.aferrari.trailead.home.StringUtils.StringUtils.LEADER_KEY
import com.aferrari.trailead.home.StringUtils.StringUtils.TAB_ID
import com.aferrari.trailead.home.viewmodel.HomeLeaderViewModel
import com.aferrari.trailead.home.viewmodel.HomeViewModelFactory

class LeaderActivity : AppCompatActivity() {

    lateinit var binding: LeaderActivityBinding

    private lateinit var homeLeaderViewModel: HomeLeaderViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.leader_activity)
        val dao = UserDataBase.getInstance(this).userDao
        val repository = UserRepository(dao)
        val factory = HomeViewModelFactory(repository)
        homeLeaderViewModel = ViewModelProvider(this, factory)[HomeLeaderViewModel::class.java]
        binding.lifecycleOwner = this
        initComponent()
        initListener()
    }

    private fun initComponent() {
        homeLeaderViewModel.setLeader(intent.extras?.get(LEADER_KEY) as? Leader)
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
    }

    private fun navigateToProfile() {
        binding.fragmentContainerId.findNavController()
            .navigate(R.id.leaderProfileFragment, getBundleTab(2))
    }

    private fun navigateToHome() {
        binding.fragmentContainerId.findNavController()
            .navigate(R.id.leaderHomeFragment, getBundleTab(0))
    }

    private fun navigateToTrainee() {
        binding.fragmentContainerId.findNavController()
            .navigate(R.id.linkedTraineeListFragment, getBundleTab(1))
    }

    private fun getBundleTab(tabId: Int): Bundle = Bundle().apply { this.putInt(TAB_ID, tabId) }

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
