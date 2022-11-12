package com.aferrari.trailead.home.view.trainee.editProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.TraineeEditProfileNameFragmentBinding
import com.aferrari.trailead.home.viewmodel.HomeTraineeViewModel

class TraineeEditProfileName : Fragment() {

    private lateinit var binding: TraineeEditProfileNameFragmentBinding

    private val traineeViewModel: HomeTraineeViewModel by activityViewModels()

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
            findNavController().navigate(R.id.action_traineeEditProfileName_to_traineeProfileFragment)
        }
        binding.editProfileNameButton.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Su perfil fue actualizado correctamente",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}