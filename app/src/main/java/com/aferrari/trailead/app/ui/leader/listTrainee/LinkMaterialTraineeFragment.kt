package com.aferrari.trailead.app.ui.leader.listTrainee

import android.content.DialogInterface
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
import com.aferrari.trailead.app.ui.leader.listTrainee.adapter.SettingsMaterialTraineeAdapter
import com.aferrari.trailead.app.viewmodel.leader.LeaderViewModel
import com.aferrari.trailead.app.viewmodel.leader.listTrainee.ListTraineeViewModel
import com.aferrari.trailead.common.ui.TraileadDialog
import com.aferrari.trailead.databinding.LinkMaterialTraineeFragmentBinding
import com.google.android.material.radiobutton.MaterialRadioButton


class LinkMaterialTraineeFragment : Fragment(), RefreshListener {

    private lateinit var binding: LinkMaterialTraineeFragmentBinding

    private lateinit var viewModel: ListTraineeViewModel

    private val leaderViewModel: LeaderViewModel by activityViewModels()

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
        viewModel = ListTraineeViewModel(leaderViewModel)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
        initListeners()
        setVisibilityBottomNavigation(View.GONE)
    }

    private fun initListeners() {
        binding.settingTraineeMaterialToolbarId.setNavigationOnClickListener() {
            findNavController().navigateUp()
        }
        binding.linkedRadioButtonAll.setOnClickListener {
            if ((it as MaterialRadioButton).isSelected) {
                disableAllRadioButton()
                viewModel.unselectedAllCategory()
            } else {
                enableAllRadioButton()
                viewModel.selectedAllCategory()
            }
        }
        binding.saveLinkedCategoryTrainee.setOnClickListener {
            TraileadDialog().showDialogWithAction(
                title = "Guardar categorías",
                message = "Esta seguro de asignar las categorias seleccionadas a ${viewModel.traineeSelected.name + " " + viewModel.traineeSelected.lastName}",
                fragment = this,
                positiveAction = getPositiveAction(),
                iconRes = R.drawable.ic_link,
                colorRes = R.color.primaryColor
            )
        }
        refresh()
    }

    private fun getPositiveAction(): DialogInterface.OnClickListener =
        DialogInterface.OnClickListener { _, _ ->
            viewModel.saveCategorySelected()
            findNavController().navigateUp()
        }

    private fun disableAllRadioButton() {
        binding.linkedRadioButtonAll.isSelected = false
        binding.linkedRadioButtonAll.isChecked = false
    }

    private fun enableAllRadioButton() {
        binding.linkedRadioButtonAll.isSelected = true
        binding.linkedRadioButtonAll.isChecked = true
    }

    private fun initComponents() {
        binding.recyclerViewMaterialForCategoryList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewModel.getCategoriesSelected()
        viewModel.getCategories()?.let {
            binding.recyclerViewMaterialForCategoryList.adapter =
                SettingsMaterialTraineeAdapter(it, this, viewModel)
        }
        initAllSelected()
    }

    /**
     * Init all radio button if all categories is selected
     */
    private fun initAllSelected() {
        viewModel.allCategorySelectedSize.observe(viewLifecycleOwner) {
            if (it == viewModel.getCategories()?.size) {
                enableAllRadioButton()
            } else {
                disableAllRadioButton()
            }
        }
    }

    private fun setVisibilityBottomNavigation(visibility: Int) {
        leaderViewModel.setBottomNavigationVisibility(visibility)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        setVisibilityBottomNavigation(View.VISIBLE)
    }

    override fun refresh() {
        leaderViewModel.refresh.observe(viewLifecycleOwner) {
            if (it) viewModel.getCategoriesSelected()
        }
    }
}
