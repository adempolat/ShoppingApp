package com.adempolat.eterationshoppingapp.service

import com.adempolat.eterationshoppingapp.data.Product
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("products")
    suspend fun getProducts(@Query("page") page: Int): List<Product>
}