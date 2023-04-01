package com.aferrari.trailead.home.view.leader.listTrainee

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.LinkMaterialTraineeFragmentBinding
import com.aferrari.trailead.home.view.leader.listTrainee.adapter.SettingsMaterialTraineeAdapter
import com.aferrari.trailead.home.viewmodel.StatusUpdateInformation
import com.aferrari.trailead.home.viewmodel.leader.HomeLeaderViewModel
import com.aferrari.trailead.home.viewmodel.leader.listTrainee.ListTraineeViewModel
import com.google.android.material.radiobutton.MaterialRadioButton


class LinkMaterialTraineeFragment : Fragment() {

    private lateinit var binding: LinkMaterialTraineeFragmentBinding

    private lateinit var viewModel: ListTraineeViewModel

    private val homeLeaderViewModel: HomeLeaderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.link_material_trainee_fragment,
            container,
            false
        )
        viewModel = ListTraineeViewModel(homeLeaderViewModel)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
        configToolbar()
        configRadioButtonSelectedAll()
        initListeners()
    }

    private fun initListeners() {
        viewModel.statusUpdateRadioButtonSelectedAll.observe(viewLifecycleOwner) {
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

    private fun successFlow() {
        // TODO("Not yet implemented")
    }

    private fun failedFlow() {
        // TODO("Not yet implemented")
    }


    private fun configRadioButtonSelectedAll() {
        binding.linkedRadioButtonAll.setOnClickListener {
            if ((it as MaterialRadioButton).isSelected) {
                failedSelectedRadioButtonFlow(it)
            } else {
                successSelectedRadioButtonFlow(it)
            }
        }
    }

    private fun failedSelectedRadioButtonFlow(radioButton: MaterialRadioButton) {
        radioButton.isSelected = false
        radioButton.isChecked = false
    }

    private fun successSelectedRadioButtonFlow(radioButton: MaterialRadioButton) {
        radioButton.isSelected = true
        radioButton.isChecked = true
    }

    private fun configToolbar() {
        binding.settingTraineeMaterialToolbarId.setNavigationOnClickListener() {
            findNavController().navigateUp()
        }
    }

    private fun initComponents() {
        binding.recyclerViewMaterialForCategoryList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewModel.getCategories()?.let {
            binding.recyclerViewMaterialForCategoryList.adapter =
                SettingsMaterialTraineeAdapter(it, this)
        }
    }
}
