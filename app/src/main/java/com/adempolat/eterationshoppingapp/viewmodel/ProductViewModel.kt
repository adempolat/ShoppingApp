package com.adempolat.eterationshoppingapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adempolat.eterationshoppingapp.ShoppingApp
import com.adempolat.eterationshoppingapp.data.Product
import com.adempolat.eterationshoppingapp.data.dao.toFavoriteItemEntity
import com.adempolat.eterationshoppingapp.data.dao.toProduct
import com.adempolat.eterationshoppingapp.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    // Diğer kodlar

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
        loadFavoriteProducts()

    }

    fun loadProducts() {
        viewModelScope.launch {
            repository.getProducts(currentPage).collect { newProducts ->
                val favorites = _favoriteProducts.value ?: listOf()
                val updatedProducts = newProducts.map { product ->
                    product.isFavorite = favorites.any { it.id == product.id }
                    product
                }
                _products.value = updatedProducts
                applyFilters()
            }
        }
    }

    fun loadMoreProducts() {
        viewModelScope.launch {
            currentPage++
            repository.getProducts(currentPage).collect { moreProducts ->
                val favorites = _favoriteProducts.value ?: listOf()
                val updatedProducts = moreProducts.map { product ->
                    product.isFavorite = favorites.any { it.id == product.id }
                    product
                }
                val currentList = _products.value ?: listOf()
                _products.value = currentList + updatedProducts
                applyFilters()
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
//        if (selectedBrands.isNotEmpty()) {
//            result = result.filter { it.name in selectedBrands }
//        }
//        if (selectedModels.isNotEmpty()) {
//            result = result.filter { it.description in selectedModels }
//        }
//        result = when (sortOrder) {
//            SortOrder.OLD_TO_NEW -> result.sortedBy { it.createdAt }
//            SortOrder.NEW_TO_OLD -> result.sortedByDescending { it.createdAt }
//            SortOrder.PRICE_HIGH_TO_LOW -> result.sortedByDescending { it.price }
//            SortOrder.PRICE_LOW_TO_HIGH -> result.sortedBy { it.price }
//        }
        _filteredProducts.value = result
    }


    private fun updateFavoriteProducts() {
        viewModelScope.launch {
            ShoppingApp.database.favoriteDao().getAllFavoriteItems().collect { favoriteItems ->
                val favoriteProducts = favoriteItems.map { it.toProduct() }
                _favoriteProducts.postValue(favoriteProducts)
                // Debugging purpose
                println("Loaded favorite products from DB: ${favoriteProducts.size} items")
                // Update products list with favorite information
                val currentProducts = _products.value?.map { product ->
                    product.isFavorite = favoriteProducts.any { it.id == product.id }
                    product
                }
                _products.postValue(currentProducts ?: listOf())
            }
        }
    }

    fun loadFavoriteProducts() {
        // Veritabanından favori ürünleri yükler
        viewModelScope.launch {
            ShoppingApp.database.favoriteDao().getAllFavoriteItems().collect { favoriteItems ->
                val favoriteProducts = favoriteItems.map { it.toProduct() }
                _favoriteProducts.postValue(favoriteProducts)
                // Debugging purpose
                println("Loaded favorite products from DB: ${favoriteProducts.size} items")
                // Update products list with favorite information
                val currentProducts = _products.value?.map { product ->
                    product.isFavorite = favoriteProducts.any { it.id == product.id }
                    product
                }
                _products.postValue(currentProducts ?: listOf())
            }
        }
    }

    fun toggleFavorite(product: Product) {
        viewModelScope.launch {
            product.isFavorite = !product.isFavorite
            val favoriteDao = ShoppingApp.database.favoriteDao()

            if (product.isFavorite) {
                favoriteDao.insertFavoriteItem(product.toFavoriteItemEntity())
            } else {
                favoriteDao.deleteFavoriteItem(product.toFavoriteItemEntity())
            }

            updateFavoriteProducts()
        }
    }
}
