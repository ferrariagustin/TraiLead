package com.aferrari.trailead.home.view.leader.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.LeaderMaterialListBinding
import com.aferrari.trailead.home.view.leader.home.adapter.MaterialListAdapter
import com.aferrari.trailead.home.viewmodel.leader.LeaderViewModel

class LeaderMaterialListFragment : Fragment() {

    private lateinit var binding: LeaderMaterialListBinding
    private val leaderViewModel: LeaderViewModel by activityViewModels()

    // Initializing the animations
    private val rotateOpenAnimation: Animation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_open_animation)
    }
    private val rotateCloseAnimation: Animation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_close_animation)
    }
    private val fromBottomAnimation: Animation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.from_bottom_animation)
    }
    private val toBottomAnimation: Animation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.to_bottom_animation)
    }

    private var addButtonClicked = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.leader_material_list, container, false)
        binding.lifecycleOwner = this
        binding.homeLeaderViewModel = leaderViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        leaderViewModel.init()
        binding.leaderMaterialListToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.recyclerViewMaterialList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        leaderViewModel.getMaterialsCategoryFilter()
        leaderViewModel.listAllMaterials.observe(viewLifecycleOwner) {
            binding.recyclerViewMaterialList.adapter =
                MaterialListAdapter(it, this, leaderViewModel, true)
        }
        binding.addMaterialLeader.setOnClickListener {
            onAddButtonClicked()
        }
        binding.addVideoMaterial.setOnClickListener {
            findNavController().navigate(R.id.addMaterialFragment)
        }
    }

    private fun onAddButtonClicked() {
        setVisibility(addButtonClicked)
        setAnimation(addButtonClicked)
        buttonSetClickable()

        addButtonClicked = !addButtonClicked
    }

    //Setting call and message buttons visible
    private fun setVisibility(buttonClicked: Boolean) {
        if (!buttonClicked) {
            binding.addLinkMaterial.visibility = VISIBLE
            binding.addPdfMaterial.visibility = VISIBLE
            binding.addVideoMaterial.visibility = VISIBLE
        } else {
            binding.addLinkMaterial.visibility = INVISIBLE
            binding.addPdfMaterial.visibility = INVISIBLE
            binding.addVideoMaterial.visibility = INVISIBLE
        }
    }

    //Setting the animation on the buttons
    private fun setAnimation(buttonClicked: Boolean) {
        if (!buttonClicked) {
            binding.addLinkMaterial.startAnimation(fromBottomAnimation)
            binding.addPdfMaterial.startAnimation(fromBottomAnimation)
            binding.addVideoMaterial.startAnimation(fromBottomAnimation)
            binding.addMaterialLeader.startAnimation(rotateOpenAnimation)
        } else {
            binding.addLinkMaterial.startAnimation(toBottomAnimation)
            binding.addPdfMaterial.startAnimation(toBottomAnimation)
            binding.addVideoMaterial.startAnimation(toBottomAnimation)
            binding.addMaterialLeader.startAnimation(rotateCloseAnimation)

        }
    }

    //Checking if the add button is clicked
    private fun buttonSetClickable() {
        if (!addButtonClicked) {
            binding.addLinkMaterial.isClickable = true
            binding.addPdfMaterial.isClickable = true
            binding.addVideoMaterial.isClickable = true
        } else {
            binding.addLinkMaterial.isClickable = false
            binding.addPdfMaterial.isClickable = false
            binding.addVideoMaterial.isClickable = false
        }
    }
}