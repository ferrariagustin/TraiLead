package com.aferrari.trailead.app.ui.trainee.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.TraineeMaterialListFromCategorySelectedBinding
import com.aferrari.trailead.app.ui.leader.home.adapter.MaterialListAdapter
import com.aferrari.trailead.app.viewmodel.trainee.TraineeViewModel
import com.aferrari.trailead.app.viewmodel.trainee.home.HomeTraineeViewModel

class TraineeMaterialFragment : Fragment() {

    private lateinit var binding: TraineeMaterialListFromCategorySelectedBinding

    private val traineeViewModel: TraineeViewModel by activityViewModels()

    private lateinit var viewModel: HomeTraineeViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.trainee_material_list_from_category_selected,
                container,
                false
            )
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(requireActivity())[HomeTraineeViewModel::class.java]
        initComponents()
        initListeners()
        return binding.root
    }

    private fun initComponents() {
        binding.traineeMaterialRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun initListeners() {
        binding.traineeListMaterialToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        viewModel.getMaterials()
        viewModel.materialsByCategory.observe(viewLifecycleOwner) {
            binding.traineeMaterialRecyclerView.adapter = MaterialListAdapter(it, this, viewModel)
        }
    }
}