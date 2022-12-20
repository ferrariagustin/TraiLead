package com.aferrari.trailead.home.view.leader.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.ItemMaterialCategoryLeaderHomeBinding

class MaterialListAdapter(
    private val dataSet: List<String>
) :
    RecyclerView.Adapter<MaterialListAdapter.MaterialListViewHolder>() {

    internal lateinit var binding: ItemMaterialCategoryLeaderHomeBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialListViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_material_category_leader_home,
            parent,
            false
        )
        return MaterialListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MaterialListViewHolder, position: Int) {
        val material = dataSet[position]
        holder.viewHolderBinding.textViewMaterialCategoryId.text = material
    }

    override fun getItemCount(): Int = dataSet.size

    class MaterialListViewHolder(binding: ItemMaterialCategoryLeaderHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val viewHolderBinding: ItemMaterialCategoryLeaderHomeBinding = binding
    }
}
