package com.aferrari.trailead.app.ui.leader.home

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
import com.aferrari.trailead.app.viewmodel.leader.LeaderViewModel
import com.aferrari.trailead.databinding.AddPdfFragmentBinding

class AddPdfFragment : Fragment() {

    private lateinit var binding: AddPdfFragmentBinding
    private val viewModel: LeaderViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.add_pdf_fragment,
            container,
            false
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        binding.savePdfButton.setOnClickListener {
            Toast.makeText(requireContext(), "TODO: PDF saved", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
    }

    private fun setupToolbar() {
        binding.addPdfToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
    }
}