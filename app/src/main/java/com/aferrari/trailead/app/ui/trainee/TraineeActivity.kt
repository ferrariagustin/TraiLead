package com.aferrari.trailead.app.ui.trainee

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.aferrari.trailead.R
import com.aferrari.trailead.app.viewmodel.HomeViewModelFactory
import com.aferrari.trailead.app.viewmodel.trainee.TraineeViewModel
import com.aferrari.trailead.common.BundleUtils
import com.aferrari.trailead.common.StringUtils
import com.aferrari.trailead.databinding.TraineeActivityBinding
import com.aferrari.trailead.domain.datasource.LocalDataSource
import com.aferrari.trailead.domain.datasource.RemoteDataSource
import com.aferrari.trailead.domain.models.Trainee
import com.aferrari.trailead.domain.repository.MaterialRepository
import com.aferrari.trailead.domain.repository.UserRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TraineeActivity : AppCompatActivity() {

    private lateinit var binding: TraineeActivityBinding

    private lateinit var homeTraineeViewModel: TraineeViewModel

    @Inject
    lateinit var remoteDataSource: RemoteDataSource

    @Inject
    lateinit var localDataSource: LocalDataSource


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.trainee_activity)
        val factory = HomeViewModelFactory(
            UserRepository(localDataSource, remoteDataSource),
            MaterialRepository(localDataSource, remoteDataSource)
        )
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
        binding.traineeSwipeToRefresh.setOnRefreshListener {
            homeTraineeViewModel.refresh()
            binding.traineeSwipeToRefresh.setColorSchemeResources(R.color.primaryColor)
            binding.traineeSwipeToRefresh.isRefreshing = false
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