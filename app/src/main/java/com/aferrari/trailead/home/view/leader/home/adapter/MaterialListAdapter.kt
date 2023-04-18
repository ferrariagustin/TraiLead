package com.aferrari.trailead.home.view.leader.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.aferrari.components.TraileadPopupMenu
import com.aferrari.login.data.Material
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.ItemMaterialBinding
import com.aferrari.trailead.home.viewmodel.leader.LeaderViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback


class MaterialListAdapter(
    private val dataSet: List<Material>,
    private val fragment: Fragment,
    private val viewModel: LeaderViewModel
) :
    RecyclerView.Adapter<MaterialListAdapter.MaterialListViewHolder>() {

    private lateinit var binding: ItemMaterialBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialListViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_material,
            parent,
            false
        )
        return MaterialListViewHolder(binding)
    }

    @SuppressLint("RestrictedApi", "ResourceType")
    override fun onBindViewHolder(holder: MaterialListViewHolder, position: Int) {
        val material = dataSet[position]
        fragment.lifecycle.addObserver(holder.viewHolderBinding.leaderMaterialYoutubeId)
        holder.viewHolderBinding.leaderMaterialYoutubeId.getYouTubePlayerWhenReady(object :
            YouTubePlayerCallback {
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                youTubePlayer.cueVideo(material.url, 0f)
            }
        })
        holder.viewHolderBinding.titleMaterialItem.hint = material.title
        holder.viewHolderBinding.imageSettingMaterialLeader.setOnClickListener {
            TraileadPopupMenu(it, fragment)
                .create(R.menu.menu_item_abm_material, R.color.primaryColor)
                .setOnClickListener { item -> popupListener(item, material) }
                .show()
        }
    }

    private fun popupListener(item: MenuItem?, material: Material): Boolean {
        viewModel.setSelectedMaterial(material)
        when (item?.itemId) {
            R.id.material_full_screen -> navigateToFullScreenVideo()
            R.id.material_delete -> navigateToDeleteMaterial()
            R.id.material_edit -> navigateToEditMaterial()
            else -> return true
        }
        return false
    }

    private fun navigateToEditMaterial() {
        fragment.findNavController().navigate(R.id.editMaterialFragment)
    }

    private fun navigateToDeleteMaterial() {
        viewModel.deleteMaterialSelected()
    }

    private fun navigateToFullScreenVideo() {
        fragment.findNavController().navigate(R.id.fullScreenVideo)
    }

    override fun getItemCount(): Int = dataSet.size

    class MaterialListViewHolder(binding: ItemMaterialBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val viewHolderBinding: ItemMaterialBinding = binding
    }
}