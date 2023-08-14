package com.aferrari.trailead.app.ui.leader.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aferrari.trailead.R
import com.aferrari.trailead.app.ui.RefreshListener
import com.aferrari.trailead.app.viewmodel.leader.LeaderViewModel
import com.aferrari.trailead.databinding.LeaderProfileFragmentBinding

class LeaderProfileFragment : Fragment(), RefreshListener {
    private lateinit var binding: LeaderProfileFragmentBinding

    private val leaderViewModel: LeaderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.leader_profile_fragment, container, false)
        binding.lifecycleOwner = this
        binding.homeLeaderViewModel = leaderViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initListener() {
        binding.leaderImageViewEditCompleteNameIcon.setOnClickListener {
            findNavController().navigate(R.id.action_leaderProfileFragment_to_leaderEditNameFragment)
        }
        binding.leaderImageViewEditPasswordIcon.setOnClickListener {
            findNavController().navigate(R.id.action_leaderProfileFragment_to_leaderEditPassFragment)
        }
        refresh()
    }

    override fun refresh() {
        leaderViewModel.refresh.observe(viewLifecycleOwner) {
            if (it) leaderViewModel.updateProfile()
        }
    }
}