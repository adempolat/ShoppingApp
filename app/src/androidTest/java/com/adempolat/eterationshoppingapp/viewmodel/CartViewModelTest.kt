package com.adempolat.eterationshoppingapp.viewmodel

import androidx.lifecycle.Observer
import com.adempolat.eterationshoppingapp.data.CartItem
import com.adempolat.eterationshoppingapp.data.Product
import com.adempolat.eterationshoppingapp.data.dao.CartDao
import com.adempolat.eterationshoppingapp.data.dao.CartItemEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import okhttp3.internal.tls.OkHostnameVerifier.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import androidx.arch.core.executor.testing.InstantTaskExecutorRule


@ExperimentalCoroutinesApi
class CartViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var cartDao: CartDao

    private lateinit var cartViewModel: CartViewModel

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        cartViewModel = CartViewModel(cartDao)
    }

    @Test
    fun addToCart_success() = testDispatcher.runBlockingTest {
        val product = Product("1", "Product 1", 10.0, "url1", "desc1", "2023-01-01")
        val cartItem = CartItem(product, 1)
        val cartItemEntity = CartItemEntity("1", "Product 1", "desc1", 10.0, "url1", 1, "2023-01-01")

        `when`(cartDao.getAllCartItems()).thenReturn(flowOf(listOf(cartItemEntity)))

        val observer = mock(Observer::class.java) as Observer<MutableList<CartItem>>
        cartViewModel.cartItems.observeForever(observer)

        cartViewModel.addToCart(product)

        verify(cartDao).insertCartItem(cartItemEntity)
        verify(observer).onChanged(mutableListOf(cartItem))
    }
}