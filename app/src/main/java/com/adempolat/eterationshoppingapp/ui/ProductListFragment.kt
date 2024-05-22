package com.adempolat.eterationshoppingapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        productAdapter = ProductAdapter(emptyList(),cartViewModel)
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

        productViewModel.products.observe(viewLifecycleOwner) { products ->
            productAdapter.updateProducts(products)
            isLoading = false
        }

        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                productAdapter.filter(newText ?: "")
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}