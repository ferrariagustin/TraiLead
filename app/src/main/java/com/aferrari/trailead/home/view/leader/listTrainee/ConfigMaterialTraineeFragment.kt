package com.aferrari.trailead.home.view.leader.listTrainee

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.LeaderConfigMaterialTraineeFragmentBinding
import com.aferrari.trailead.home.view.leader.listTrainee.adapter.ConfigMaterialTraineeAdapter
import com.aferrari.trailead.home.viewmodel.leader.HomeLeaderViewModel
import com.aferrari.trailead.home.viewmodel.leader.listTrainee.ListTraineeViewModel

class ConfigMaterialTraineeFragment : Fragment() {

    private lateinit var binding: LeaderConfigMaterialTraineeFragmentBinding
    private lateinit var viewModel: ListTraineeViewModel
    private val homeLeaderViewModel: HomeLeaderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.leader_config_material_trainee_fragment,
            container,
            false
        )
        viewModel = ListTraineeViewModel(homeLeaderViewModel)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        binding.recyclerViewConfigMaterialCategoryToTrainee.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        homeLeaderViewModel.listMaterialLeader.observe(viewLifecycleOwner) {
            binding.recyclerViewConfigMaterialCategoryToTrainee.adapter =
                ConfigMaterialTraineeAdapter(it, this)
        }
    }
}