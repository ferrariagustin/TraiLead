package com.aferrari.trailead.home.view.leader

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.aferrari.login.session.SessionManagement
import com.aferrari.login.utils.StringUtils
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
        binding.homeLeaderViewModel = homeLeaderViewModel
        initListeners()
        return binding.root
    }

    private fun initListeners() {
        binding.leaderHomeToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.close_session_toolbar_menu -> logout()
            }
            return@setOnMenuItemClickListener true
        }
    }

    private fun logout() {
        SessionManagement(requireContext()).removeSession()
        goLogin()
    }

    private fun goLogin() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(StringUtils.DEEPLINK_LOGIN))
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

}