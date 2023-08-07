package com.aferrari.trailead.app.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.aferrari.trailead.R
import com.aferrari.trailead.app.viewmodel.login.LoginViewModel
import com.aferrari.trailead.app.viewmodel.login.LoginViewModelFactory
import com.aferrari.trailead.common.StringUtils
import com.aferrari.trailead.common.common_enum.LoginState
import com.aferrari.trailead.common.session.SessionManagement
import com.aferrari.trailead.common.ui.TraileadDialog
import com.aferrari.trailead.databinding.LoginFragmentBinding
import com.aferrari.trailead.domain.datasource.LocalDataSource
import com.aferrari.trailead.domain.datasource.RemoteDataSource
import com.aferrari.trailead.domain.models.User
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// TODO: don't working back navigation
@AndroidEntryPoint
class LoginFragment : Fragment(), Login, LifecycleOwner {

    private lateinit var binding: LoginFragmentBinding
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
        binding = DataBindingUtil.inflate(inflater, R.layout.login_fragment, container, false)
        val factory = LoginViewModelFactory(localDataSource, remoteDataSource)
        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
        binding.loginViewModel = loginViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLogin()
    }

    override fun onStart() {
        super.onStart()
        checkSession()
    }

    override fun goHome(user: User) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(StringUtils.DEEPLINK_HOME)).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            putExtra(StringUtils.USER_KEY, user)
        }
        startActivity(intent)
        requireActivity().finish()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun observeLogin() {
        loginViewModel.loginState.observe(viewLifecycleOwner) {
            when (it!!) {
                LoginState.STARTED -> startLogin()
                LoginState.IN_PROGRESS -> showProgressBar()
                LoginState.FAILED -> errorLogin()
                LoginState.SUCCESS -> successLogin()
                LoginState.REGISTER -> goRegister()
            }
        }
        loginViewModel.visibilityPassDrawable.observe(viewLifecycleOwner) {
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
        newIconPass: Drawable,
        passwordTransformationMethod: PasswordTransformationMethod?
    ) {
        binding.loginVisibilityPasswordImageView.setImageDrawable(newIconPass)
        binding.passwordInputText.transformationMethod = passwordTransformationMethod
    }

    private fun goRegister() {
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_loginFragment_to_registrationFragment)
    }

    private fun startLogin() {
        // do nothing
    }

    private fun successLogin() {
        binding.progressBar.visibility = View.GONE
        SessionManagement(requireContext()).saveSession(loginViewModel.user.id)
        goHome(loginViewModel.user)
    }

    private fun errorLogin() {
        TraileadDialog().showDialog(
            getString(R.string.login_btn_text),
            getString(R.string.error_login),
            requireContext()
        )
        binding.progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    /**
     * Validate if exist some session in sharedPrefernce.
     * If exist a session, redirect to home
     */
    private fun checkSession() {
        val userId = SessionManagement(requireContext()).getSession()
        if (userId != -1) {
            loginViewModel.getUser(userId)
        }
    }
}