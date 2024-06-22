package com.aferrari.trailead.app.ui.leader.home.adapter

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
import com.aferrari.trailead.common.common_enum.StatusUpdateInformation
import com.aferrari.trailead.common.ui.TraiLeadSnackbar
import com.aferrari.trailead.databinding.LeaderEditMaterialPdfBinding

class EditPdfFragment : Fragment() {

    private lateinit var _binding: LeaderEditMaterialPdfBinding
    val binding: LeaderEditMaterialPdfBinding get() = _binding

    private val viewModel: LeaderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.leader_edit_material_pdf, container, false)
        _binding.lifecycleOwner = viewLifecycleOwner
        _binding.viewModel = viewModel
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setStateRefreshingScreen(false)
        binding.editPdfMaterialToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.saveEditPdfMaterialButton.setOnClickListener {
            viewModel.updatePdf(binding.editPdfMaterialTitleTextInput.text.toString())
        }
        viewModel.statusUpdatePdf.observe(viewLifecycleOwner) {
            when (it) {
                StatusUpdateInformation.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    TraiLeadSnackbar().success(requireContext(), binding.root)
                    findNavController().navigateUp()
                }

                StatusUpdateInformation.FAILED -> {
                    binding.progressBar.visibility = View.GONE
                    TraiLeadSnackbar().error(requireContext(), binding.root)
                }

                StatusUpdateInformation.INTERNET_CONECTION -> {
                    binding.progressBar.visibility = View.GONE
                    TraiLeadSnackbar().errorConection(requireContext(), binding.root)
                }

                StatusUpdateInformation.LOADING -> {
                    showProgressBar()
                }

                else -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            viewModel.resetPdfStatus()
        }
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }
}