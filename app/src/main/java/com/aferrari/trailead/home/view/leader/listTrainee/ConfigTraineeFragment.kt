package com.aferrari.trailead.home.view.leader.listTrainee

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
import com.aferrari.login.dialog.Dialog
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.LeaderConfigTraineeFragmentBinding
import com.aferrari.trailead.home.viewmodel.HomeLeaderViewModel

class ConfigTraineeFragment : Fragment() {

    lateinit var binding: LeaderConfigTraineeFragmentBinding

    private val homeLeaderViewModel: HomeLeaderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.leader_config_trainee_fragment,
                container,
                false
            )
        binding.viewModel = homeLeaderViewModel
        binding.lifecycleOwner = this
        initListener()
        return binding.root
    }

    private fun initListener() {
        binding.traineeConfigToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.configEditRolButton.setOnClickListener {
            findNavController().navigate(R.id.action_configTraineeFragment_to_traineeEditRolFragment)
        }
        binding.configUnlinkedButton.setOnClickListener {
            showDialog()
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun showDialog() {
        Dialog().showDialogWithAction(
            title = resources.getString(R.string.dialog_title_unlinked_trainee),
            message = resources.getString(
                R.string.dialog_message_unlinked_trainee,
                homeLeaderViewModel.traineeSelected?.name
            ),
            requireContext(),
            getPositiveAction(),
            icon = resources.getDrawable(R.drawable.ic_unlink, requireContext().theme).apply {
                this.setTint(
                    resources.getColor(
                        R.color.primaryColor,
                        requireContext().theme
                    )
                )
            }
        )
    }

    private fun getPositiveAction(): DialogInterface.OnClickListener =
        DialogInterface.OnClickListener { _, _ ->
            homeLeaderViewModel.setUnlinkedTrainee()
            findNavController().navigateUp()
        }

}