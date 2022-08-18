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
import androidx.lifecycle.MutableLiveData
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
import com.google.android.material.textfield.TextInputEditText

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
        observeError(registrationViewModel.inputNameError, binding.userNameRgInputText)
        observeError(registrationViewModel.inputLastNameError, binding.lastNameRgInputText)
        observeError(registrationViewModel.inputEmailError, binding.emailRgInputText)
        observeError(registrationViewModel.inputPassError, binding.passwordRgInputText)
        observeError(registrationViewModel.inputRepeatPassError, binding.repeatPasswordRgInputText)
    }

    private fun observeError(
        registerErrorState: MutableLiveData<RegisterErrorState>,
        inputEditText: TextInputEditText
    ) {
        registerErrorState.observe(viewLifecycleOwner) {
            when (it) {
                RegisterErrorState.ERROR -> inputEditText.backgroundTintList =
                    ColorStateList.valueOf(Color.RED)
                RegisterErrorState.DONE -> inputEditText.backgroundTintList =
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
        Toast.makeText(requireContext(), "Registrado Correctamente", Toast.LENGTH_SHORT).show()
        goLogin()
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