package com.aferrari.trailead.app.ui.leader.home

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aferrari.trailead.R
import com.aferrari.trailead.app.viewmodel.leader.LeaderViewModel
import com.aferrari.trailead.common.ui.TraileadDialog
import com.aferrari.trailead.databinding.LeaderSettingMaterialCategoryBinding
import com.aferrari.trailead.common.common_enum.StatusUpdateInformation

class SettingCategoryMaterialFragment : Fragment() {

    private lateinit var binding: LeaderSettingMaterialCategoryBinding

    private val leaderViewModel: LeaderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.leader_setting_material_category,
                container,
                false
            )
        binding.lifecycleOwner = this
        binding.homeLeaderViewModel = leaderViewModel
        initListeners()
        return binding.root
    }

    private fun initListeners() {
        leaderViewModel.init()
        binding.leaderCategorySettingToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.deleteNewCategoryButton.setOnClickListener {
            deleteShowDialog()
        }
        binding.saveNewCategoryButton.setOnClickListener {
            leaderViewModel.editCategory(binding.editCategoryNameTextInput.text.toString())
        }

        leaderViewModel.statusUpdateEditCategory.observe(viewLifecycleOwner) {
            when (it) {
                StatusUpdateInformation.SUCCESS -> {
                    findNavController().navigateUp()
                }

                StatusUpdateInformation.FAILED -> {
                    findNavController().navigateUp()
                }

                else -> {}
            }

        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun deleteShowDialog() {
        TraileadDialog().showDialogWithAction(
            title = resources.getString(R.string.dialog_title_removed_trainee),
            message = resources.getString(
                R.string.dialog_message_delete_category,
                leaderViewModel.categorySelected?.name ?: ""
            ),
            requireContext(),
            removeCategoryAction(),
            icon = resources.getDrawable(R.drawable.ic_delete, requireContext().theme).apply {
                this.setTint(
                    resources.getColor(
                        R.color.red,
                        requireContext().theme
                    )
                )
            }
        )
    }

    private fun removeCategoryAction(): DialogInterface.OnClickListener =
        DialogInterface.OnClickListener { _, _ ->
            leaderViewModel.removeCategory()
            findNavController().navigateUp()
        }
}