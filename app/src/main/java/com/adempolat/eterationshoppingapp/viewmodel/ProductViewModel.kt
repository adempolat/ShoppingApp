package com.adempolat.eterationshoppingapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adempolat.eterationshoppingapp.data.Product
import com.adempolat.eterationshoppingapp.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    enum class SortOrder {
        OLD_TO_NEW, NEW_TO_OLD, PRICE_HIGH_TO_LOW, PRICE_LOW_TO_HIGH
    }

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> get() = _products

    private val _filteredProducts = MutableLiveData<List<Product>>()
    val filteredProducts: LiveData<List<Product>> get() = _filteredProducts

    private val _favoriteProducts = MutableLiveData<List<Product>>()
    val favoriteProducts: LiveData<List<Product>> get() = _favoriteProducts

    private var currentPage = 1
    var brands: List<String> = listOf("Bentley", "Aston Martin", "Rolls")
    var models: List<String> = listOf("11", "12 Pro", "13 Pro Max")

    private var selectedBrands = mutableSetOf<String>()
    private var selectedModels = mutableSetOf<String>()
    private var sortOrder: SortOrder = SortOrder.OLD_TO_NEW


    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            repository.getProducts(currentPage).collect { newProducts ->
                _products.value = newProducts
                applyFilters()
                updateFavoriteProducts()
            }
        }
    }

    fun loadMoreProducts() {
        viewModelScope.launch {
            currentPage++
            repository.getProducts(currentPage).collect { moreProducts ->
                val currentList = _products.value ?: listOf()
                _products.value = currentList + moreProducts
                applyFilters()
                updateFavoriteProducts()
            }
        }
    }

    fun setSortOrder(order: SortOrder) {
        sortOrder = order
        applyFilters()
    }

    fun setSelectedBrands(brands: Set<String>) {
        selectedBrands = brands.toMutableSet()
        applyFilters()
    }

    fun setSelectedModels(models: Set<String>) {
        selectedModels = models.toMutableSet()
        applyFilters()
    }

    fun applyFilters() {
        val products = _products.value ?: listOf()
        var result = products
        if (selectedBrands.isNotEmpty()) {
            result = result.filter { it.name in selectedBrands }
        }
        if (selectedModels.isNotEmpty()) {
            result = result.filter { it.description in selectedModels }
        }
        result = when (sortOrder) {
            SortOrder.OLD_TO_NEW -> result.sortedBy { it.createdAt }
            SortOrder.NEW_TO_OLD -> result.sortedByDescending { it.createdAt }
            SortOrder.PRICE_HIGH_TO_LOW -> result.sortedByDescending { it.price }
            SortOrder.PRICE_LOW_TO_HIGH -> result.sortedBy { it.price }
        }
        _filteredProducts.value = result
    }

    private fun updateFavoriteProducts() {
        _favoriteProducts.value = _products.value?.filter { it.isFavorite }
    }

    fun toggleFavorite(product: Product) {
        product.isFavorite = !product.isFavorite
        val currentProducts = _products.value?.map {
            if (it.id == product.id) product else it
        }
        _products.value = currentProducts!!
        updateFavoriteProducts()
    }
}

