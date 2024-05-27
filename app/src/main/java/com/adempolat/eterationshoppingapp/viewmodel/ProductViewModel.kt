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
import com.adempolat.eterationshoppingapp.usecases.FilterProductsUseCase
import com.adempolat.eterationshoppingapp.usecases.LoadProductsUseCase
import com.adempolat.eterationshoppingapp.usecases.SortProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val loadProductsUseCase: LoadProductsUseCase,
    private val filterProductsUseCase: FilterProductsUseCase,
    private val sortProductsUseCase: SortProductsUseCase,
    private val repository: ProductRepository
) : ViewModel() {

    enum class SortOrder {
        Sort_Order, OLD_TO_NEW, NEW_TO_OLD, PRICE_HIGH_TO_LOW, PRICE_LOW_TO_HIGH, A_TO_Z, Z_TO_A
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

    private lateinit var filterProducts: List<Product>

    private var currentPage = 1

    private var selectedBrands = mutableSetOf<String>()
    private var selectedModels = mutableSetOf<String>()
    private var sortOrder: SortOrder = SortOrder.Sort_Order

    init {
        loadProducts()
        loadFavoriteProducts()
        loadBrandsAndModels()
    }

    private fun loadBrandsAndModels() {
        viewModelScope.launch {
            val allProducts = repository.getProducts(1).firstOrNull() ?: listOf()
            val uniqueBrands = allProducts.map { it.brand }.distinct()
            val uniqueModels = allProducts.map { it.model }.distinct()
            _brands.postValue(uniqueBrands)
            _models.postValue(uniqueModels)
        }
    }

    fun loadProducts() {
        viewModelScope.launch {
            val products = loadProductsUseCase(currentPage)
            val favorites = _favoriteProducts.value ?: listOf()
            val updatedProducts = products.map { product ->
                product.isFavorite = favorites.any { it.id == product.id }
                product
            }
            _products.value = updatedProducts
            applyFiltersAndSort()
        }
    }

    fun loadMoreProducts() {
        viewModelScope.launch {
            currentPage++
            val moreProducts = loadProductsUseCase(currentPage)
            val favorites = _favoriteProducts.value ?: listOf()
            val updatedProducts = moreProducts.map { product ->
                product.isFavorite = favorites.any { it.id == product.id }
                product
            }
            val currentList = _products.value ?: listOf()
            _products.value = currentList + updatedProducts
            applyFiltersAndSort()
        }
    }

    fun setSortOrder(order: SortOrder) {
        sortOrder = order
        applyFiltersAndSort()
    }

    fun setSelectedBrands(brands: Set<String>) {
        selectedBrands = brands.toMutableSet()
        applyFiltersAndSort()
    }

    fun setSelectedModels(models: Set<String>) {
        selectedModels = models.toMutableSet()
        applyFiltersAndSort()
    }

    fun applyFiltersAndSort() {
        val filteredList = filterProductsUseCase(_products.value ?: listOf(), selectedBrands, selectedModels)
        _filteredProducts.value = sortProductsUseCase(filteredList, sortOrder)
    }

    private fun updateFavoriteProducts() {
        viewModelScope.launch {
            ShoppingApp.database.favoriteDao().getAllFavoriteItems().collect { favoriteItems ->
                val favoriteProducts = favoriteItems.map { it.toProduct() }
                _favoriteProducts.postValue(favoriteProducts)
                val currentProducts = _products.value?.map { product ->
                    product.isFavorite = favoriteProducts.any { it.id == product.id }
                    product
                }
                _products.postValue(currentProducts ?: listOf())
            }
        }
    }

    fun loadFavoriteProducts() {
        viewModelScope.launch {
            ShoppingApp.database.favoriteDao().getAllFavoriteItems().collect { favoriteItems ->
                val favoriteProducts = favoriteItems.map { it.toProduct() }
                _favoriteProducts.postValue(favoriteProducts)
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
