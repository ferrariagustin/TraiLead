package com.aferrari.trailead.app.ui.trainee.profile

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aferrari.trailead.R
import com.aferrari.trailead.app.ui.RefreshListener
import com.aferrari.trailead.app.viewmodel.trainee.TraineeViewModel
import com.aferrari.trailead.common.BundleUtils
import com.aferrari.trailead.databinding.TraineeProfileFragmentBinding
import com.aferrari.trailead.viewmodel.StatusVisibilityPassword


class TraineeProfileFragment : Fragment(), RefreshListener {

    private lateinit var binding: TraineeProfileFragmentBinding
    private val homeTraineeViewModel: TraineeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.trainee_profile_fragment, container, false)
        binding.lifecycleOwner = this
        binding.traineeViewModel = homeTraineeViewModel
        initListeners()
        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initListeners() {
        binding.imageViewEditCompleteNameIcon.setOnClickListener {
            navigateToEditProfileName()
        }

        binding.imageViewEditPasswordIcon.setOnClickListener {
            navigateToEditPassword()
        }

        binding.imageViewPasswordIcon.setOnClickListener {
            when (homeTraineeViewModel.statusVisibilityPassword.value) {
                StatusVisibilityPassword.INVISIBLE -> {
                    setVisibilityPassword(
                        StatusVisibilityPassword.VISIBLE,
                        resources.getDrawable(R.drawable.ic_unlock, requireContext().theme),
                        null,
                    )
                }

                StatusVisibilityPassword.VISIBLE -> {
                    setVisibilityPassword(
                        StatusVisibilityPassword.INVISIBLE,
                        resources.getDrawable(R.drawable.ic_lock, requireContext().theme),
                        PasswordTransformationMethod()
                    )

                }

                else -> {}
            }
        }
    }

    private fun setVisibilityPassword(
        newVisibility: StatusVisibilityPassword,
        drawable: Drawable,
        passwordTransformationMethod: PasswordTransformationMethod?,
    ) {
        homeTraineeViewModel.statusVisibilityPassword.value = newVisibility
        binding.imageViewPasswordIcon.setImageDrawable(drawable)
        binding.traineePasswordTextview.transformationMethod = passwordTransformationMethod
    }

    private fun navigateToEditPassword() {
        findNavController().navigate(
            R.id.action_traineeProfileFragment_to_traineeEditProfilePass,
            BundleUtils().getBundleTab(1)
        )
    }

    private fun navigateToEditProfileName() {
        findNavController().navigate(
            R.id.action_traineeProfileFragment_to_traineeEditProfileName,
            BundleUtils().getBundleTab(1)
        )
    }

    override fun refresh() {
        homeTraineeViewModel.refresh()
    }
}
