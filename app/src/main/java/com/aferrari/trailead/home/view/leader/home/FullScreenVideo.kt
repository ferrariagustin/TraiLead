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
import com.aferrari.trailead.databinding.FullScreenVideoBinding
import com.aferrari.trailead.home.viewmodel.leader.LeaderViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback

class FullScreenVideo : Fragment() {

    private lateinit var binding: FullScreenVideoBinding

    private val leaderViewModel: LeaderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.full_screen_video, container, false)
        binding.lifecycleOwner = this
        binding.homeLeaderViewModel = leaderViewModel
        goneBottomNavigation()
        initFullScreenVideo()
        return binding.root
    }

    private fun goneBottomNavigation() {
        leaderViewModel.setBottomNavigationVisibility(View.INVISIBLE)
    }

    private fun initFullScreenVideo() {
        lifecycle.addObserver(binding.fullScreenMaterialYoutubeId)
        binding.fullScreenMaterialYoutubeId.getYouTubePlayerWhenReady(object :
            YouTubePlayerCallback {
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                youTubePlayer.cueVideo(leaderViewModel.materialSelected!!.url, 0f)
            }
        })
        binding.fullScreenMaterialYoutubeId.enterFullScreen()
        binding.fullScreenToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}