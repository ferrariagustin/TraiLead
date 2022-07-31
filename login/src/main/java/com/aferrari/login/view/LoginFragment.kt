package com.aferrari.login.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.aferrari.login.R
import com.aferrari.login.databinding.LoginFragmentBinding
import com.aferrari.login.db.User
import com.aferrari.login.db.UserDataBase
import com.aferrari.login.db.UserRepository
import com.aferrari.login.session.SessionManagement
import com.aferrari.login.utils.StringUtils
import com.aferrari.login.viewmodel.LoginViewModel
import com.aferrari.login.viewmodel.LoginViewModelFactory
import com.aferrari.login.viewmodel.StateLogin

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
        loginViewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)
        binding.loginViewModel = loginViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLogin()
    }

    private fun observeLogin() {
        loginViewModel.stateLogin.observe(viewLifecycleOwner) {
            when (it!!) {
                StateLogin.STARTED -> startLogin()
                StateLogin.IN_PROGRESS -> showProgressBar()
                StateLogin.FAILED -> errorLogin()
                StateLogin.SUCCESS -> successLogin()
            }
        }
    }

    private fun startLogin() {
        // do nothing
    }

    private fun successLogin() {
        binding.progressBar.visibility = View.GONE
        SessionManagement(requireContext()).saveSession(loginViewModel.user)
        goHome(loginViewModel.user)
    }

    private fun errorLogin() {
        Toast.makeText(requireContext(), "Login Error", Toast.LENGTH_SHORT).show()
        binding.progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun onStart() {
        super.onStart()

        checkSession()
    }

    /**
     * Validate if exist some session in sharedPrefernce.
     * If exist a session, redirect to home
     */
    private fun checkSession() {
        val userId = SessionManagement(requireContext()).getSession()
        if (userId != -1) {
            val user = loginViewModel.getUser(userId, requireContext())
            if (user != null) {
                goHome(user)
            }
        }
    }

    override fun goHome(user: User) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(StringUtils.DEEPLINK_HOME)).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            putExtra(StringUtils.USER_NAME_KEY, user.name)
            putExtra(StringUtils.USER_EMAIL_KEY, user.email)
        }
        startActivity(intent)
        requireActivity().finish()
    }
}