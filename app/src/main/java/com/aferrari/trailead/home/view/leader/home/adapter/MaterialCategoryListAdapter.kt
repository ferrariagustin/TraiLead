package com.aferrari.trailead.home.view.leader.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.aferrari.login.db.Category
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.ItemMaterialCategoryListBinding
import com.aferrari.trailead.home.viewmodel.leader.HomeLeaderViewModel

class MaterialCategoryListAdapter(
    private val dataSet: List<Category>,
    private val homeLeaderViewModel: HomeLeaderViewModel,
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
            homeLeaderViewModel.categorySelected = category
            fragment.findNavController().navigate(R.id.leaderMaterialListFragment)
        }
        holder.viewHolderBinding.imageSettingCategoryName.setOnClickListener {
            homeLeaderViewModel.categorySelected = category
            fragment.findNavController()
                .navigate(R.id.leaderSettingCategoryMaterial)
        }
    }

    override fun getItemCount(): Int = dataSet.size

    class CategoryListViewHolder(binding: ItemMaterialCategoryListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val viewHolderBinding: ItemMaterialCategoryListBinding = binding
    }
}
