package com.aferrari.trailead.app.ui.leader.listTrainee

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aferrari.trailead.R
import com.aferrari.trailead.app.ui.RefreshListener
import com.aferrari.trailead.app.ui.leader.listTrainee.adapter.LinkedTraineeListAdapter
import com.aferrari.trailead.app.viewmodel.leader.LeaderViewModel
import com.aferrari.trailead.databinding.LinkedTraineeListFragmentBinding

class LinkedTraineeListFragment : Fragment(), RefreshListener {

    private lateinit var binding: LinkedTraineeListFragmentBinding

    private val leaderViewModel: LeaderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.linked_trainee_list_fragment,
                container,
                false
            )
        binding.homeLeaderViewModel = leaderViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
        binding.addFloatingButton.setOnClickListener {
            findNavController().navigate(R.id.action_linkedTraineeListFragment_to_unLinkedTraineeListFragment)
        }
    }

    private fun initComponents() {
        binding.traineeListRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        leaderViewModel.getLinkedTrainees()
        leaderViewModel.listLinkedTrainees.observe(viewLifecycleOwner) {
            when (it.isEmpty()) {
                true -> binding.emptyResultTextView.visibility = View.VISIBLE
                false -> binding.emptyResultTextView.visibility = View.INVISIBLE
            }
            binding.traineeListRecyclerView.adapter =
                LinkedTraineeListAdapter(it, this, leaderViewModel)
        }
        refresh()
    }

    override fun refresh() {
        leaderViewModel.refresh.observe(viewLifecycleOwner) {
            if (it) leaderViewModel.getLinkedTrainees()
        }
    }
}