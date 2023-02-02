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
import com.aferrari.trailead.databinding.LeaderAddMaterialBinding
import com.aferrari.trailead.home.viewmodel.HomeLeaderViewModel
import com.aferrari.trailead.home.viewmodel.StatusUpdateInformation

class AddMaterialFragment : Fragment() {

    private lateinit var binding: LeaderAddMaterialBinding

    private val homeLeaderViewModel: HomeLeaderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.leader_add_material,
                container,
                false
            )
        binding.lifecycleOwner = this
        binding.homeLeaderViewModel = homeLeaderViewModel
        initListeners()
        return binding.root
    }

    private fun initListeners() {
        binding.addNewMaterialToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.addNewMaterialButton.setOnClickListener {
            homeLeaderViewModel.insertMaterial(
                binding.addNewMaterialTitleTextInput.text.toString(),
                binding.addNewMaterialTextInput.text.toString()
            )
        }
        homeLeaderViewModel.statusUpdateNewMaterial.observe(viewLifecycleOwner) {
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
        Toast.makeText(requireContext(), "Ingrese un link válido", Toast.LENGTH_SHORT).show()
    }
}