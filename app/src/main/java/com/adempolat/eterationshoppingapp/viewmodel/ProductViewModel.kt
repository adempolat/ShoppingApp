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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    enum class SortOrder {
        Sort_Order,OLD_TO_NEW, NEW_TO_OLD, PRICE_HIGH_TO_LOW, PRICE_LOW_TO_HIGH, A_TO_Z, Z_TO_A
    }

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> get() = _products

    private val _filteredProducts = MutableLiveData<List<Product>>()
    val filteredProducts: LiveData<List<Product>> get() = _filteredProducts

    private val _favoriteProducts = MutableLiveData<List<Product>>()
    val favoriteProducts: LiveData<List<Product>> get() = _favoriteProducts

    private val _brands = MutableLiveData<List<String>>()
    val brands: LiveData<List<String>> get() = _brands

    private val _models = MutableLiveData<List<String>>()
    val models: LiveData<List<String>> get() = _models

    private var currentPage = 1

    private var selectedBrands = mutableSetOf<String>()
    private var selectedModels = mutableSetOf<String>()
    private var sortOrder: SortOrder = SortOrder.OLD_TO_NEW

    init {
        loadProducts()
        loadFavoriteProducts()
        loadBrandsAndModels()
    }

    private fun loadBrandsAndModels() {
//        viewModelScope.launch {
//            val allProducts = repository.getProducts(currentPage)
//            val uniqueBrands = allProducts.map { it.brand }.distinct()
//            val uniqueModels = allProducts.map { it.model }.distinct()
//            _brands.postValue(uniqueBrands)
//            _models.postValue(uniqueModels)
//        }
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
                //_filteredProducts.value = updatedProducts // Başlangıçta tüm ürünler gelsin
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

        // Uygun filtreleme işlemleri
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
            SortOrder.A_TO_Z -> result.sortedBy { it.name }
            SortOrder.Z_TO_A -> result.sortedByDescending { it.name }
            else-> result
        }

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
