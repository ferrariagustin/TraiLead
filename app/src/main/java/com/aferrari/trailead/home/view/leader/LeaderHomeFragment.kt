package com.aferrari.trailead.home.view.leader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.LeaderHomeFragmentBinding
import com.aferrari.trailead.home.viewmodel.HomeLeaderViewModel


class LeaderHomeFragment : Fragment() {

    private lateinit var binding: LeaderHomeFragmentBinding

    private val homeLeaderViewModel: HomeLeaderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.leader_home_fragment, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

}