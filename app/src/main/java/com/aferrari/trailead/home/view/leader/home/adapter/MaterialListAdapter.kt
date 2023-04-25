package com.aferrari.trailead.home.view.leader.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.aferrari.components.TraileadPopupMenu
import com.aferrari.login.data.material.YouTubeVideo
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.ItemMaterialBinding
import com.aferrari.trailead.home.viewmodel.IMaterial
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback


class MaterialListAdapter(
    private val dataSet: List<YouTubeVideo>,
    private val fragment: Fragment,
    private val viewModel: IMaterial,
    private val isEditable: Boolean = false
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
                .create(getMenuPopUp(), R.color.primaryColor)
                .setOnClickListener { item -> popupListener(item, material) }
                .show()
        }
        configureSettingEditable()
    }

    private fun configureSettingEditable() {
        binding.imageSettingMaterialLeader.visibility = if (isEditable)
            View.VISIBLE
        else
            View.GONE
    }

    private fun getMenuPopUp() =
        if (isEditable) R.menu.menu_item_abm_material else R.menu.menu_item_m_material

    private fun popupListener(item: MenuItem?, youTubeVideo: YouTubeVideo): Boolean {
        viewModel.setSelectedMaterial(youTubeVideo)
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