package com.aferrari.trailead.home.view.trainee

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.TraineeActivityBinding
import com.aferrari.trailead.home.StringUtils.StringUtils

class TraineeActivity : AppCompatActivity() {

    private lateinit var binding: TraineeActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.trainee_activity)
        initListener()
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
            .navigate(R.id.traineeHomeFragment, getBundleTab(0))
    }

    private fun navigateToProfile() {
        binding.fragmentContainerId.findNavController()
            .navigate(R.id.traineeProfileFragment, getBundleTab(1))
    }

    private fun navigateToTest() {
        // do nothing
    }


    private fun getBundleTab(tabId: Int): Bundle =
        Bundle().apply { this.putInt(StringUtils.TAB_ID, tabId) }

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