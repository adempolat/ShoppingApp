package com.adempolat.eterationshoppingapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adempolat.eterationshoppingapp.data.CartItem
import com.adempolat.eterationshoppingapp.data.Product


class CartViewModel : ViewModel() {

    private val _cartItems = MutableLiveData<MutableList<CartItem>>(mutableListOf())
    val cartItems: LiveData<MutableList<CartItem>> get() = _cartItems

    private val _totalPrice = MutableLiveData<Double>()
    val totalPrice: LiveData<Double> get() = _totalPrice

    private val _cartItemCount = MutableLiveData<Int>()
    val cartItemCount: LiveData<Int> get() = _cartItemCount

    fun addToCart(product: Product) {
        val currentList = _cartItems.value ?: mutableListOf()
        val existingItem = currentList.find { it.product.id == product.id }

        if (existingItem != null) {
            existingItem.quantity += 1
        } else {
            currentList.add(CartItem(product, 1))
        }
        _cartItems.value = currentList
        calculateTotalPrice()
        updateCartItemCount()
    }

    fun increaseQuantity(cartItem: CartItem) {
        cartItem.quantity += 1
        _cartItems.value = _cartItems.value
        calculateTotalPrice()
        updateCartItemCount()
    }

    fun decreaseQuantity(cartItem: CartItem) {
        if (cartItem.quantity > 1) {
            cartItem.quantity -= 1
            _cartItems.value = _cartItems.value
            calculateTotalPrice()
            updateCartItemCount()
        }
    }

    fun removeFromCart(product: Product) {
        val currentList = _cartItems.value
        currentList?.removeAll { it.product.id == product.id }
        _cartItems.value = currentList!!
        calculateTotalPrice()
        updateCartItemCount()
    }

    private fun calculateTotalPrice() {
        val currentList = _cartItems.value ?: mutableListOf()
        var total = 0.0
        for (item in currentList) {
            total += item.product.price * item.quantity
        }
        _totalPrice . value = total
    }

    private fun updateCartItemCount() {
        val currentList = _cartItems.value ?: mutableListOf()
        var count = 0
        for (item in currentList) {
            count += item.quantity
        }
        _cartItemCount.value = count
    }
}
