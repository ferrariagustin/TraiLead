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
import com.aferrari.trailead.databinding.LeaderAddMaterialCategoryBinding
import com.aferrari.trailead.home.viewmodel.HomeLeaderViewModel
import com.aferrari.trailead.home.viewmodel.StatusUpdateInformation

class AddCategoryFragment : Fragment() {

    private lateinit var binding: LeaderAddMaterialCategoryBinding

    private val homeLeaderViewModel: HomeLeaderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.leader_add_material_category,
                container,
                false
            )
        binding.lifecycleOwner = this
        binding.viewModel = homeLeaderViewModel
        initListeners()
        return binding.root
    }

    private fun initListeners() {
        binding.addNewCategoryToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.addNewCategoryButton.setOnClickListener {
            homeLeaderViewModel.addCategory(binding.addNewCategoryTextInput.text.toString())
        }
        homeLeaderViewModel.statusUpdateNewCategory.observe(viewLifecycleOwner) {
            when (it) {
                StatusUpdateInformation.FAILED -> {
                    failedFlow()
                }
                StatusUpdateInformation.SUCCESS -> {
                    successFlow()
                }
                else -> {}
            }
        }
    }

    private fun successFlow() {
        findNavController().navigateUp()
    }

    private fun failedFlow() {
        Toast.makeText(requireContext(), "Ingrese una categoría válida", Toast.LENGTH_SHORT).show()
    }
}