package com.aferrari.trailead.home.view.leader.listTrainee.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.aferrari.login.db.Material
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.ItemMaterialBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback

class ConfigMaterialTraineeAdapter(
    private val dataSource: List<Material>,
    private val fragment: Fragment
) :
    RecyclerView.Adapter<ConfigMaterialTraineeAdapter.ConfigMaterialTraineeViewHolder>() {

    private lateinit var binding: ItemMaterialBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ConfigMaterialTraineeViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_material,
            parent,
            false
        )
        return ConfigMaterialTraineeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ConfigMaterialTraineeViewHolder, position: Int) {
        val material = dataSource[position]
        fragment.lifecycle.addObserver(holder.viewHolderBinding.leaderMaterialYoutubeId)
        holder.viewHolderBinding.leaderMaterialYoutubeId.getYouTubePlayerWhenReady(object :
            YouTubePlayerCallback {
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                youTubePlayer.cueVideo(material.url, 0f)
            }
        })
        holder.viewHolderBinding.titleMaterialItem.hint = material.title
        holder.viewHolderBinding.imageSettingMaterialLeader.setOnClickListener { it ->
//            configMenu(it, material)
        }
    }

    override fun getItemCount(): Int = dataSource.size

    class ConfigMaterialTraineeViewHolder(var viewHolderBinding: ItemMaterialBinding) :
        RecyclerView.ViewHolder(viewHolderBinding.root)
}