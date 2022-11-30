package com.aferrari.trailead.home.view.leader.listTrainee.adapter

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.aferrari.login.db.Trainee
import com.aferrari.login.dialog.Dialog
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.ItemTraineeListBinding
import com.aferrari.trailead.home.viewmodel.HomeLeaderViewModel

/**
 * Trainee List that all element is linked with a leader
 */
class LinkedTraineeListAdapter(
    private val dataSet: List<Trainee>,
    private val fragment: Fragment,
    private val viewModel: HomeLeaderViewModel
) : RecyclerView.Adapter<LinkedTraineeListAdapter.ViewHolder>() {

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
        holder.viewHolderBinding.nameLabelId.text = trainee.name + " " + trainee.lastName
        holder.viewHolderBinding.emailLabelId.text = trainee.email
        holder.viewHolderBinding.positionLabelId.text = trainee.position.name

        holder.viewHolderBinding.traineeCardItem.setOnClickListener {
            Dialog().showDialogWithAction(
                title = fragment.resources.getString(R.string.dialog_title_removed_trainee),
                message = fragment.resources.getString(
                    R.string.dialog_message_removed_trainee,
                    trainee.name
                ),
                fragment.requireContext(),
                getPositiveAction(trainee)
            )
        }
    }

    private fun getPositiveAction(trainee: Trainee): DialogInterface.OnClickListener =
        DialogInterface.OnClickListener { _, _ ->
            viewModel.setUnlinkedTrainee(trainee)
        }


    override fun getItemCount(): Int = dataSet.size

    class ViewHolder(binding: ItemTraineeListBinding) : RecyclerView.ViewHolder(binding.root) {
        val viewHolderBinding: ItemTraineeListBinding = binding
    }

}
