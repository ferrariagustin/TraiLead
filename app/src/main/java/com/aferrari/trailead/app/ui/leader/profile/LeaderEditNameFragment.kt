package com.aferrari.trailead.app.ui.leader.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.LeaderEditProfileNameFragmentBinding
import com.aferrari.trailead.app.viewmodel.leader.LeaderViewModel

class LeaderEditNameFragment : Fragment() {

    private lateinit var binding: LeaderEditProfileNameFragmentBinding

    private val viewModel: LeaderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.leader_edit_profile_name_fragment,
                container,
                false
            )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        initListeners()
        return binding.root
    }

    private fun initListeners() {
        binding.leaderEditProfileNameToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.leaderEditProfileNameButton.setOnClickListener {
            viewModel.updateInformation(
                binding.leaderEditProfileNameInputText.text.toString(),
                binding.leaderEditProfileLastNameInputText.text.toString()
            )
            findNavController().navigateUp()
        }
    }
}