package com.aferrari.trailead.home.view.leader.listTrainee

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.LinkMaterialTraineeFragmentBinding
import com.aferrari.trailead.home.view.leader.listTrainee.adapter.ConfigMaterialTraineeAdapter
import com.aferrari.trailead.home.view.leader.listTrainee.adapter.SpinnerAdapterCategoryLinkList
import com.aferrari.trailead.home.viewmodel.leader.HomeLeaderViewModel
import com.aferrari.trailead.home.viewmodel.leader.listTrainee.ListTraineeViewModel


class LinkMaterialTraineeFragment : Fragment() {

    private lateinit var binding: LinkMaterialTraineeFragmentBinding

    private lateinit var viewModel: ListTraineeViewModel

    private val homeLeaderViewModel: HomeLeaderViewModel by activityViewModels()

    private lateinit var spinnerAdapter: SpinnerAdapterCategoryLinkList

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.link_material_trainee_fragment,
            container,
            false
        )
        viewModel = ListTraineeViewModel(homeLeaderViewModel)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
        initListeners()
    }

    private fun initComponents() {
        binding.recyclerViewMaterialForCategoryList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        spinnerAdapter = SpinnerAdapterCategoryLinkList(requireContext())
        configureSpinner()
    }

    private fun initListeners() {
        spinnerAdapter.itemSelected.observe(viewLifecycleOwner) { categorySelected ->
            viewModel.getMaterialsBy(categorySelected)?.let {
                binding.recyclerViewMaterialForCategoryList.adapter =
                    ConfigMaterialTraineeAdapter(it, this)
            }
        }
        binding.linkMaterialTraineeToolbar.setNavigationOnClickListener() {
            findNavController().navigateUp()
        }
    }

    private fun configureSpinner() {
        binding.linkMaterialSelectedCategorySpinner.onItemSelectedListener = spinnerAdapter
        val categories: MutableList<String> = viewModel.getAllCategoryToString()

        // Creating adapter for spinner
        val dataAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, categories)

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // attaching data adapter to spinner
        binding.linkMaterialSelectedCategorySpinner.adapter = dataAdapter
    }
}