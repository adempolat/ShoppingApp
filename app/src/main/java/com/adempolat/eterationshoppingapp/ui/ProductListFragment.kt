package com.adempolat.eterationshoppingapp.ui

import android.R
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adempolat.eterationshoppingapp.databinding.FragmentProductListBinding
import com.adempolat.eterationshoppingapp.viewmodel.CartViewModel
import com.adempolat.eterationshoppingapp.viewmodel.ProductViewModel
import com.example.shoppingapp.ui.ProductAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProductListFragment : Fragment() {

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!
    private val productViewModel: ProductViewModel by viewModels()
    private val cartViewModel: CartViewModel by activityViewModels()
    private lateinit var productAdapter: ProductAdapter

    private var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productAdapter = ProductAdapter(productViewModel,emptyList(),cartViewModel,this)
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = productAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as GridLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                    if (!isLoading && totalItemCount <= (lastVisibleItem + 4)) {
                        productViewModel.loadMoreProducts()
                        isLoading = true
                    }
            }
        })}

        productViewModel.filteredProducts.observe(viewLifecycleOwner) { products ->
            productAdapter.updateProducts(products)
            binding.recyclerView.scrollToPosition(0)
            isLoading = false
        }


        setupSortSpinner()


        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                productAdapter.filter(newText ?: "")
                return true
            }
        })

        binding.filterButton.setOnClickListener {
            val filterFragment = FilterFragment()
            filterFragment.show(parentFragmentManager, "FilterFragment")
        }
    }
    private fun setupSortSpinner() {
        val sortOptions = arrayOf("Sort Order", "High to Low", "Low to High", "A to Z", "Z to A")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sortOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.sortSpinner.adapter = adapter

        binding.sortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> productViewModel.setSortOrder(ProductViewModel.SortOrder.Sort_Order)
                    1 -> productViewModel.setSortOrder(ProductViewModel.SortOrder.PRICE_HIGH_TO_LOW)
                    2 -> productViewModel.setSortOrder(ProductViewModel.SortOrder.PRICE_LOW_TO_HIGH)
                    3 -> productViewModel.setSortOrder(ProductViewModel.SortOrder.A_TO_Z)
                    4 -> productViewModel.setSortOrder(ProductViewModel.SortOrder.Z_TO_A)
                }
                productViewModel.applyFiltersAndSort()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}