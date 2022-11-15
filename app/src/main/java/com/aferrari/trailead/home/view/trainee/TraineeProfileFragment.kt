package com.aferrari.trailead.home.view.trainee

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.TraineeProfileFragmentBinding
import com.aferrari.trailead.home.Utils.BundleUtils
import com.aferrari.trailead.home.viewmodel.HomeTraineeViewModel

class TraineeProfileFragment : Fragment() {

    private lateinit var binding: TraineeProfileFragmentBinding
    private val homeTraineeViewModel: HomeTraineeViewModel by activityViewModels()

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

    private fun initListeners() {
        binding.imageViewEditCompleteNameIcon.setOnClickListener {
            navigateToEditProfileName()
        }

        binding.imageViewEditPasswordIcon.setOnClickListener {
            navigateToEditPassword()
        }
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
}
