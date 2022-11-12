package com.aferrari.trailead.home.view.trainee

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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

    private val homeTraineeViewModel: HomeTraineeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.trainee_home_fragment, container, false)
        binding.lifecycleOwner = this
        initListeners()
        return binding.root
    }

    private fun initListeners() {
        binding.traineeHomeToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.close_session_toolbar_menu -> logout()
            }
            return@setOnMenuItemClickListener true
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