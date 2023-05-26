package com.aferrari.trailead.app.ui.leader.listTrainee

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aferrari.trailead.common.common_enum.Position
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.LinkedTraineeEditRolFragmentBinding
import com.aferrari.trailead.app.viewmodel.leader.LeaderViewModel
import com.aferrari.trailead.viewmodel.StatusUpdateInformation

class TraineeEditRolFragment : Fragment() {
    private lateinit var binding: LinkedTraineeEditRolFragmentBinding

    private val viewModel: LeaderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.linked_trainee_edit_rol_fragment,
                container,
                false
            )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        viewModel.init()
        initListeners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRolSelected()
    }

    private fun initListeners() {
        navigationUp()

        setInitialRol()

        updateRol()
    }

    private fun navigationUp() {
        binding.traineeRolToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun updateRol() {
        viewModel.statusUpdateTraineeRol.observe(viewLifecycleOwner) {
            when (it) {
                StatusUpdateInformation.SUCCESS -> {
                    findNavController().navigateUp()
                }
                StatusUpdateInformation.FAILED -> {
                    Toast.makeText(
                        requireContext(),
                        "No pudimos actualizar el Rol",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {}
            }
        }
    }

    private fun setRolSelected() {
        binding.radioRol.setOnCheckedChangeListener { _, id ->
            when (id) {
                binding.radioJunior.id -> viewModel.radioRolCheck = Position.JUNIOR
                binding.radioSemisenior.id -> viewModel.radioRolCheck = Position.SEMISENIOR
                binding.radioSenior.id -> viewModel.radioRolCheck = Position.SENIOR
            }
        }
    }

    private fun setInitialRol() {
        when (viewModel.traineeSelected?.position) {
            Position.JUNIOR -> binding.radioRol.check(binding.radioJunior.id)
            Position.SEMISENIOR -> binding.radioRol.check(binding.radioSemisenior.id)
            Position.SENIOR -> binding.radioRol.check(binding.radioSenior.id)
            else -> {}
        }
    }
}