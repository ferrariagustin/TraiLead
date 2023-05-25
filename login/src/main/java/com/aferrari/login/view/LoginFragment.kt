package com.aferrari.login.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.aferrari.login.R
import com.aferrari.login.databinding.LoginFragmentBinding
import com.aferrari.login.data.user.dao.User
import com.aferrari.login.data.UserDataBase
import com.aferrari.login.data.user.repository.UserRepository
import com.aferrari.login.dialog.TraileadDialog
import com.aferrari.login.session.SessionManagement
import com.aferrari.login.utils.StringUtils
import com.aferrari.login.viewmodel.LoginViewModelFactory
import com.aferrari.login.viewmodel.login.LoginState
import com.aferrari.login.viewmodel.login.LoginViewModel

// TODO: don't working back navigation
class LoginFragment : Fragment(), Login, LifecycleOwner {

    private lateinit var binding: LoginFragmentBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.login_fragment, container, false)
        val dao = UserDataBase.getInstance(requireActivity()).userDao
        val repository = UserRepository(dao)
        val factory = LoginViewModelFactory(repository)
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