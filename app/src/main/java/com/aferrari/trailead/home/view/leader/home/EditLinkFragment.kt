package com.aferrari.trailead.home.view.leader.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.LeaderAddMaterialLinkBinding
import com.aferrari.trailead.databinding.LeaderEditMaterialLinkBinding
import com.aferrari.trailead.home.viewmodel.leader.LeaderViewModel
import com.aferrari.trailead.home.viewmodel.leader.material.LinkMaterialViewModel

class EditLinkFragment : Fragment() {

    private lateinit var _binding: LeaderEditMaterialLinkBinding

    private lateinit var viewModel: LinkMaterialViewModel

    private val leaderViewModel: LeaderViewModel by activityViewModels()

    val binding: LeaderEditMaterialLinkBinding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.leader_edit_material_link, container, false)
        viewModel = LinkMaterialViewModel(leaderViewModel)
        _binding.lifecycleOwner = viewLifecycleOwner
        _binding.viewModel = viewModel

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editLinkMaterialToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.saveEditLinkMaterialButton.setOnClickListener {
            // TODO: Save Link edited
            findNavController().navigateUp()
        }
    }
}