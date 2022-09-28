package com.aferrari.trailead.home.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.aferrari.login.db.User
import com.aferrari.login.db.UserDataBase
import com.aferrari.login.db.UserRepository
import com.aferrari.login.session.SessionManagement
import com.aferrari.login.utils.StringUtils
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.HomeActivityBinding
import com.aferrari.trailead.home.viewmodel.HomeState
import com.aferrari.trailead.home.viewmodel.HomeViewModel
import com.aferrari.trailead.home.viewmodel.HomeViewModelFactory

class HomeFragment : Fragment() {

    private lateinit var binding: HomeActivityBinding

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.setContentView(requireActivity(), R.layout.home_activity)
        val dao = UserDataBase.getInstance(requireActivity()).userDao
        val repository = UserRepository(dao)
        val factory = HomeViewModelFactory(repository)
        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
        binding.lifecycleOwner = this
        initComponent()
        initListeners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initListeners() {
        homeViewModel.homeState.observe(viewLifecycleOwner) {
            when (it) {
                HomeState.LEADER -> goLeaderScreen()
                HomeState.TRAINEE -> goTraineeScreen()
                HomeState.ERROR -> goErrorScreen()
            }
        }
        binding.logoutBtn.setOnClickListener {
            logout()
        }
    }

    private fun goErrorScreen() {
        Toast.makeText(requireContext(), "Dialog de error y show login", Toast.LENGTH_SHORT).show()
    }

    private fun goTraineeScreen() {
        Toast.makeText(requireContext(), "Flujo de Trainee", Toast.LENGTH_SHORT).show()
    }

    private fun goLeaderScreen() {
        Toast.makeText(requireContext(), "Flujo de Leader", Toast.LENGTH_SHORT).show()
    }

    private fun logout() {
        SessionManagement(requireContext()).removeSession()
        goLogin()
    }

    private fun goLogin() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(StringUtils.DEEPLINK_LOGIN))
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    private fun initComponent() {
        homeViewModel.getUser(requireActivity().intent.extras?.getSerializable(StringUtils.USER_KEY) as User)
    }
}