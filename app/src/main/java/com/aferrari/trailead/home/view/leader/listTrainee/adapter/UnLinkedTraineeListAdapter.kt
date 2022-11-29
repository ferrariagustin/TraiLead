package com.aferrari.trailead.home.view.leader.listTrainee.adapter

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.aferrari.login.database.Trainee
import com.aferrari.login.dialog.Dialog
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.ItemTraineeListBinding
import com.aferrari.trailead.home.viewmodel.HomeLeaderViewModel

/**
 * Trainee List that all element is unlinked with a leader, the trainee_leader_id is null
 */
class UnLinkedTraineeListAdapter(
    private val dataSet: List<Trainee>,
    private val fragment: Fragment,
    private val viewModel: HomeLeaderViewModel
) :
    RecyclerView.Adapter<UnLinkedTraineeListAdapter.ViewHolder>() {

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

        holder.viewHolderBinding.traineeCardItem.setOnClickListener {
            Dialog().showDialogWithAction(
                title = fragment.resources.getString(R.string.dialog_title_add_trainee),
                message = fragment.resources.getString(
                    R.string.dialog_message_add_trainee,
                    trainee.name
                ),
                fragment.requireContext(),
                getPositiveAction(trainee)
            )
        }
    }

    /**
     * Link leader session with trainee selected
     */
    private fun getPositiveAction(trainee: Trainee) =
        DialogInterface.OnClickListener { _, _ ->
            viewModel.setLinkedTrainee(trainee)
        }

    override fun getItemCount(): Int = dataSet.size

    class ViewHolder(binding: ItemTraineeListBinding) : RecyclerView.ViewHolder(binding.root) {
        val viewHolderBinding: ItemTraineeListBinding = binding
    }

}
