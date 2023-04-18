package com.aferrari.trailead.home.view.trainee.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.aferrari.login.data.Category
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.ItemMaterialCategoryListBinding

class TraineeCategoryListAdapter(
    private val dataSet: List<Category>,
    private val fragment: Fragment
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
            Toast.makeText(fragment.context, "${category.name} Selected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = dataSet.size

    class ViewHolder(binding: ItemMaterialCategoryListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val viewHolderBinding: ItemMaterialCategoryListBinding = binding
    }
}