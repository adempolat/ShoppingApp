package com.adempolat.eterationshoppingapp.usecases

import com.adempolat.eterationshoppingapp.data.Product

class FilterProductsUseCase {
    operator fun invoke(products: List<Product>, selectedBrands: Set<String>, selectedModels: Set<String>): List<Product> {
        var result = products
        if (selectedBrands.isNotEmpty()) {
            result = result.filter { it.brand in selectedBrands }
        }
        if (selectedModels.isNotEmpty()) {
            result = result.filter { it.model in selectedModels }
        }
        return result
    }
}
