package com.aferrari.trailead.app.ui.leader.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aferrari.trailead.R
import com.aferrari.trailead.app.viewmodel.leader.LeaderViewModel
import com.aferrari.trailead.databinding.PdfFragmentBinding

class PdfFragment : Fragment() {

    private lateinit var binding: PdfFragmentBinding
    private val viewModel: LeaderViewModel by activityViewModels()
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            binding.pdfView.fromUri(it).load()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.pdf_fragment, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        binding.searchPdfFloatButton.setOnClickListener {
            launcher.launch("application/pdf")
        }
    }

    private fun setupToolbar() {
        binding.pdfToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
    }
}