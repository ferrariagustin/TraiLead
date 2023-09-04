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
import com.aferrari.trailead.common.ui.TraiLeadSnackbar
import com.aferrari.trailead.databinding.EditProfilePassFragmentBinding
import com.aferrari.trailead.domain.datasource.LocalDataSource
import com.aferrari.trailead.domain.datasource.RemoteDataSource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RestorePasswordLoginFragment : Fragment() {

    private lateinit var binding: EditProfilePassFragmentBinding
    private lateinit var loginViewModel: LoginViewModel

    @Inject
    lateinit var remoteDataSource: RemoteDataSource

    @Inject
    lateinit var localDataSource: LocalDataSource

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.edit_profile_pass_fragment, container, false)
        val factory = LoginViewModelFactory(localDataSource, remoteDataSource)
        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        binding.editProfilePasswordToolbar.title = resources.getString(R.string.restore_password)
        binding.editProfilePasswordToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.editProfilePasswordButton.setOnClickListener {
            loginViewModel.restorePassword()
        }
    }

    private fun successFlow() {
        TODO("Not yet implemented")
    }

    private fun failedFlow() {
        TraiLeadSnackbar().init(
            requireContext(),
            requireView(),
            "Ingrese un email válido",
            type = ErrorView.ERROR
        )
    }
}
