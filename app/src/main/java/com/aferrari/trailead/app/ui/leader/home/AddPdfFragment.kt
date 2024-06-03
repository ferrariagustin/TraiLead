package com.aferrari.trailead.app.ui.leader.home

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aferrari.trailead.R
import com.aferrari.trailead.app.viewmodel.leader.LeaderViewModel
import com.aferrari.trailead.common.common_enum.StatusUpdateInformation
import com.aferrari.trailead.common.ui.TraiLeadSnackbar
import com.aferrari.trailead.common.ui.TraileadDialog
import com.aferrari.trailead.databinding.AddPdfFragmentBinding

class AddPdfFragment : Fragment() {

    private lateinit var binding: AddPdfFragmentBinding
    private val viewModel: LeaderViewModel by activityViewModels()

    private val selectedPdfCallback =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let {
                    viewModel.selectedPdfUri(it)
                    binding.pdfView.fromUri(it).load()
                    binding.pdfFilePathTextview.hint =
                        DocumentFile.fromSingleUri(requireContext(), it)?.name
                }
                return@registerForActivityResult
            }
        }

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
            viewModel.savePdf(binding.pdfTitleIdTextviewTextInput.text.toString())
            binding.pdfFilePathTextview.hint = getString(R.string.do_not_file_selected)
        }
        binding.searchPdfFileButton.setOnClickListener {
            openDocumentPDF()
        }
        viewModel.statusUpdatePdf.observe(viewLifecycleOwner) {
            when (it) {
                StatusUpdateInformation.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                StatusUpdateInformation.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    TraiLeadSnackbar().success(requireContext(), binding.root)
                    findNavController().navigateUp()
                }

                StatusUpdateInformation.FAILED -> {
                    binding.progressBar.visibility = View.GONE
                    TraileadDialog().showDialog(
                        getString(R.string.error),
                        getString(R.string.show_error_save_pdf),
                        requireContext()
                    )
                }

                StatusUpdateInformation.INTERNET_CONECTION -> {
                    binding.progressBar.visibility = View.GONE
                    TraiLeadSnackbar().errorConection(requireContext(), binding.root)
                }

                else -> Unit
            }
        }
    }

    private fun openDocumentPDF() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.setType("application/pdf")
        selectedPdfCallback.launch(intent)
    }

    private fun setupToolbar() {
        binding.addPdfToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
    }
}