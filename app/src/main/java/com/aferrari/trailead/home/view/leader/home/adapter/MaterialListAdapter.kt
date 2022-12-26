package com.aferrari.trailead.home.view.leader.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.aferrari.login.db.Category
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.ItemMaterialCategoryLeaderHomeBinding
import com.aferrari.trailead.home.viewmodel.HomeLeaderViewModel

class MaterialListAdapter(
    private val dataSet: List<Category>,
    private val homeLeaderViewModel: HomeLeaderViewModel,
    private val fragment: Fragment
) :
    RecyclerView.Adapter<MaterialListAdapter.CategoryListViewHolder>() {

    internal lateinit var binding: ItemMaterialCategoryLeaderHomeBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_material_category_leader_home,
            parent,
            false
        )
        return CategoryListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryListViewHolder, position: Int) {
        val category = dataSet[position]
        holder.viewHolderBinding.textViewMaterialCategoryId.text = category.name
        holder.viewHolderBinding.imageSettingCategoryMaterialLeader.setOnClickListener {
            homeLeaderViewModel.categorySelected = category
            fragment.findNavController()
                .navigate(R.id.leaderSettingCategoryMaterial)
        }
    }

    override fun getItemCount(): Int = dataSet.size

    class CategoryListViewHolder(binding: ItemMaterialCategoryLeaderHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val viewHolderBinding: ItemMaterialCategoryLeaderHomeBinding = binding
    }
}
