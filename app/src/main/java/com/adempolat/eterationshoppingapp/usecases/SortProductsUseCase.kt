package com.adempolat.eterationshoppingapp.usecases

import com.adempolat.eterationshoppingapp.data.Product
import com.adempolat.eterationshoppingapp.viewmodel.ProductViewModel

class SortProductsUseCase {
    operator fun invoke(products: List<Product>, sortOrder: ProductViewModel.SortOrder): List<Product> {
        return when (sortOrder) {
            ProductViewModel.SortOrder.OLD_TO_NEW -> products.sortedBy { it.createdAt }
            ProductViewModel.SortOrder.NEW_TO_OLD -> products.sortedByDescending { it.createdAt }
            ProductViewModel.SortOrder.PRICE_HIGH_TO_LOW -> products.sortedByDescending { it.price }
            ProductViewModel.SortOrder.PRICE_LOW_TO_HIGH -> products.sortedBy { it.price }
            ProductViewModel.SortOrder.A_TO_Z -> products.sortedBy { it.name }
            ProductViewModel.SortOrder.Z_TO_A -> products.sortedByDescending { it.name }
            else -> products
        }
    }
}