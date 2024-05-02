package com.aferrari.trailead.app.ui.login

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.aferrari.trailead.common.ui.TraiLeadSnackbar
import com.aferrari.trailead.common.ui.TraileadDialog
import com.aferrari.trailead.databinding.LoginFragmentBinding
import com.aferrari.trailead.domain.datasource.RemoteDataSource
import com.aferrari.trailead.domain.models.User
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


// TODO: don't working back navigation
@AndroidEntryPoint
class LoginFragment : Fragment(), Login, LifecycleOwner {

    private lateinit var binding: LoginFragmentBinding
    private lateinit var loginViewModel: LoginViewModel

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                Toast.makeText(requireContext(), "Is Logged!", Toast.LENGTH_SHORT).show()
            }
        }

    @Inject
    lateinit var remoteDataSource: RemoteDataSource

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.login_fragment, container, false)
        val factory = LoginViewModelFactory(remoteDataSource)
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
        loginViewModel.validateSession(requireContext())
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
                LoginState.INTERNET_CONECTION -> errorInternetConection()
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
        binding.restorePasswordBtn.setOnClickListener {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_loginFragment_to_restorePasswordFragment)
        }
        binding.loginBtn.setOnClickListener {
            loginViewModel.login(requireContext())
        }
    }

    private fun errorInternetConection() {
        TraiLeadSnackbar().errorConection(requireContext(), binding.root)
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
}