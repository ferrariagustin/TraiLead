package com.aferrari.trailead.home.view.trainee

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.aferrari.login.db.UserDataBase
import com.aferrari.login.db.UserRepository
import com.aferrari.login.session.SessionManagement
import com.aferrari.login.utils.StringUtils
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.TraineeHomeFragmentBinding
import com.aferrari.trailead.home.viewmodel.HomeTraineeViewModel
import com.aferrari.trailead.home.viewmodel.HomeViewModelFactory

class TraineeHomeFragment : Fragment() {
    private lateinit var binding: TraineeHomeFragmentBinding

    private lateinit var homeTraineeViewModel: HomeTraineeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.trainee_home_fragment, container, false)
        val dao = UserDataBase.getInstance(requireActivity()).userDao
        val repository = UserRepository(dao)
        val factory = HomeViewModelFactory(repository)
        homeTraineeViewModel = ViewModelProvider(this, factory)[HomeTraineeViewModel::class.java]
        binding.lifecycleOwner = this
        initListeners()
        return binding.root
    }

    private fun initListeners() {
        binding.logoutBtn.setOnClickListener {
            logout()
        }
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
}