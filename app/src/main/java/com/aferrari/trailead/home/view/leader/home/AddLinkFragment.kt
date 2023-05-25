package com.aferrari.trailead.home.view.leader.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aferrari.login.dialog.TraileadDialog
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.LeaderAddMaterialLinkBinding
import com.aferrari.trailead.home.viewmodel.StatusErrorType
import com.aferrari.trailead.home.viewmodel.StatusUpdateInformation
import com.aferrari.trailead.home.viewmodel.leader.LeaderViewModel
import com.aferrari.trailead.home.viewmodel.leader.material.LinkMaterialViewModel

class AddLinkFragment : Fragment() {

    private lateinit var binding: LeaderAddMaterialLinkBinding

    private lateinit var viewModel: LinkMaterialViewModel

    private val leaderViewModel: LeaderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.leader_add_material_link, container, false)
        binding.lifecycleOwner = this
        viewModel = LinkMaterialViewModel(leaderViewModel)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
        statusListeners()
    }

    private fun statusListeners() {
        viewModel.status.observe(viewLifecycleOwner) {
            when (it) {
                StatusUpdateInformation.SUCCESS -> successFlow()
                StatusUpdateInformation.FAILED -> failedFlow()
                else -> {}
            }
        }
    }

    private fun successFlow() {
        findNavController().navigateUp()
    }

    private fun failedFlow() {
        when (viewModel.errorType) {
            StatusErrorType.EMPTY -> showErrorEmptyField()
            StatusErrorType.INVALID -> showErrorInvalidField()
            else -> {showErrorGeneric()}
        }
    }

    private fun showErrorGeneric() {
        TraileadDialog().showDialog(
            resources.getString(R.string.error),
            resources.getString(R.string.error_generic),
            requireContext()
        )
    }

    private fun showErrorEmptyField() {
        TraileadDialog().showDialog(
            resources.getString(R.string.error),
            resources.getString(R.string.error_empty_field),
            requireContext()
        )
    }

    private fun showErrorInvalidField() {
        TraileadDialog().showDialog(
            resources.getString(R.string.error),
            resources.getString(R.string.error_invalid_link),
            requireContext()
        )
    }

    private fun initComponents() {
        binding.addLinkMaterialToolbar.title = resources.getString(
            R.string.add_new_material,
            LINK
        )
        binding.addLinkMaterialToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
    }

    companion object {
        private const val LINK = "Link"
    }
}