package com.aferrari.trailead.app.ui.leader.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.aferrari.trailead.R
import com.aferrari.trailead.app.viewmodel.leader.LeaderViewModel
import com.aferrari.trailead.databinding.ItemMaterialCategoryListBinding
import com.aferrari.trailead.domain.models.Category

class MaterialCategoryListAdapter(
    private val dataSet: List<Category>,
    private val leaderViewModel: LeaderViewModel,
    private val fragment: Fragment
) :
    RecyclerView.Adapter<MaterialCategoryListAdapter.CategoryListViewHolder>() {

    internal lateinit var binding: ItemMaterialCategoryListBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_material_category_list,
            parent,
            false
        )
        return CategoryListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryListViewHolder, position: Int) {
        val category = dataSet[position]
        holder.viewHolderBinding.textViewMaterialCategoryId.text = category.name
        holder.viewHolderBinding.cardCategoryId.setOnClickListener {
            leaderViewModel.categorySelected = category
            fragment.findNavController().navigate(R.id.leaderMaterialListFragment)
        }

        holder.viewHolderBinding.cardCategoryId.setOnLongClickListener {
            leaderViewModel.categorySelected = category
            fragment.findNavController()
                .navigate(R.id.leaderSettingCategoryMaterial)
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int = dataSet.size

    class CategoryListViewHolder(binding: ItemMaterialCategoryListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val viewHolderBinding: ItemMaterialCategoryListBinding = binding
    }
}
