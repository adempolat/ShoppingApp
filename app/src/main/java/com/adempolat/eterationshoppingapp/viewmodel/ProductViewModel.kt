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

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> get() = _products

    private var currentPage = 1



    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            repository.getProducts(currentPage).collect { newProducts ->
                _products.value = newProducts
            }
        }
    }

    fun loadMoreProducts() {
        viewModelScope.launch {
            currentPage++
            repository.getProducts(currentPage).collect { moreProducts ->
                val currentList = _products.value ?: listOf()
                _products.value = currentList + moreProducts
            }
        }
    }
}

