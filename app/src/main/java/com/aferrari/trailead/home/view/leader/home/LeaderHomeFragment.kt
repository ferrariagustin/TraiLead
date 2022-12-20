package com.aferrari.trailead.home.view.leader.home

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aferrari.login.session.SessionManagement
import com.aferrari.login.utils.StringUtils
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.LeaderHomeFragmentBinding
import com.aferrari.trailead.home.view.leader.home.adapter.MaterialListAdapter
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

    @SuppressLint("SetJavaScriptEnabled")
    private fun initListeners() {
        binding.leaderHomeToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.close_session_toolbar_menu -> logout()
            }
            return@setOnMenuItemClickListener true
        }
        binding.recyclerViewCategoryList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        homeLeaderViewModel.getMaterialCategory()
        homeLeaderViewModel.listMaterialCategory.observe(viewLifecycleOwner) {
            binding.recyclerViewCategoryList.adapter =
                MaterialListAdapter(it)
        }
//        lifecycle.addObserver(binding.leaderMaterialYoutubeId)
//        binding.leaderMaterialYoutubeId.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
//            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
//                youTubePlayer.cueVideo("Fe57a6qpoi0", 0f)
//            }
//        })
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