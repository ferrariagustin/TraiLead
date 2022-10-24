package com.aferrari.trailead.home.view.leader.listTrainee

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.UnlinkedTraineeListFragmentBinding
import com.aferrari.trailead.home.view.leader.listTrainee.adapter.UnLinkedTraineeListAdapter
import com.aferrari.trailead.home.viewmodel.HomeLeaderViewModel

class UnLinkedTraineeListFragment : Fragment() {
    lateinit var binding: UnlinkedTraineeListFragmentBinding

    private val homeLeaderViewModel: HomeLeaderViewModel by activityViewModels()

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
        binding.homeLeaderViewModel = homeLeaderViewModel
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
        homeLeaderViewModel.getUnLinkedTrainees()
        homeLeaderViewModel.listunlinkedTrainees.observe(viewLifecycleOwner) {
            binding.traineesLeadersNoneRecycleView.adapter =
                UnLinkedTraineeListAdapter(it, this, homeLeaderViewModel)
        }
    }
}