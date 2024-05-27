package com.adempolat.eterationshoppingapp.ui

import com.adempolat.eterationshoppingapp.ui.BasketFragment
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.adempolat.eterationshoppingapp.R
import com.adempolat.eterationshoppingapp.data.CartItem
import com.adempolat.eterationshoppingapp.data.Product
import com.adempolat.eterationshoppingapp.viewmodel.CartViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.regex.Pattern.matches

@RunWith(AndroidJUnit4::class)
class BasketFragmentTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var cartViewModel: CartViewModel

    private val testCartItems = MutableLiveData<List<CartItem>>()
    private val testTotalPrice = MutableLiveData<Double>()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Mockito.`when`(cartViewModel.cartItems).thenReturn(testCartItems)
        Mockito.`when`(cartViewModel.totalPrice).thenReturn(testTotalPrice)
        launchFragmentInContainer<BasketFragment>()
    }

    @Test
    fun testCartItemsDisplayedInRecyclerView() {
        val product = Product("1", "Test Product", "Description", "imageUrl", 10.0, "2024-05-27", false)
        val cartItem = CartItem(product, 1)
        testCartItems.postValue(listOf(cartItem))

        onView(withId(R.id.recyclerView))
            .check(matches(isDisplayed()))
            .check(matches(hasDescendant(withText("Test Product"))))
    }

    @Test
    fun testTotalPriceDisplayed() {
        testTotalPrice.postValue(100.0)

        onView(withId(R.id.totalPriceTextView))
            .check(matches(withText("100.0$")))
    }
}
