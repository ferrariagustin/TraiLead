package com.aferrari.trailead.app.ui.trainee.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aferrari.trailead.R
import com.aferrari.trailead.app.ui.RefreshListener
import com.aferrari.trailead.app.viewmodel.trainee.TraineeViewModel
import com.aferrari.trailead.common.BundleUtils
import com.aferrari.trailead.databinding.TraineeProfileFragmentBinding


class TraineeProfileFragment : Fragment(), RefreshListener {

    private lateinit var binding: TraineeProfileFragmentBinding
    private val homeTraineeViewModel: TraineeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.trainee_profile_fragment, container, false)
        binding.lifecycleOwner = this
        binding.traineeViewModel = homeTraineeViewModel
        initListeners()
        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initListeners() {
        binding.imageViewEditCompleteNameIcon.setOnClickListener {
            navigateToEditProfileName()
        }

        binding.imageViewEditPasswordIcon.setOnClickListener {
            navigateToEditPassword()
        }
        
        refresh()
    }

    private fun navigateToEditPassword() {
        findNavController().navigate(
            R.id.action_traineeProfileFragment_to_traineeEditProfilePass,
            BundleUtils().getBundleTab(1)
        )
    }

    private fun navigateToEditProfileName() {
        findNavController().navigate(
            R.id.action_traineeProfileFragment_to_traineeEditProfileName,
            BundleUtils().getBundleTab(1)
        )
    }

    override fun refresh() {
        homeTraineeViewModel.refresh.observe(viewLifecycleOwner) {
            if (it) homeTraineeViewModel.updateProfile()
        }
    }
}
