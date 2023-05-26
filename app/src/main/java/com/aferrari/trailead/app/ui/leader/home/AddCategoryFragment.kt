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
import com.aferrari.trailead.databinding.LeaderAddMaterialCategoryBinding
import com.aferrari.trailead.app.viewmodel.leader.LeaderViewModel
import com.aferrari.trailead.viewmodel.StatusUpdateInformation

/**
 * The leader can add a new category for sort his materials.
 */
class AddCategoryFragment : Fragment() {

    private lateinit var binding: LeaderAddMaterialCategoryBinding

    private val leaderViewModel: LeaderViewModel by activityViewModels()

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
        binding.viewModel = leaderViewModel
        initListeners()
        return binding.root
    }

    private fun initListeners() {
        binding.addNewCategoryToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.addNewCategoryButton.setOnClickListener {
            leaderViewModel.insertCategory(binding.addNewCategoryTextInput.text.toString())
        }
        leaderViewModel.statusUpdateNewCategory.observe(viewLifecycleOwner) {
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
        Toast.makeText(requireContext(), "Ingrese una categoría válida. Recuerde que no puede repertir", Toast.LENGTH_SHORT).show()
    }
}