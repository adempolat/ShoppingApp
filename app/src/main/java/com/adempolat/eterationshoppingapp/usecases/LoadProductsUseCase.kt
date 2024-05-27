package com.adempolat.eterationshoppingapp.usecases

import com.adempolat.eterationshoppingapp.data.Product
import com.adempolat.eterationshoppingapp.repository.ProductRepository
import kotlinx.coroutines.flow.first

class LoadProductsUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(page: Int): List<Product> {
        return repository.getProducts(page).first()
    }
}

