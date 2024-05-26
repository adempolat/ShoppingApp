package com.adempolat.eterationshoppingapp.data.dao

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.adempolat.eterationshoppingapp.data.Product

@Entity(tableName = "favorite_items")
data class FavoriteItemEntity(
    @PrimaryKey val productId: String,
    val productName: String,
    val productDescription: String,
    val productPrice: Double,
    val productImageUrl: String,
    val createdAt: String
)

fun FavoriteItemEntity.toProduct(): Product {
    return Product(
        id = productId,
        name = productName,
        description = productDescription,
        price = productPrice,
        createdAt = createdAt,
        imageUrl = productImageUrl,
        isFavorite = true
    )
}

fun Product.toFavoriteItemEntity(): FavoriteItemEntity {
    return FavoriteItemEntity(
        productId = id,
        productName = name,
        productDescription = description,
        productPrice = price,
        productImageUrl = "",
        createdAt = createdAt
    )
}
