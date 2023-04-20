package com.aferrari.trailead.home.view.trainee.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.aferrari.login.data.Category
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.ItemMaterialCategoryListBinding
import com.aferrari.trailead.home.viewmodel.trainee.home.HomeTraineeViewModel

class TraineeCategoryListAdapter(
    private val dataSet: List<Category>,
    private val fragment: Fragment,
    private val viewModel: HomeTraineeViewModel
) : RecyclerView.Adapter<TraineeCategoryListAdapter.ViewHolder>() {

    private lateinit var binding: ItemMaterialCategoryListBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(fragment.context),
            R.layout.item_material_category_list,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = dataSet[position]
        holder.viewHolderBinding.textViewMaterialCategoryId.text = category.name
        holder.viewHolderBinding.cardCategoryId.setOnClickListener {
            viewModel.setCategorySelected(category)
            fragment.findNavController()
                .navigate(R.id.action_traineeHomeFragment_to_traineeMaterialFragment)
        }
    }

    override fun getItemCount(): Int = dataSet.size

    class ViewHolder(binding: ItemMaterialCategoryListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val viewHolderBinding: ItemMaterialCategoryListBinding = binding
    }
}