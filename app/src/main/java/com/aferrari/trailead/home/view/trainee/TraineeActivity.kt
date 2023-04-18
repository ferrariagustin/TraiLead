package com.aferrari.trailead.home.view.trainee

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.aferrari.login.data.Trainee
import com.aferrari.login.data.UserDataBase
import com.aferrari.login.data.UserRepository
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.TraineeActivityBinding
import com.aferrari.trailead.home.Utils.BundleUtils
import com.aferrari.trailead.home.Utils.StringUtils
import com.aferrari.trailead.home.viewmodel.trainee.TraineeViewModel
import com.aferrari.trailead.home.viewmodel.HomeViewModelFactory

class TraineeActivity : AppCompatActivity() {

    private lateinit var binding: TraineeActivityBinding

    private lateinit var homeTraineeViewModel: TraineeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.trainee_activity)
        val dao = UserDataBase.getInstance(this).userDao
        val repository = UserRepository(dao)
        val factory = HomeViewModelFactory(repository)
        homeTraineeViewModel = ViewModelProvider(this, factory)[TraineeViewModel::class.java]
        binding.lifecycleOwner = this
        initComponent()
        initListener()
    }

    private fun initComponent() {
        homeTraineeViewModel.setTrainee(intent.extras?.get(StringUtils.TRAINEE_KEY) as? Trainee)
    }

    private fun initListener() {
        binding.traineeBottomNavigationId.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.trainee_leader_menu -> if (!it.isChecked) {
                    navigateToHome()
                }
                R.id.profile_trainee_menu -> if (!it.isChecked) {
                    navigateToProfile()
                }
                R.id.trainee_test -> if (!it.isChecked) {
                    navigateToTest()
                }
            }
            return@setOnItemSelectedListener true
        }
    }

    private fun navigateToHome() {
        binding.fragmentContainerId.findNavController()
            .navigate(R.id.traineeHomeFragment, BundleUtils().getBundleTab(0))
    }

    private fun navigateToProfile() {
        binding.fragmentContainerId.findNavController()
            .navigate(R.id.traineeProfileFragment, BundleUtils().getBundleTab(1))
    }

    private fun navigateToTest() {
        // do nothing
    }

    override fun onBackPressed() {
        super.onBackPressed()
        binding.fragmentContainerId.findNavController().currentBackStackEntry?.arguments.let {
            if (it != null) {
                binding.traineeBottomNavigationId.menu.getItem(it[StringUtils.TAB_ID] as Int).isChecked =
                    true
            } else {
                binding.traineeBottomNavigationId.menu.getItem(0).isChecked = true
            }
        }
    }
}