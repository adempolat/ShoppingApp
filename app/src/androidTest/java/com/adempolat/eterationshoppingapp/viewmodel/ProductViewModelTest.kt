package com.adempolat.eterationshoppingapp.viewmodel


import androidx.lifecycle.Observer
import com.adempolat.eterationshoppingapp.data.Product
import com.adempolat.eterationshoppingapp.repository.ProductRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.*

import kotlinx.coroutines.flow.flowOf
import okhttp3.internal.tls.OkHostnameVerifier.verify

@ExperimentalCoroutinesApi
class ProductViewModelTest {

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
    fun loadProducts_success() = testDispatcher.runBlockingTest {
        val products = listOf(
            Product("1", "Product 1", 10.0, "url1", "desc1", "2023-01-01"),
            Product("2", "Product 2", 20.0, "url2", "desc2", "2023-01-02")
        )

        `when`(repository.getProducts(1)).thenReturn(flowOf(products))

        val observer = mock(Observer::class.java) as Observer<List<Product>>
        productViewModel.products.observeForever(observer)

        productViewModel.loadProducts()

        verify(observer).onChanged(products)
        Assert.assertEquals(products, productViewModel.products.value)
    }
}
