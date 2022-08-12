package com.aferrari.login.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.aferrari.login.R
import com.aferrari.login.databinding.RegistrationFragmentBinding
import com.aferrari.login.db.UserDataBase
import com.aferrari.login.db.UserRepository
import com.aferrari.login.viewmodel.LoginViewModelFactory
import com.aferrari.login.viewmodel.login.LoginState
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
        // TODO: implement
    }

    private fun successRegister() {
        // TODO: implement
    }

    private fun goLogin() {
        NavHostFragment.findNavController(this).navigate(R.id.action_registrationFragment_to_loginFragment)
    }

    private fun showProgressBar() {
        // TODO: implement
    }

    private fun startRegister() {
        // TODO: implement
    }

}