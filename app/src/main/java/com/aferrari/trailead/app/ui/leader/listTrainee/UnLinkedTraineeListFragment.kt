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
import com.aferrari.trailead.app.ui.leader.listTrainee.adapter.UnLinkedTraineeListAdapter
import com.aferrari.trailead.app.viewmodel.leader.LeaderViewModel
import com.aferrari.trailead.databinding.UnlinkedTraineeListFragmentBinding


class UnLinkedTraineeListFragment : Fragment(), RefreshListener {
    lateinit var binding: UnlinkedTraineeListFragmentBinding

    private val leaderViewModel: LeaderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.unlinked_trainee_list_fragment,
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
    }

    private fun initComponents() {
        binding.traineesLeadersNoneRecycleView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        leaderViewModel.getUnLinkedTrainees()
        leaderViewModel.listunlinkedTrainees.observe(viewLifecycleOwner) {
            binding.traineesLeadersNoneRecycleView.adapter =
                UnLinkedTraineeListAdapter(it, this, leaderViewModel)
        }
        binding.leaderUnlinkedTraineesToolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_unLinkedTraineeListFragment_to_linkedTraineeListFragment)
        }
    }

    override fun refresh() {
        leaderViewModel.getUnLinkedTrainees()
    }
}