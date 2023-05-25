package com.aferrari.trailead.home.view.leader.home.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.aferrari.components.TraileadPopupMenu
import com.aferrari.login.data.material.Link
import com.aferrari.login.data.material.Pdf
import com.aferrari.login.data.material.YouTubeVideo
import com.aferrari.login.data.material.dao.Material
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.ItemMaterialBinding
import com.aferrari.trailead.home.viewmodel.IMaterial
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback


class MaterialListAdapter(
    private val dataSet: List<Material>,
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

    override fun onBindViewHolder(holder: MaterialListViewHolder, position: Int) {
        goneAllViews(holder)
        when (val material = dataSet[position]) {
            is YouTubeVideo -> {
                bindingYoutubeVideo(holder, material)
            }

            is Link -> {
                bindingLink(holder, material)
            }

            is Pdf -> {
                bindingPdf(holder, material)
            }
        }
    }

    @SuppressLint("ResourceType")
    private fun bindingLink(holder: MaterialListViewHolder, link: Link) {
        holder.viewHolderBinding.linkViewMaterial.root.visibility = VISIBLE
        holder.viewHolderBinding.linkViewMaterial.itemLinkTextView.text = link.title
        holder.viewHolderBinding.linkViewMaterial.itemLinkImageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link.url))
            fragment.startActivity(intent)
        }
        holder.viewHolderBinding.linkViewMaterial.itemLinkSettingImageView.setOnClickListener {
            viewModel.setSelectedMaterial(link)
            TraileadPopupMenu(it, fragment)
                .create(getMenuPopUp(), R.color.primaryColor)
                .setVisibilityItem(R.id.material_full_screen, false)
                .setOnClickListener { item -> popupLinkListener(item) }
                .show()
        }
    }

    private fun popupLinkListener(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.material_delete -> navigateToDeleteMaterial()
            R.id.material_edit -> {
                navigateToEditLinkMaterial()
            }

            else -> return true
        }
        return false
    }

    private fun navigateToEditLinkMaterial() {
        fragment.findNavController()
            .navigate(R.id.action_leaderMaterialListFragment_to_editLinkFragment)
    }


    private fun bindingPdf(holder: MaterialListViewHolder, material: Pdf) {
        TODO("Not yet implemented")
    }

    @SuppressLint("ResourceType")
    private fun bindingYoutubeVideo(
        holder: MaterialListViewHolder,
        youtubeVideo: YouTubeVideo
    ) {
        holder.viewHolderBinding.cardMaterialId.visibility = VISIBLE
        fragment.lifecycle.addObserver(holder.viewHolderBinding.leaderMaterialYoutubeId)
        holder.viewHolderBinding.leaderMaterialYoutubeId.getYouTubePlayerWhenReady(object :
            YouTubePlayerCallback {
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                youTubePlayer.cueVideo(youtubeVideo.url, 0f)
            }
        })
        holder.viewHolderBinding.titleMaterialItem.hint = youtubeVideo.title
        holder.viewHolderBinding.imageSettingMaterialLeader.setOnClickListener {
            viewModel.setSelectedMaterial(youtubeVideo)
            TraileadPopupMenu(it, fragment)
                .create(getMenuPopUp(), R.color.primaryColor)
                .setOnClickListener { item -> popupListener(item) }
                .show()
        }
        configureSettingEditable()
    }

    private fun configureSettingEditable() {
        binding.imageSettingMaterialLeader.visibility = if (isEditable) VISIBLE else GONE
    }

    private fun getMenuPopUp() =
        if (isEditable) R.menu.menu_item_abm_material else R.menu.menu_item_m_material

    private fun popupListener(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.material_full_screen -> navigateToFullScreenVideo()
            R.id.material_delete -> navigateToDeleteMaterial()
            R.id.material_edit -> navigateToEditVideo()
            else -> return true
        }
        return false
    }

    private fun navigateToEditVideo() {
        fragment.findNavController().navigate(R.id.editMaterialFragment)
    }

    private fun navigateToDeleteMaterial() {
        viewModel.deleteMaterialSelected()
    }

    private fun navigateToFullScreenVideo() {
        fragment.findNavController().navigate(R.id.fullScreenVideo)
    }

    override fun getItemCount(): Int = dataSet.size

    private fun goneAllViews(holder: MaterialListViewHolder) {
        holder.viewHolderBinding.cardMaterialId.visibility = GONE
        holder.viewHolderBinding.linkViewMaterial.root.visibility = GONE
    }

    class MaterialListViewHolder(binding: ItemMaterialBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val viewHolderBinding: ItemMaterialBinding = binding
    }
}