package com.adempolat.eterationshoppingapp.data

data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val imageUrl: String?,
    val description: String,
    val createdAt:String,
    var isFavorite: Boolean = false, // Favori durumu için alan
    val model: String= "", // Yeni alan
    val brand: String= "" // Yeni alan
)
