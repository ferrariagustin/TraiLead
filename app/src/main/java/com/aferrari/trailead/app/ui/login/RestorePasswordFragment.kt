package com.aferrari.trailead.app.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.aferrari.trailead.R
import com.aferrari.trailead.app.viewmodel.login.LoginViewModel
import com.aferrari.trailead.app.viewmodel.login.LoginViewModelFactory
import com.aferrari.trailead.common.email.EmailUtils
import com.aferrari.trailead.databinding.RestorePasswordFragmentBinding
import com.aferrari.trailead.domain.datasource.LocalDataSource
import com.aferrari.trailead.domain.datasource.RemoteDataSource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch


@AndroidEntryPoint
class RestorePasswordFragment : Fragment() {

    private lateinit var binding: RestorePasswordFragmentBinding
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
            DataBindingUtil.inflate(inflater, R.layout.restore_password_fragment, container, false)
        val factory = LoginViewModelFactory(localDataSource, remoteDataSource)
        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
        binding.loginViewModel = loginViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        binding.restorePasswordSendEmailBtn.setOnClickListener {
            lifecycleScope.launch {
                // ver como enviar mail
//                EmailUtils.sendEmail(
//                    binding.emailRestorePasswordInput.text.toString(),
//                    requireContext()
//                )
            }
        }
    }
}