package com.aferrari.trailead.home.view.leader.listTrainee.adapter

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.aferrari.login.data.user.Trainee
import com.aferrari.login.dialog.TraileadDialog
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.ItemTraineeUnlinkedListBinding
import com.aferrari.trailead.home.viewmodel.leader.LeaderViewModel

/**
 * Trainee List that all element is unlinked with a leader, the trainee_leader_id is null
 */
class UnLinkedTraineeListAdapter(
    private val dataSet: List<Trainee>,
    private val fragment: Fragment,
    private val viewModel: LeaderViewModel
) :
    RecyclerView.Adapter<UnLinkedTraineeListAdapter.ViewHolder>() {

    internal lateinit var binding: ItemTraineeUnlinkedListBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_trainee_unlinked_list,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val trainee = dataSet[position]
        holder.viewHolderBinding.unlinkedNameLabelId.text = trainee.name + " " + trainee.lastName
        holder.viewHolderBinding.unlinkedEmailLabelId.text = trainee.email
        holder.viewHolderBinding.unlinkedPositionLabelId.text = trainee.position.name

        holder.viewHolderBinding.unlinkedConfigTraineeIconId.setOnClickListener {
            TraileadDialog().showDialogWithAction(
                title = fragment.resources.getString(R.string.dialog_title_add_trainee),
                message = fragment.resources.getString(
                    R.string.dialog_message_add_trainee,
                    trainee.name
                ),
                fragment.requireContext(),
                getPositiveAction(trainee),
                icon = fragment.resources.getDrawable(
                    R.drawable.ic_link,
                    fragment.requireContext().theme
                ).apply {
                    this.setTint(
                        fragment.resources.getColor(
                            R.color.primaryColor,
                            fragment.requireContext().theme
                        )
                    )
                }
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

    class ViewHolder(binding: ItemTraineeUnlinkedListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val viewHolderBinding: ItemTraineeUnlinkedListBinding = binding
    }

}
