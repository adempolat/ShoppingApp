package com.adempolat.eterationshoppingapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.adempolat.eterationshoppingapp.R
import com.adempolat.eterationshoppingapp.databinding.FragmentFilterBinding
import com.adempolat.eterationshoppingapp.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterFragment : DialogFragment() {

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!
    private val productViewModel: ProductViewModel by activityViewModels()

    private val selectedBrands = mutableSetOf<String>()
    private val selectedModels = mutableSetOf<String>()

    val brands: List<String> = listOf(
        "Lamborghini", "Smart", "Ferrari", "Volkswagen", "Mercedes Benz", "Tesla", "Fiat",
        "Nissan", "Aston Martin", "Bugatti", "Rolls Royce", "Cadillac", "Mini", "Polestar",
        "Audi", "Honda", "Kia", "Ford", "BMW", "Dodge", "Mazda", "Jeep", "Toyota"
    )

    val models: List<String> = listOf(
        "CTS", "Roadster", "Taurus", "Jetta", "Fortwo", "A4", "F-150", "Corvette", "Alpine",
        "Ranchero", "Colorado", "911", "Altima", "Mercielago", "Explorer", "Grand Caravan",
        "Wrangler", "2", "Golf", "Accord", "V90", "Element", "Silverado", "Beetle", "Mustang",
        "Durango", "XTS", "Focus", "Fiesta", "El Camino", "Impala", "Model S", "Model X",
        "PT Cruiser", "Grand Cherokee", "Camry", "Charger", "Countach", "ATS", "Land Cruiser",
        "LeBaron", "Spyder", "Model 3", "Model Y", "Challenger", "Malibu", "Expedition", "Volt"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Brands
        val brandAdapter = FilterAdapter(brands, selectedBrands)
        binding.brandRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.brandRecyclerView.adapter = brandAdapter

        // Models
        val modelAdapter = FilterAdapter(models, selectedModels)
        binding.modelRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.modelRecyclerView.adapter = modelAdapter

        // Apply Filter Button
        binding.applyButton.setOnClickListener {
            productViewModel.setSelectedBrands(selectedBrands)
            productViewModel.setSelectedModels(selectedModels)
            productViewModel.applyFiltersAndSort()
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }
}