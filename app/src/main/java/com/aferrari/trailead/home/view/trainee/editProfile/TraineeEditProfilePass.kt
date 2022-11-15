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
import com.aferrari.trailead.databinding.TraineeEditProfilePasswordFragmentBinding
import com.aferrari.trailead.home.viewmodel.HomeTraineeViewModel

class TraineeEditProfilePass : Fragment() {

    private lateinit var binding: TraineeEditProfilePasswordFragmentBinding

    private val traineeViewModel: HomeTraineeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.trainee_edit_profile_password_fragment,
                container,
                false
            )
        binding.lifecycleOwner = this
        binding.traineeViewModel = traineeViewModel
        initListeners()
        return binding.root
    }

    private fun initListeners() {
        binding.traineeEditProfilePasswordToolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_traineeEditProfilePass_to_traineeProfileFragment)
        }
        binding.editProfilePasswordButton.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Su contraseña fue actualizada correctamente",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_traineeEditProfilePass_to_traineeProfileFragment)
        }
    }
}