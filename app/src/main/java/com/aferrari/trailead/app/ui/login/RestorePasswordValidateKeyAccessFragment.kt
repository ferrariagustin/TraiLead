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
import com.aferrari.trailead.databinding.RestorePasswordKeyAccessFragmentBinding
import com.aferrari.trailead.domain.datasource.LocalDataSource
import com.aferrari.trailead.domain.datasource.RemoteDataSource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RestorePasswordValidateKeyAccessFragment : Fragment() {
    private lateinit var binding: RestorePasswordKeyAccessFragmentBinding
    private lateinit var loginViewModel: LoginViewModel

    @Inject
    lateinit var remoteDataSource: RemoteDataSource

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.restore_password_key_access_fragment,
                container,
                false
            )
        val factory = LoginViewModelFactory(remoteDataSource)
        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        binding.restorePasswordKeyAccessToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.restorePasswordKeyAccessBtn.setOnClickListener {
            loginViewModel.validateAccessKey(binding.restorePasswordKeyAccessEditTextFirst.text.toString(),
                binding.restorePasswordKeyAccessEditTextSecond.text.toString(),
                binding.restorePasswordKeyAccessEditTextThird.text.toString(),
                binding.restorePasswordKeyAccessEditTextFour.text.toString(),)
        }
    }

}