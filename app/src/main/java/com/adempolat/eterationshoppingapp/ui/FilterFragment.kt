package com.adempolat.eterationshoppingapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.adempolat.eterationshoppingapp.R
import com.adempolat.eterationshoppingapp.adapter.FilterAdapter
import com.adempolat.eterationshoppingapp.databinding.FragmentFilterBinding
import com.adempolat.eterationshoppingapp.viewmodel.ProductViewModel

class FilterFragment : DialogFragment() {

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!
    private val productViewModel: ProductViewModel by activityViewModels()

    private val selectedBrands = mutableSetOf<String>()
    private val selectedModels = mutableSetOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Sort By
        binding.sortRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.sortOldToNew -> productViewModel.setSortOrder(ProductViewModel.SortOrder.OLD_TO_NEW)
                R.id.sortNewToOld -> productViewModel.setSortOrder(ProductViewModel.SortOrder.NEW_TO_OLD)
                R.id.sortPriceHighToLow -> productViewModel.setSortOrder(ProductViewModel.SortOrder.PRICE_HIGH_TO_LOW)
                R.id.sortPriceLowToHigh -> productViewModel.setSortOrder(ProductViewModel.SortOrder.PRICE_LOW_TO_HIGH)
            }
        }

        // Brands
        val brandAdapter = FilterAdapter(productViewModel.brands,selectedBrands)
        binding.brandRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.brandRecyclerView.adapter = brandAdapter

        // Models
        val modelAdapter = FilterAdapter(productViewModel.models,selectedModels)
        binding.modelRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.modelRecyclerView.adapter = modelAdapter

        // Apply Filter Button
        binding.applyFilterButton.setOnClickListener {
            productViewModel.setSelectedBrands(selectedBrands)
            productViewModel.setSelectedModels(selectedModels)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}