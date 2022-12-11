package com.aferrari.trailead.home.view.trainee.editProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.aferrari.login.dialog.Dialog
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.TraineeEditProfilePasswordFragmentBinding
import com.aferrari.trailead.home.viewmodel.HomeTraineeViewModel
import com.aferrari.trailead.home.viewmodel.StatusUpdateInformation

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponent()
        initListeners()
    }

    private fun initComponent() {
        traineeViewModel.init()
    }

    private fun initListeners() {
        binding.traineeEditProfilePasswordToolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_traineeEditProfilePass_to_traineeProfileFragment)
        }
        binding.editProfilePasswordButton.setOnClickListener {
            traineeViewModel.updatePassword(
                binding.editProfilePasswordInputText.text.toString(),
                binding.editProfilePasswordRepeatInputText.text.toString(),
            )
        }
        traineeViewModel.statusEditProfilePass.observe(viewLifecycleOwner) {
            when (it.name) {
                StatusUpdateInformation.FAILED.name -> showError()
                StatusUpdateInformation.SUCCESS.name -> showSuccess()
            }
        }
    }

    private fun showSuccess() {
        Toast.makeText(
            requireContext(),
            "Su contraseña fue actualizada correctamente",
            Toast.LENGTH_SHORT
        ).show()
        findNavController().navigate(R.id.action_traineeEditProfilePass_to_traineeProfileFragment)
    }

    private fun showError() {
        Dialog().showDialog(
            resources.getString(com.aferrari.login.R.string.title_error),
            "Ingrese una contraseña correcta",
            requireContext()
        )
    }
}