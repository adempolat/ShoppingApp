package com.adempolat.eterationshoppingapp.ui

import android.os.Bundle
import com.adempolat.eterationshoppingapp.ui.ProductDetailFragment
import org.mockito.ArgumentMatchers.matches
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.adempolat.eterationshoppingapp.R
import com.adempolat.eterationshoppingapp.data.Product
import com.adempolat.eterationshoppingapp.viewmodel.CartViewModel
import com.adempolat.eterationshoppingapp.viewmodel.ProductViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class ProductDetailFragmentTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var cartViewModel: CartViewModel

    @Mock
    private lateinit var productViewModel: ProductViewModel

    private val testProduct = MutableLiveData<Product>()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Mockito.`when`(productViewModel.products).thenReturn(testProduct)
        val bundle = Bundle().apply {
            putString("productId", "1")
            putString("productName", "Test Product")
            putString("productDescription", "Description")
            putString("productPrice", "10.0")
            putString("productImageUrl", "imageUrl")
            putBoolean("isFavorite", false)
        }
        launchFragmentInContainer<ProductDetailFragment>(bundle)
    }

    @Test
    fun testProductDetailsDisplayed() {
        val product = Product("1", "Test Product", "Description", "imageUrl", 10.0, "2024-05-27", false)
        testProduct.postValue(product)

        onView(withId(R.id.productName))
            .check(matches(withText("Test Product")))

        onView(withId(R.id.productPrice))
            .check(matches(withText("10.0$")))
    }
}
