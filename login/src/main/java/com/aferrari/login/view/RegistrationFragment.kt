package com.aferrari.login.view

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.aferrari.login.R
import com.aferrari.login.databinding.RegistrationFragmentBinding
import com.aferrari.login.db.UserDataBase
import com.aferrari.login.db.UserRepository
import com.aferrari.login.viewmodel.LoginViewModelFactory
import com.aferrari.login.viewmodel.register.RegisterErrorState
import com.aferrari.login.viewmodel.register.RegisterState
import com.aferrari.login.viewmodel.register.RegistrationViewModel

class RegistrationFragment : Fragment() {

    private lateinit var binding: RegistrationFragmentBinding
    private lateinit var registrationViewModel: RegistrationViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.registration_fragment, container, false)
        val dao = UserDataBase.getInstance(requireActivity()).userDao
        val repository = UserRepository(dao)
        val factory = LoginViewModelFactory(repository)
        registrationViewModel = ViewModelProvider(this, factory)[RegistrationViewModel::class.java]
        binding.registrationViewModel = registrationViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLogin()
        handleError()
    }

    private fun handleError() {
        registrationViewModel.inputNameError.observe(viewLifecycleOwner) {
            when (it) {
                RegisterErrorState.ERROR -> binding.userNameRgInputText.backgroundTintList =
                    ColorStateList.valueOf(Color.RED)
                RegisterErrorState.DONE -> binding.userNameRgInputText.backgroundTintList =
                    ColorStateList.valueOf(Color.GRAY)
            }
        }
        registrationViewModel.inputLastNameError.observe(viewLifecycleOwner) {
            when (it) {
                RegisterErrorState.ERROR -> binding.lastNameRgInputText.backgroundTintList =
                    ColorStateList.valueOf(Color.RED)
                RegisterErrorState.DONE -> binding.lastNameRgInputText.backgroundTintList =
                    ColorStateList.valueOf(Color.GRAY)
            }
        }
        registrationViewModel.inputEmailError.observe(viewLifecycleOwner) {
            when (it) {
                RegisterErrorState.ERROR -> binding.emailRgInputText.backgroundTintList =
                    ColorStateList.valueOf(Color.RED)
                RegisterErrorState.DONE -> binding.emailRgInputText.backgroundTintList =
                    ColorStateList.valueOf(Color.GRAY)
            }
        }
        registrationViewModel.inputPassError.observe(viewLifecycleOwner) {
            when (it) {
                RegisterErrorState.ERROR -> binding.passwordRgInputText.backgroundTintList =
                    ColorStateList.valueOf(Color.RED)
                RegisterErrorState.DONE -> binding.passwordRgInputText.backgroundTintList =
                    ColorStateList.valueOf(Color.GRAY)
            }
        }
        registrationViewModel.inputRepeatPassError.observe(viewLifecycleOwner) {
            when (it) {
                RegisterErrorState.ERROR -> binding.repeatPasswordRgInputText.backgroundTintList =
                    ColorStateList.valueOf(Color.RED)
                RegisterErrorState.DONE -> binding.repeatPasswordRgInputText.backgroundTintList =
                    ColorStateList.valueOf(Color.GRAY)
            }
        }
    }

    private fun observeLogin() {
        registrationViewModel.registerState.observe(viewLifecycleOwner) {
            when (it!!) {
                RegisterState.STARTED -> startRegister()
                RegisterState.IN_PROGRESS -> showProgressBar()
                RegisterState.FAILED -> errorRegister()
                RegisterState.SUCCESS -> successRegister()
                RegisterState.CANCEL -> goLogin()
            }
        }
    }

    private fun errorRegister() {
        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
    }

    private fun successRegister() {
        Toast.makeText(requireContext(), "Registrado", Toast.LENGTH_SHORT).show()
    }

    private fun goLogin() {
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_registrationFragment_to_loginFragment)
    }

    private fun showProgressBar() {
        // TODO: implement
    }

    private fun startRegister() {
        // TODO: implement
    }

}