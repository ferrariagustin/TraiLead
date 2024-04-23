package com.aferrari.trailead.app.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.aferrari.trailead.R
import com.aferrari.trailead.app.viewmodel.login.LoginViewModel
import com.aferrari.trailead.app.viewmodel.login.LoginViewModelFactory
import com.aferrari.trailead.common.common_enum.ErrorView
import com.aferrari.trailead.common.common_enum.StatusCode
import com.aferrari.trailead.common.ui.TraiLeadSnackbar
import com.aferrari.trailead.databinding.RestorePasswordFragmentBinding
import com.aferrari.trailead.domain.datasource.LocalDataSource
import com.aferrari.trailead.domain.datasource.RemoteDataSource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class RestorePasswordFragment : Fragment() {

    private lateinit var binding: RestorePasswordFragmentBinding
    private lateinit var loginViewModel: LoginViewModel

    @Inject
    lateinit var remoteDataSource: RemoteDataSource

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.restore_password_fragment, container, false)
        val factory = LoginViewModelFactory(remoteDataSource)
        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
        binding.viewModel = loginViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        binding.toolbarRestorePassword.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.restorePasswordSendEmailBtn.setOnClickListener {
            loginViewModel.sendMail()
        }
        loginViewModel.sendEmailStatus.observe(viewLifecycleOwner) {
            when (it) {
                StatusCode.SUCCESS -> {
                    successFlow()
                }

                StatusCode.ERROR -> {
                    failedFlow(resources.getString(R.string.invalid_email))
                }

                StatusCode.NOT_FOUND -> {
                    failedFlow(resources.getString(R.string.user_not_found))
                }

                else -> {}
            }
        }
    }

    private fun successFlow() {
        findNavController().navigate(R.id.action_restorePasswordFragment_to_restorePasswordValidateKeyAccessFragment)
    }

    private fun failedFlow(message: String) {
        TraiLeadSnackbar().init(
            requireContext(),
            requireView(),
            message,
            type = ErrorView.ERROR
        )
    }
}