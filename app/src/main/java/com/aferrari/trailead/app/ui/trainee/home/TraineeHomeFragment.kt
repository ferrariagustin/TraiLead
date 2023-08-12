package com.aferrari.trailead.app.ui.trainee.home

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.aferrari.trailead.R
import com.aferrari.trailead.app.ui.RefreshListener
import com.aferrari.trailead.app.ui.trainee.home.adapter.TraineeCategoryListAdapter
import com.aferrari.trailead.app.viewmodel.trainee.TraineeViewModel
import com.aferrari.trailead.app.viewmodel.trainee.TraineeViewModelFactory
import com.aferrari.trailead.app.viewmodel.trainee.home.HomeTraineeViewModel
import com.aferrari.trailead.common.StringUtils
import com.aferrari.trailead.common.session.SessionManagement
import com.aferrari.trailead.databinding.TraineeHomeFragmentBinding

class TraineeHomeFragment : Fragment(), RefreshListener {
    private lateinit var binding: TraineeHomeFragmentBinding

    private val traineeViewModel: TraineeViewModel by activityViewModels()

    private lateinit var viewModel: HomeTraineeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.trainee_home_fragment, container, false)
        binding.lifecycleOwner = this
        val viewModelFactory = TraineeViewModelFactory(traineeViewModel)
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[HomeTraineeViewModel::class.java]
        initListeners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
    }

    private fun initRecycler() {
        binding.categoryListRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewModel.getCategories()
        viewModel.setCategories.observe(viewLifecycleOwner) {
            binding.categoryListRecyclerView.adapter =
                TraineeCategoryListAdapter(it.toList(), this, viewModel)
        }
    }

    private fun initListeners() {
        setTitleToolbar()
        binding.traineeHomeToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.close_session_toolbar_menu -> logout()
            }
            return@setOnMenuItemClickListener true
        }
    }

    private fun setTitleToolbar() {
        val title = resources.getString(
            R.string.welcome_home_trainee,
            viewModel.getTraineeName()
        )
        binding.traineeHomeToolbar.title = title
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

    override fun refresh() {
        viewModel.getCategories()
    }
}