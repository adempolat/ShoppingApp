package com.adempolat.eterationshoppingapp.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
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
import java.util.regex.Pattern.matches

@RunWith(AndroidJUnit4::class)
class ProductListFragmentTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var productViewModel: ProductViewModel

    @Mock
    private lateinit var cartViewModel: CartViewModel

    private val testProducts = MutableLiveData<List<Product>>()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Mockito.`when`(productViewModel.filteredProducts).thenReturn(testProducts)
        launchFragmentInContainer<ProductListFragment>()
    }

    @Test
    fun testProductsDisplayedInRecyclerView() {
        val product = Product("1", "Test Product", "Description", "imageUrl", 10.0, "2024-05-27", false)
        testProducts.postValue(listOf(product))

        onView(withId(R.id.recyclerView))
            .check(matches(isDisplayed()))
            .check(matches(hasDescendant(withText("Test Product"))))
    }
}
