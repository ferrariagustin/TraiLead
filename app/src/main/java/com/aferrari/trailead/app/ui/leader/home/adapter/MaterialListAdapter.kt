package com.aferrari.trailead.app.ui.leader.home.adapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.aferrari.trailead.R
import com.aferrari.trailead.app.component.TraileadPopupMenu
import com.aferrari.trailead.app.viewmodel.IMaterial
import com.aferrari.trailead.common.StringUtils.PDF_KEY
import com.aferrari.trailead.common.ui.TraileadDialog
import com.aferrari.trailead.databinding.ItemMaterialBinding
import com.aferrari.trailead.domain.models.Link
import com.aferrari.trailead.domain.models.Material
import com.aferrari.trailead.domain.models.Pdf
import com.aferrari.trailead.domain.models.YouTubeVideo
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback


class MaterialListAdapter(
    private val dataSet: List<Material>,
    private val fragment: Fragment,
    private val viewModel: IMaterial,
    private val isLeader: Boolean,
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
            val bundle = Bundle().apply {
                putString(LINK, link.url)
            }
            if (isLeader) {
                fragment.findNavController()
                    .navigate(R.id.action_leaderMaterialListFragment_to_linkFragment, bundle)
            } else {
                fragment.findNavController()
                    .navigate(R.id.action_traineeMaterialFragment_to_linkFragment, bundle)
            }
        }
        holder.viewHolderBinding.linkViewMaterial.itemLinkSettingImageView.setOnClickListener {
            TraileadPopupMenu(it, fragment)
                .create(getMenuPopUp(), R.color.primaryColor)
                .setVisibilityItem(R.id.material_full_screen, false)
                .setOnClickListener { item -> popupLinkListener(link, item) }
                .show()
        }
        configureSettingEditable()
    }

    private fun popupPDFListener(pdf: Pdf, item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.material_delete -> {
                TraileadDialog().showDialogWithAction(
                    fragment.resources.getString(R.string.delete_pdf),
                    fragment.resources.getString(R.string.delete_pdf_message, pdf.title),
                    fragment,
                    positiveAction = { _, _ ->
                        viewModel.setSelectedMaterial(pdf)
                        navigateToDeleteMaterial()
                    },
                    iconRes = R.drawable.ic_delete,
                    colorRes = R.color.red
                )
            }

            R.id.material_edit -> {
                viewModel.setSelectedMaterial(pdf)
                navigateToEditPdf()
            }

            else -> return true
        }
        return false
    }

    private fun navigateToEditPdf() {
        fragment.findNavController()
            .navigate(R.id.action_leaderMaterialListFragment_to_editPdfFragment)
    }

    private fun popupLinkListener(link: Link, item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.material_delete -> {
                viewModel.setSelectedMaterial(link)
                navigateToDeleteMaterial()
            }

            R.id.material_edit -> {
                viewModel.setSelectedMaterial(link)
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


    @SuppressLint("ResourceType")
    private fun bindingPdf(holder: MaterialListViewHolder, pdf: Pdf) {
        holder.viewHolderBinding.pdfViewMaterial.let { pdfBinding ->
            pdfBinding.root.visibility = VISIBLE
            pdfBinding.itemPdfTextView.text = pdf.title
            pdfBinding.itemPdfSettingImageView.visibility = VISIBLE
            pdfBinding.root.setOnClickListener {
                val bundle = Bundle().apply {
                    putSerializable(PDF_KEY, pdf)
                }
                navigateToPdfView(bundle)
            }
            pdfBinding.itemPdfSettingImageView.setOnClickListener {
                TraileadPopupMenu(it, fragment)
                    .create(getMenuPopUp(), R.color.primaryColor)
                    .setVisibilityItem(R.id.material_full_screen, false)
                    .setOnClickListener { item -> popupPDFListener(pdf, item) }
                    .show()
            }
            configureSettingEditable()
        }
    }

    private fun navigateToPdfView(bundle: Bundle) {
        if (isLeader) {
            fragment.findNavController()
                .navigate(R.id.action_leaderMaterialListFragment_to_pdfFragment, bundle)
        } else {
            fragment.findNavController()
                .navigate(R.id.action_traineeMaterialFragment_to_pdfFragment, bundle)
        }
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
        binding.linkViewMaterial.itemLinkSettingImageView.visibility =
            if (isEditable) VISIBLE else GONE
        binding.pdfViewMaterial.itemPdfSettingImageView.visibility =
            if (isEditable) VISIBLE else GONE
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
        holder.viewHolderBinding.pdfViewMaterial.root.visibility = GONE
    }

    class MaterialListViewHolder(binding: ItemMaterialBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val viewHolderBinding: ItemMaterialBinding = binding
    }

    companion object {
        const val LINK = "link"
    }
}
