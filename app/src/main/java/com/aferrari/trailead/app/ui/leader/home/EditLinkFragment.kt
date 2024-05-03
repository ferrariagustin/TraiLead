package com.aferrari.trailead.app.ui.leader.home

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
import com.aferrari.trailead.app.viewmodel.leader.material.LinkMaterialViewModel
import com.aferrari.trailead.common.common_enum.StatusUpdateInformation
import com.aferrari.trailead.common.ui.TraiLeadSnackbar
import com.aferrari.trailead.common.ui.TraileadDialog
import com.aferrari.trailead.databinding.LeaderEditMaterialLinkBinding

class EditLinkFragment : Fragment() {

    private lateinit var _binding: LeaderEditMaterialLinkBinding

    private lateinit var viewModel: LinkMaterialViewModel

    private val leaderViewModel: LeaderViewModel by activityViewModels()

    val binding: LeaderEditMaterialLinkBinding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.leader_edit_material_link, container, false)
        viewModel = LinkMaterialViewModel(leaderViewModel)
        _binding.lifecycleOwner = viewLifecycleOwner
        _binding.viewModel = viewModel

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editLinkMaterialToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.saveEditLinkMaterialButton.setOnClickListener {
            viewModel.updateLink(
                binding.editLinkMaterialTitleTextInput.text.toString(),
                binding.editLinkMaterialUrlTextInput.text.toString()
            )
        }
        viewModel.status.observe(viewLifecycleOwner) {
            when (it) {
                StatusUpdateInformation.SUCCESS -> navigateToSuccessFlow()
                StatusUpdateInformation.FAILED -> navigateToFailedFlow()
                StatusUpdateInformation.INTERNET_CONECTION -> navigateToFailedInternetContectionFlow()
                else -> {}
            }
        }
    }

    private fun navigateToFailedInternetContectionFlow() {
        TraiLeadSnackbar().errorConection(requireContext(), binding.root)
    }

    private fun navigateToFailedFlow() {
        TraileadDialog().showDialog(
            resources.getString(R.string.error),
            resources.getString(R.string.error_invalid_link),
            requireContext()
        )
    }

    private fun navigateToSuccessFlow() {
        findNavController().navigateUp()
    }
}