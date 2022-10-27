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
import com.aferrari.trailead.home.viewmodel.HomeLeaderViewModel
import com.aferrari.trailead.home.viewmodel.HomeViewModelFactory

class LeaderActivity : AppCompatActivity() {

    internal lateinit var binding: LeaderActivityBinding

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
                R.id.home_leader_menu -> if (!it.isChecked) navigateToHome()
                R.id.trainee_list_leader_menu -> if (!it.isChecked) navigateToTrainee()
                R.id.profile_leader_menu -> if (!it.isChecked) navigateToProfile()
            }
            return@setOnItemSelectedListener true
        }
    }

    private fun navigateToProfile() {
        binding.fragmentContainerId.findNavController().navigate(R.id.leaderProfileFragment)
    }

    private fun navigateToHome() {
        binding.fragmentContainerId.findNavController().navigate(R.id.leaderHomeFragment)
    }

    private fun navigateToTrainee() {
        binding.fragmentContainerId.findNavController().navigate(R.id.linkedTraineeListFragment)
    }

}