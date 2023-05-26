package com.aferrari.trailead.app.ui.trainee.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.TraineeEditProfileNameFragmentBinding
import com.aferrari.trailead.app.viewmodel.trainee.TraineeViewModel

class TraineeEditProfileName : Fragment() {

    private lateinit var binding: TraineeEditProfileNameFragmentBinding

    private val traineeViewModel: TraineeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.trainee_edit_profile_name_fragment,
                container,
                false
            )
        binding.lifecycleOwner = this
        binding.traineeViewModel = traineeViewModel
        initListeners()
        return binding.root
    }

    private fun initListeners() {
        binding.traineeEditProfileNameToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.editProfileNameButton.setOnClickListener {
            traineeViewModel.updateInformation(
                binding.editProfileNameInputText.text.toString(),
                binding.editProfileLastNameInputText.text.toString()
            )
            findNavController().navigateUp()
        }
    }
}