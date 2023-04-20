package com.aferrari.trailead.home.view.leader.profile

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.LeaderProfileFragmentBinding
import com.aferrari.trailead.home.viewmodel.leader.LeaderViewModel
import com.aferrari.trailead.home.viewmodel.StatusVisibilityPassword

class LeaderProfileFragment : Fragment() {
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
        binding.leaderImageViewPasswordIcon.setOnClickListener {
            when (leaderViewModel.statusVisibilityPassword) {
                StatusVisibilityPassword.INVISIBLE -> setVisibilityPassword(
                    StatusVisibilityPassword.VISIBLE,
                    resources.getDrawable(
                        R.drawable.ic_unlock,
                        requireContext().theme
                    ), null
                )
                StatusVisibilityPassword.VISIBLE -> setVisibilityPassword(
                    StatusVisibilityPassword.INVISIBLE,
                    resources.getDrawable(R.drawable.ic_lock, requireContext().theme),
                    PasswordTransformationMethod()
                )
            }
        }
    }

    private fun setVisibilityPassword(
        visibilityPassword: StatusVisibilityPassword,
        newIconPass: Drawable,
        passwordTransformationMethod: PasswordTransformationMethod?
    ) {
        leaderViewModel.statusVisibilityPassword = visibilityPassword
        binding.leaderImageViewPasswordIcon.setImageDrawable(newIconPass)
        binding.leaderPasswordTextview.transformationMethod = passwordTransformationMethod
    }
}