package com.aferrari.trailead.app.ui.login

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.aferrari.trailead.R
import com.aferrari.trailead.app.viewmodel.login.LoginViewModelFactory
import com.aferrari.trailead.app.viewmodel.login.RegistrationViewModel
import com.aferrari.trailead.common.common_enum.RegisterErrorState
import com.aferrari.trailead.common.common_enum.RegisterState
import com.aferrari.trailead.common.ui.TraileadDialog
import com.aferrari.trailead.databinding.RegistrationFragmentBinding
import com.aferrari.trailead.domain.datasource.LocalDataSource
import com.aferrari.trailead.domain.datasource.RemoteDataSource
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// TODO: don't working back navigation
@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private lateinit var binding: RegistrationFragmentBinding
    private lateinit var registrationViewModel: RegistrationViewModel

    @Inject
    lateinit var remoteDataSource: RemoteDataSource

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.registration_fragment, container, false)
        val factory = LoginViewModelFactory(remoteDataSource)
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
        registrationViewModel.visibilityPassDrawable.observe(viewLifecycleOwner) {
            when (it) {
                View.GONE -> {
                    setVisibilityPassword(
                        resources.getDrawable(R.drawable.ic_gone_text, requireContext().theme),
                        PasswordTransformationMethod()
                    )
                }

                View.VISIBLE -> {
                    setVisibilityPassword(
                        resources.getDrawable(R.drawable.ic_show_text, requireContext().theme),
                        null
                    )
                }
            }
        }
    }

    private fun setVisibilityPassword(
        drawable: Drawable?,
        passwordTransformationMethod: PasswordTransformationMethod?
    ) {
        binding.registerVisibilityPasswordImageView.setImageDrawable(drawable)
        binding.passwordRgInputText.transformationMethod =
            passwordTransformationMethod
        binding.repeatPasswordRgInputText.transformationMethod =
            passwordTransformationMethod
    }

    private fun observeLogin() {
        registrationViewModel.registerState.observe(viewLifecycleOwner) {
            when (it!!) {
                RegisterState.STARTED -> startRegister()
                RegisterState.IN_PROGRESS -> showProgressBar(View.VISIBLE)
                RegisterState.FAILED -> errorRegister()
                RegisterState.FAILED_USER_EXIST -> errorRegisterExistUser()
                RegisterState.SUCCESS -> successRegister()
                RegisterState.CANCEL -> goLogin()
            }
        }
    }

    private fun errorRegisterExistUser() {
        showProgressBar(View.GONE)
        TraileadDialog().showDialog(
            getString(R.string.register_btn_text),
            getString(R.string.error_user_existe_register),
            requireContext()
        )
    }

    private fun errorRegister() {
        showProgressBar(View.GONE)
        TraileadDialog().showDialog(
            getString(R.string.register_btn_text),
            getString(R.string.error_register), requireContext()
        )
    }

    private fun successRegister() {
        showProgressBar(View.GONE)
        TraileadDialog().showDialog(
            getString(R.string.register_btn_text),
            getString(R.string.success_register),
            requireContext()
        )
        goLogin()
    }

    private fun goLogin() {
        showProgressBar(View.GONE)
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_registrationFragment_to_loginFragment)
    }

    private fun showProgressBar(visibility: Int) {
        binding.progressBar.visibility = visibility
    }

    private fun startRegister() {
        // TODO: implement
    }

}