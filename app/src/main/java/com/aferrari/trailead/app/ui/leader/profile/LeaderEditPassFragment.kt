package com.aferrari.trailead.app.ui.leader.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aferrari.trailead.R
import com.aferrari.trailead.app.viewmodel.leader.LeaderViewModel
import com.aferrari.trailead.common.common_enum.ErrorView
import com.aferrari.trailead.common.common_enum.StatusUpdateInformation
import com.aferrari.trailead.common.ui.TraiLeadSnackbar
import com.aferrari.trailead.common.ui.TraileadDialog
import com.aferrari.trailead.databinding.EditProfilePassFragmentBinding

class LeaderEditPassFragment : Fragment() {

    private lateinit var binding: EditProfilePassFragmentBinding

    private val viewModel: LeaderViewModel by activityViewModels()

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
        viewModel.init()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        binding.editProfilePasswordToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.editProfilePasswordButton.setOnClickListener {
            viewModel.updatePassword(
                binding.editProfilePasswordInputText.text.toString(),
                binding.editProfilePasswordRepeatInputText.text.toString()
            )
        }
        viewModel.statusUpdatePassword.observe(viewLifecycleOwner) {
            when (it) {
                StatusUpdateInformation.SUCCESS -> successUpdated()
                StatusUpdateInformation.FAILED -> failedUpdated()
                StatusUpdateInformation.INTERNET_CONECTION -> failedInternetContection()
                else -> {}
            }
        }
    }

    private fun failedInternetContection() {
        TraiLeadSnackbar().errorConection(requireContext(), binding.root)
    }

    private fun failedUpdated() {
        TraileadDialog().showDialog(
            resources.getString(R.string.title_error),
            resources.getString(R.string.update_pass_failed_input),
            requireContext()
        )
    }

    private fun successUpdated() {
        TraiLeadSnackbar().init(
            requireContext(),
            binding.root,
            resources.getString(R.string.update_pass_success), type = ErrorView.SUCCESS
        )
        findNavController().navigateUp()
    }
}