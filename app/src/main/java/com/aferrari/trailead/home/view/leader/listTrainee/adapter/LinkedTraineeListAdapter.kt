package com.aferrari.trailead.home.view.leader.listTrainee.adapter

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.aferrari.components.TraileadPopupMenu
import com.aferrari.login.db.Trainee
import com.aferrari.login.dialog.TraileadDialog
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

    @SuppressLint("SetTextI18n", "ResourceType")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val trainee = dataSet[position]
        holder.viewHolderBinding.nameLabelId.text = trainee.name + " " + trainee.lastName
        holder.viewHolderBinding.emailLabelId.text = trainee.email
        holder.viewHolderBinding.positionLabelId.text = trainee.position.name

        holder.viewHolderBinding.configTraineeIconId.setOnClickListener {
            viewModel.traineeSelected = trainee
            TraileadPopupMenu(it, fragment)
                .create(R.menu.menu_item_popup_trainee_linked, R.color.primaryColor)
                .setOnClickListener { item -> popupListener(item) }
                .show()
        }
    }

    private fun popupListener(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.trainee_linked_edit_rol -> navigateToEditRol()
            R.id.trainee_linked_setting_material -> navigateToSettingMaterial()
            R.id.trainee_linked_unlinked -> navigateToUnLinked()
            else -> return true
        }
        return false
    }

    private fun navigateToEditRol() {
        fragment.findNavController()
            .navigate(R.id.action_linkedTraineeListFragment_to_traineeEditRolFragment)
    }

    private fun navigateToSettingMaterial() {
        fragment.findNavController()
            .navigate(R.id.action_linkedTraineeListFragment_to_linkMaterialTraineeFragment)
    }

    private fun navigateToUnLinked() {
        unlinkedShowDialog()
    }

    private fun unlinkedShowDialog() {
        TraileadDialog().showDialogWithAction(
            title = fragment.resources.getString(R.string.dialog_title_unlinked_trainee),
            message = fragment.resources.getString(
                R.string.dialog_message_unlinked_trainee,
                viewModel.traineeSelected?.name
            ),
            fragment,
            unlinkedTraineeAction(),
            iconRes = R.drawable.ic_unlink,
            colorRes = R.color.primaryColor
        )
    }

    private fun unlinkedTraineeAction(): DialogInterface.OnClickListener =
        DialogInterface.OnClickListener { _, _ ->
            viewModel.setUnlinkedTrainee()
        }

    override fun getItemCount(): Int = dataSet.size

    class ViewHolder(binding: ItemTraineeLinkedListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val viewHolderBinding: ItemTraineeLinkedListBinding = binding
    }

}
