package com.aferrari.trailead.app.ui.trainee.profile

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
import com.aferrari.trailead.app.viewmodel.trainee.TraineeViewModel
import com.aferrari.trailead.common.common_enum.ErrorView
import com.aferrari.trailead.common.common_enum.StatusUpdateInformation
import com.aferrari.trailead.common.ui.TraiLeadSnackbar
import com.aferrari.trailead.common.ui.TraileadDialog
import com.aferrari.trailead.databinding.EditProfilePassFragmentBinding

class TraineeEditProfilePass : Fragment() {

    private lateinit var binding: EditProfilePassFragmentBinding

    private val traineeViewModel: TraineeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.edit_profile_pass_fragment,
                container,
                false
            )
        binding.lifecycleOwner = this
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
        binding.editProfilePasswordToolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_traineeEditProfilePass_to_traineeProfileFragment)
        }
        binding.editProfilePasswordButton.setOnClickListener {
            traineeViewModel.updatePassword(
                binding.editProfilePasswordInputText.text.toString(),
                binding.editProfilePasswordRepeatInputText.text.toString(),
            )
        }
        traineeViewModel.statusEditProfilePass.observe(viewLifecycleOwner) {
            when (it) {
                StatusUpdateInformation.FAILED -> showError()
                StatusUpdateInformation.SUCCESS -> showSuccess()
                StatusUpdateInformation.INTERNET_CONECTION -> showFailedInternetConnection()
                else -> {}
            }
        }
    }

    private fun showFailedInternetConnection() {
        TraiLeadSnackbar().errorConection(requireContext(), binding.root)
    }

    private fun showSuccess() {
        TraiLeadSnackbar().init(
            requireContext(),
            binding.root,
            resources.getString(R.string.update_pass_success), type = ErrorView.SUCCESS
        )
        findNavController().navigate(R.id.action_traineeEditProfilePass_to_traineeProfileFragment)
    }

    private fun showError() {
        TraileadDialog().showDialog(
            resources.getString(R.string.title_error),
            resources.getString(R.string.update_pass_failed_input),
            requireContext()
        )
    }
}