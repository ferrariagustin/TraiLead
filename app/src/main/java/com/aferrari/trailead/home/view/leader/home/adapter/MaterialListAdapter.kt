package com.aferrari.trailead.home.view.leader.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.aferrari.login.db.Material
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.MaterialItemBinding
import com.aferrari.trailead.home.viewmodel.HomeLeaderViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback

class MaterialListAdapter(
    private val dataSet: MutableList<Material>,
    private val fragment: Fragment,
    private val viewModel: HomeLeaderViewModel
) :
    RecyclerView.Adapter<MaterialListAdapter.MaterialListViewHolder>() {

    private lateinit var binding: MaterialItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialListViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.material_item,
            parent,
            false
        )
        return MaterialListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MaterialListViewHolder, position: Int) {
        val material = dataSet[position]
        fragment.lifecycle.addObserver(holder.viewHolderBinding.leaderMaterialYoutubeId)
        holder.viewHolderBinding.leaderMaterialYoutubeId.getYouTubePlayerWhenReady(object :
            YouTubePlayerCallback {
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                youTubePlayer.cueVideo(material.url, 0f)
            }
        })
        holder.viewHolderBinding.imageFullScreenMaterialLeader.setOnClickListener {
            viewModel.materialSelected = material
            fragment.findNavController().navigate(R.id.fullScreenVideo)
        }
    }

    override fun getItemCount(): Int = dataSet.size

    class MaterialListViewHolder(binding: MaterialItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val viewHolderBinding: MaterialItemBinding = binding
    }
}