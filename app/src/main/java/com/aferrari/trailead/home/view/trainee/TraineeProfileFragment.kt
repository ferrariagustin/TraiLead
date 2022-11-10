package com.aferrari.trailead.home.view.trainee

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.aferrari.login.db.UserDataBase
import com.aferrari.login.db.UserRepository
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.TraineeProfileFragmentBinding
import com.aferrari.trailead.home.viewmodel.HomeTraineeViewModel
import com.aferrari.trailead.home.viewmodel.HomeViewModelFactory

class TraineeProfileFragment : Fragment() {

    private lateinit var binding: TraineeProfileFragmentBinding
    private lateinit var homeTraineeViewModel: HomeTraineeViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.trainee_profile_fragment, container, false)
        val dao = UserDataBase.getInstance(requireActivity()).userDao
        val repository = UserRepository(dao)
        val factory = HomeViewModelFactory(repository)
        homeTraineeViewModel = ViewModelProvider(this, factory)[HomeTraineeViewModel::class.java]
        binding.lifecycleOwner = this
        initListeners()
        return binding.root
    }

    private fun initListeners() {
        // do nothing
    }
}
