package com.adempolat.eterationshoppingapp.repository

import com.adempolat.eterationshoppingapp.data.Product
import com.adempolat.eterationshoppingapp.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val apiService: ApiService
) {
    fun getProducts(page:Int): Flow<List<Product>> = flow {
        val products = apiService.getProducts(page)
        emit(products)
    }.flowOn(Dispatchers.IO)
}
