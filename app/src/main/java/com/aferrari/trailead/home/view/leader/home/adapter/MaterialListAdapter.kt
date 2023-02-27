package com.aferrari.trailead.home.view.leader.home.adapter

import android.annotation.SuppressLint
import android.util.TypedValue
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.aferrari.login.db.Material
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.MaterialItemBinding
import com.aferrari.trailead.home.viewmodel.leader.HomeLeaderViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback


class MaterialListAdapter(
    private val dataSet: List<Material>,
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

    @SuppressLint("RestrictedApi")
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
        holder.viewHolderBinding.imageSettingMaterialLeader.setOnClickListener { it ->
            configMenu(it, material)
        }
    }

    private fun configMenu(it: View, material: Material) {
        val popupMenu = PopupMenu(fragment.requireContext(), it)
        fragment.requireActivity().menuInflater.inflate(
            R.menu.menu_item_abm_material,
            popupMenu.menu
        )
        popupMenu.setForceShowIcon(true)
        configStyleMenu(popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            manageItemClick(menuItem, material)
        }
        popupMenu.show()
    }

    private fun configStyleMenu(menu: Menu) {
        menu.children.forEach {
            val typedValue = TypedValue()
            fragment.activity?.theme?.resolveAttribute(androidx.appcompat.R.attr.colorPrimary, typedValue, true)
            it.icon.setTint(typedValue.data)
        }
    }

    private fun manageItemClick(menuItem: MenuItem, material: Material): Boolean {
        viewModel.setSelectedMaterial(material)
        when (menuItem.itemId) {
            R.id.material_full_screen -> navigateToFullScreenVideo()
            R.id.material_delete -> navigateToDeleteMaterial()
            R.id.material_edit -> navigateToEditMaterial()
            else -> return false
        }
        return true
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

    class MaterialListViewHolder(binding: MaterialItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val viewHolderBinding: MaterialItemBinding = binding
    }
}