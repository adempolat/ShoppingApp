package com.adempolat.eterationshoppingapp.data

data class Product(
    val id: String,
    val createdAt: String,
    val name: String,
    val price: Double,
    val imageUrl: String,
    val description: String
)
