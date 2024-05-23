package com.adempolat.eterationshoppingapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adempolat.eterationshoppingapp.ShoppingApp
import com.adempolat.eterationshoppingapp.data.CartItem
import com.adempolat.eterationshoppingapp.data.Product
import com.adempolat.eterationshoppingapp.data.dao.toCartItem
import com.adempolat.eterationshoppingapp.data.dao.toCartItemEntity
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class CartViewModel : ViewModel() {

    private val _cartItems = MutableLiveData<MutableList<CartItem>>(mutableListOf())
    val cartItems: LiveData<MutableList<CartItem>> get() = _cartItems

    private val _totalPrice = MutableLiveData<Double>()
    val totalPrice: LiveData<Double> get() = _totalPrice

    private val _cartItemCount = MutableLiveData<Int>()
    val cartItemCount: LiveData<Int> get() = _cartItemCount

    private val _totalSpent = MutableLiveData<Double>()
    val totalSpent: LiveData<Double> get() = _totalSpent

    private val _purchaseHistory = MutableLiveData<MutableList<Pair<Double, String>>>(mutableListOf())
    val purchaseHistory: LiveData<MutableList<Pair<Double, String>>> get() = _purchaseHistory

    init {
        loadCartItems()
        _totalSpent.value = 1000.0 // Başlangıç değeri
    }

    private fun loadCartItems() {
        viewModelScope.launch {
            ShoppingApp.database.cartDao().getAllCartItems().collect { cartItemsFromDb ->
                _cartItems.value = cartItemsFromDb.map { it.toCartItem() }.toMutableList()
                calculateTotalPrice()
                updateCartItemCount()
            }
        }
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            val currentList = _cartItems.value ?: mutableListOf()
            val existingItem = currentList.find { it.product.id == product.id }

            if (existingItem != null) {
                existingItem.quantity += 1
                ShoppingApp.database.cartDao().updateCartItem(existingItem.toCartItemEntity())
            } else {
                val newItem = CartItem(product, 1)
                currentList.add(newItem)
                ShoppingApp.database.cartDao().insertCartItem(newItem.toCartItemEntity())
            }
            _cartItems.value = currentList
            calculateTotalPrice()
            updateCartItemCount()
        }
    }

    fun increaseQuantity(cartItem: CartItem) {
        viewModelScope.launch {
            cartItem.quantity += 1
            ShoppingApp.database.cartDao().updateCartItem(cartItem.toCartItemEntity())
            _cartItems.value = _cartItems.value
            calculateTotalPrice()
            updateCartItemCount()
        }
    }

    fun decreaseQuantity(cartItem: CartItem) {
        viewModelScope.launch {
            if (cartItem.quantity > 1) {
                cartItem.quantity -= 1
                ShoppingApp.database.cartDao().updateCartItem(cartItem.toCartItemEntity())
                _cartItems.value = _cartItems.value
                calculateTotalPrice()
                updateCartItemCount()
            } else {
                removeFromCart(cartItem.product)
            }
        }
    }

    fun removeFromCart(product: Product) {
        viewModelScope.launch {
            val currentList = _cartItems.value
            currentList?.removeAll { it.product.id == product.id }
            ShoppingApp.database.cartDao().deleteCartItem(product.id)
            _cartItems.value = currentList!!
            calculateTotalPrice()
            updateCartItemCount()
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            ShoppingApp.database.cartDao().clearCart()
            _cartItems.value = mutableListOf()
            _totalPrice.value = 0.0
            _cartItemCount.value = 0
        }
    }

    private fun calculateTotalPrice() {
        val currentList = _cartItems.value ?: mutableListOf()
        var total = 0.0
        for (item in currentList) {
            total += item.product.price * item.quantity
        }
        _totalPrice.value = total
    }

    private fun updateCartItemCount() {
        val currentList = _cartItems.value ?: mutableListOf()
        var count = 0
        for (item in currentList) {
            count += item.quantity
        }
        _cartItemCount.value = count
    }

    fun updateTotalSpent(amount: Double) {
        val currentSpent = _totalSpent.value ?: 0.0
        _totalSpent.value = currentSpent + amount
    }

    fun addPurchaseHistory(amount: Double, time: String) {
        val currentHistory = _purchaseHistory.value ?: mutableListOf()
        currentHistory.add(Pair(amount, time))
        _purchaseHistory.value = currentHistory
    }
}