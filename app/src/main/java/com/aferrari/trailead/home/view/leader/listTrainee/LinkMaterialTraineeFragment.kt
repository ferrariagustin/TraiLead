package com.aferrari.trailead.home.view.leader.listTrainee

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
import com.aferrari.login.dialog.TraileadDialog
import com.aferrari.trailead.R
import com.aferrari.trailead.databinding.LinkMaterialTraineeFragmentBinding
import com.aferrari.trailead.home.Utils.BundleUtils
import com.aferrari.trailead.home.view.leader.listTrainee.adapter.SettingsMaterialTraineeAdapter
import com.aferrari.trailead.home.viewmodel.leader.HomeLeaderViewModel
import com.aferrari.trailead.home.viewmodel.leader.listTrainee.ListTraineeViewModel
import com.google.android.material.radiobutton.MaterialRadioButton


class LinkMaterialTraineeFragment : Fragment() {

    private lateinit var binding: LinkMaterialTraineeFragmentBinding

    private lateinit var viewModel: ListTraineeViewModel

    private val homeLeaderViewModel: HomeLeaderViewModel by activityViewModels()

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
        setVisibilityBottomNavigation(View.GONE)
    }

    private fun initListeners() {
        binding.settingTraineeMaterialToolbarId.setNavigationOnClickListener() {
            findNavController().navigateUp()
        }
        binding.linkedRadioButtonAll.setOnClickListener {
            if ((it as MaterialRadioButton).isSelected) {
                disableRadioButton(it)
                viewModel.unselectedAllCategory()
            } else {
                enableRadioButton(it)
                viewModel.selectedAllCategory()
            }
        }
        binding.saveLinkedCategoryTrainee.setOnClickListener {
            TraileadDialog().showDialogWithAction(
                title = "Guardar categorías",
                message = "Esta seguro de asignar las categorias seleccionadas a ${viewModel.traineeSelected.name + " " + viewModel.traineeSelected.lastName }",
                fragment = this,
                positiveAction = getPositiveAction(),
                iconRes = R.drawable.ic_link,
                colorRes = R.color.primaryColor
            )
        }
    }

    private fun getPositiveAction(): DialogInterface.OnClickListener =
        DialogInterface.OnClickListener { _, _ ->
            viewModel.saveCategorySelected()
            findNavController().navigateUp()
        }

    private fun disableRadioButton(radioButton: MaterialRadioButton) {
        radioButton.isSelected = false
        radioButton.isChecked = false
    }

    private fun enableRadioButton(radioButton: MaterialRadioButton) {
        radioButton.isSelected = true
        radioButton.isChecked = true
    }

    private fun initComponents() {
        binding.recyclerViewMaterialForCategoryList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewModel.getCategories()?.let {
            binding.recyclerViewMaterialForCategoryList.adapter =
                SettingsMaterialTraineeAdapter(it, this, viewModel)
        }
        viewModel.getCategoriesSelected()
    }

    private fun setVisibilityBottomNavigation(visibility: Int) {
        homeLeaderViewModel.setBottomNavigationVisibility(visibility)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        setVisibilityBottomNavigation(View.VISIBLE)
    }
}
