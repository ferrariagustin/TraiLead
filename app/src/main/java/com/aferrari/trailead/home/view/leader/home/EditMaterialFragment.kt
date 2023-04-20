package com.aferrari.trailead.home.view.leader.home

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
import com.aferrari.trailead.databinding.LeaderEditMaterialBinding
import com.aferrari.trailead.home.viewmodel.StatusUpdateInformation
import com.aferrari.trailead.home.viewmodel.leader.LeaderViewModel
import com.aferrari.trailead.home.viewmodel.leader.material.EditMaterialViewModel

class EditMaterialFragment : Fragment() {

    private lateinit var binding: LeaderEditMaterialBinding

    private lateinit var viewModel: EditMaterialViewModel

    private val leaderViewModel: LeaderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.leader_edit_material, container, false)
        binding.lifecycleOwner = this
        viewModel = EditMaterialViewModel(leaderViewModel)
        binding.viewModel = viewModel
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        binding.editMaterialToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.saveEditMaterialButton.setOnClickListener {
            viewModel.editMaterial(
                binding.editMaterialUrlTextInput.text.toString(),
                binding.editMaterialTitleTextInput.text.toString()
            )
        }
        viewModel.statusUpdateEditMaterial.observe(viewLifecycleOwner) {
            when (it) {
                StatusUpdateInformation.SUCCESS -> {
                    successFlow()
                }
                StatusUpdateInformation.FAILED -> {
                    failedFlow()
                }
                else -> {}
            }
        }
    }

    private fun failedFlow() {
        Toast.makeText(requireContext(), "Ingrese una URL válida", Toast.LENGTH_SHORT).show()
    }

    private fun successFlow() {
        findNavController().navigateUp()
    }
}
