package com.adempolat.eterationshoppingapp.viewmodel

import com.adempolat.eterationshoppingapp.data.Product
import com.adempolat.eterationshoppingapp.repository.ProductRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.internal.tls.OkHostnameVerifier.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ProductDetailViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: ProductRepository

    private lateinit var productViewModel: ProductViewModel

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        productViewModel = ProductViewModel(repository)
    }

    @Test
    fun toggleFavorite_success() = testDispatcher.runBlockingTest {
        val product = Product("1", "Product 1", 10.0, "url1", "desc1", "2023-01-01", isFavorite = false)

        productViewModel.toggleFavorite(product)

        val updatedProduct = product.copy(isFavorite = true)
        verify(repository).updateProduct(updatedProduct)
        Assert.assertTrue(updatedProduct.isFavorite)
    }
}