package com.aferrari.trailead.home.view.leader.home

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
import com.aferrari.login.dialog.TraileadDialog
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.LeaderSettingMaterialCategoryBinding
import com.aferrari.trailead.home.viewmodel.leader.HomeLeaderViewModel
import com.aferrari.trailead.home.viewmodel.StatusUpdateInformation

class SettingCategoryMaterialFragment : Fragment() {

    private lateinit var binding: LeaderSettingMaterialCategoryBinding

    private val homeLeaderViewModel: HomeLeaderViewModel by activityViewModels()

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
        binding.homeLeaderViewModel = homeLeaderViewModel
        initListeners()
        return binding.root
    }

    private fun initListeners() {
        homeLeaderViewModel.init()
        binding.leaderCategorySettingToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.deleteNewCategoryButton.setOnClickListener {
            deleteShowDialog()
        }
        binding.saveNewCategoryButton.setOnClickListener {
            homeLeaderViewModel.editCategory(binding.editCategoryNameTextInput.text.toString())
        }

        homeLeaderViewModel.statusUpdateEditCategory.observe(viewLifecycleOwner) {
            when(it) {
                StatusUpdateInformation.SUCCESS -> {findNavController().navigateUp()}
                StatusUpdateInformation.FAILED -> {findNavController().navigateUp()}
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
                homeLeaderViewModel.categorySelected?.name ?: ""
            ),
            requireContext(),
            removeCategoryAction(),
            icon = resources.getDrawable(R.drawable.ic_delete, requireContext().theme).apply {
                this.setTint(
                    resources.getColor(
                        com.aferrari.login.R.color.red,
                        requireContext().theme
                    )
                )
            }
        )
    }

    private fun removeCategoryAction(): DialogInterface.OnClickListener =
        DialogInterface.OnClickListener { _, _ ->
            homeLeaderViewModel.removeCategory()
            findNavController().navigateUp()
        }
}