package com.aferrari.trailead.home.view.leader.profile

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
import com.aferrari.trailead.databinding.LeaderEditProfilePassFragmentBinding
import com.aferrari.trailead.home.viewmodel.HomeLeaderViewModel
import com.aferrari.trailead.home.viewmodel.StatusUpdateInformation

class LeaderEditPassFragment : Fragment() {

    private lateinit var binding: LeaderEditProfilePassFragmentBinding

    private val viewModel: HomeLeaderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.leader_edit_profile_pass_fragment,
                container,
                false
            )
        binding.lifecycleOwner = this
        viewModel.init()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        binding.leaderEditProfilePasswordToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.leaderEditProfilePasswordButton.setOnClickListener {
            viewModel.updatePassword(
                binding.leaderEditProfilePasswordInputText.text.toString(),
                binding.leaderEditProfilePasswordRepeatInputText.text.toString()
            )
        }
        viewModel.statusUpdatePassword.observe(viewLifecycleOwner) {
            when (it) {
                StatusUpdateInformation.SUCCESS -> successUpdated()
                StatusUpdateInformation.FAILED -> failedUpdated()
                else -> {}
            }
        }
    }

    private fun failedUpdated() {
        Toast.makeText(
            requireContext(),
            resources.getString(R.string.update_pass_failed_input),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun successUpdated() {
        Toast.makeText(
            requireContext(),
            resources.getString(R.string.update_pass_success),
            Toast.LENGTH_SHORT
        ).show()
        findNavController().navigateUp()
    }
}