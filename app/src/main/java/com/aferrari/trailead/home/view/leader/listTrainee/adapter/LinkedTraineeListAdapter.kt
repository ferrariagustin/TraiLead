package com.aferrari.trailead.home.view.leader.listTrainee.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.aferrari.login.db.Trainee
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.ItemTraineeLinkedListBinding
import com.aferrari.trailead.home.viewmodel.leader.HomeLeaderViewModel

/**
 * Trainee List that all element is linked with a leader
 */
class LinkedTraineeListAdapter(
    private val dataSet: List<Trainee>,
    private val fragment: Fragment,
    private val viewModel: HomeLeaderViewModel
) : RecyclerView.Adapter<LinkedTraineeListAdapter.ViewHolder>() {

    internal lateinit var binding: ItemTraineeLinkedListBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_trainee_linked_list,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val trainee = dataSet[position]
        holder.viewHolderBinding.nameLabelId.text = trainee.name + " " + trainee.lastName
        holder.viewHolderBinding.emailLabelId.text = trainee.email
        holder.viewHolderBinding.positionLabelId.text = trainee.position.name

        holder.viewHolderBinding.configTraineeIconId.setOnClickListener {
            viewModel.traineeSelected = trainee
            Navigation.findNavController(holder.itemView)
                .navigate(R.id.action_linkedTraineeListFragment_to_configTraineeFragment)
        }
    }

    override fun getItemCount(): Int = dataSet.size

    class ViewHolder(binding: ItemTraineeLinkedListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val viewHolderBinding: ItemTraineeLinkedListBinding = binding
    }

}
