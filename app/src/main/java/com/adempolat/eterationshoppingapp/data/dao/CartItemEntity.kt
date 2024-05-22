package com.adempolat.eterationshoppingapp.data.dao

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.adempolat.eterationshoppingapp.data.CartItem
import com.adempolat.eterationshoppingapp.data.Product

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey val productId: String,
    val productName: String,
    val productDescription: String,
    val productPrice: Double,
    val productImageUrl: String,
    val quantity: Int
)

fun CartItemEntity.toCartItem(): CartItem {
    return CartItem(
        product = Product(
            id = productId,
            name = productName,
            description = productDescription,
            price = productPrice,
            imageUrl = productImageUrl,
        ),
        quantity = quantity
    )
}

fun CartItem.toCartItemEntity(): CartItemEntity {
    return CartItemEntity(
        productId = product.id,
        productName = product.name,
        productDescription = product.description,
        productPrice = product.price,
        productImageUrl = product.imageUrl!!,
        quantity = quantity
    )
}
