package com.aferrari.trailead.home.view.leader.listTrainee

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.aferrari.login.db.Trainee
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.ItemTraineeListBinding

class ListTraineeAdapter(private val dataSet: List<Trainee>) :
    RecyclerView.Adapter<ListTraineeAdapter.ViewHolder>() {

    internal lateinit var binding: ItemTraineeListBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_trainee_list,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val trainee = dataSet[position]
        holder.viewHolderBinding.nameLabelId.text = trainee.name
        holder.viewHolderBinding.lastNameLabelId.text = trainee.lastName
        holder.viewHolderBinding.emailLabelId.text = trainee.email
    }

    override fun getItemCount(): Int = dataSet.size

    class ViewHolder(binding: ItemTraineeListBinding) : RecyclerView.ViewHolder(binding.root) {
        val viewHolderBinding: ItemTraineeListBinding = binding
    }

}
