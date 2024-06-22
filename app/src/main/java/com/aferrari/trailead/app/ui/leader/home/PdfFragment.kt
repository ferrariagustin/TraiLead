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
import com.aferrari.trailead.common.StringUtils.PDF_KEY
import com.aferrari.trailead.common.common_enum.StatusUpdateInformation
import com.aferrari.trailead.common.ui.TraiLeadSnackbar
import com.aferrari.trailead.databinding.PdfLeaderFragmentBinding
import com.aferrari.trailead.domain.models.Pdf

class PdfFragment : Fragment() {

    private lateinit var binding: PdfLeaderFragmentBinding
    private val viewModel: LeaderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.pdf_leader_fragment, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        arguments?.getSerializable(PDF_KEY).let {
            viewModel.restorePdf(it as? Pdf)
        }
        viewModel.setStateRefreshingScreen(false)
        viewModel.statusUpdatePdf.observe(viewLifecycleOwner) {
            when (it) {
                StatusUpdateInformation.LOADING -> {
                    binding.pdfProgressBar.visibility = View.VISIBLE
                }

                StatusUpdateInformation.SUCCESS -> {
                    binding.pdfProgressBar.visibility = View.GONE
                    showPdf()
                }

                StatusUpdateInformation.FAILED -> {
                    binding.pdfProgressBar.visibility = View.GONE
                    TraiLeadSnackbar().error(requireContext(), binding.root)
                }

                StatusUpdateInformation.INTERNET_CONECTION -> {
                    binding.pdfProgressBar.visibility = View.GONE
                }

                else -> Unit
            }
        }
    }

    private fun showPdf() {
        binding.pdfView.fromFile(viewModel.pdfFileSelected).load()
    }

    private fun setupToolbar() {
        binding.pdfToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
    }
}