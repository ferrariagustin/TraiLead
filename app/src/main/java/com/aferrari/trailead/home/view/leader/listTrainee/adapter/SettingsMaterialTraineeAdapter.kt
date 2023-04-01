package com.aferrari.trailead.home.view.leader.listTrainee.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.aferrari.login.db.Category
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.ItemMaterialCategoryListBinding

class SettingsMaterialTraineeAdapter(
    private val dataSource: List<Category>,
    private val fragment: Fragment
) :
    RecyclerView.Adapter<SettingsMaterialTraineeAdapter.SettingsMaterialTraineeViewHolder>() {

    private lateinit var binding: ItemMaterialCategoryListBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SettingsMaterialTraineeViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_material_category_list,
            parent,
            false
        )
        configureItem()
        return SettingsMaterialTraineeViewHolder(binding)
    }

    private fun configureItem() {
        binding.imageSettingCategoryName.visibility = View.GONE
        binding.linkedRadioButtonCategory.visibility = View.VISIBLE
    }

    override fun onBindViewHolder(holder: SettingsMaterialTraineeViewHolder, position: Int) {
        val category = dataSource[position]
        holder.viewHolderBinding.textViewMaterialCategoryId.hint = category.name
    }

    override fun getItemCount(): Int = dataSource.size

    class SettingsMaterialTraineeViewHolder(var viewHolderBinding: ItemMaterialCategoryListBinding) :
        RecyclerView.ViewHolder(viewHolderBinding.root)
}