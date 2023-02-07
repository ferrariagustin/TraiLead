package com.aferrari.trailead.home.view.leader.home

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
import com.aferrari.trailead.databinding.LeaderMaterialListBinding
import com.aferrari.trailead.home.view.leader.home.adapter.MaterialListAdapter
import com.aferrari.trailead.home.viewmodel.HomeLeaderViewModel

class LeaderMaterialListFragment : Fragment() {

    private lateinit var binding: LeaderMaterialListBinding
    private val homeLeaderViewModel: HomeLeaderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.leader_material_list, container, false)
        binding.lifecycleOwner = this
        binding.homeLeaderViewModel = homeLeaderViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        homeLeaderViewModel.init()
        binding.leaderMaterialListToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.recyclerViewMaterialList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        homeLeaderViewModel.getMaterialsCategoryFilter()
        homeLeaderViewModel.listMaterialLeader.observe(viewLifecycleOwner) {
            binding.recyclerViewMaterialList.adapter =
                MaterialListAdapter(it, this, homeLeaderViewModel)
        }
        binding.addMaterialLeader.setOnClickListener {
            findNavController().navigate(R.id.addMaterialFragment)
        }
    }
}