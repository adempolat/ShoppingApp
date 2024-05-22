package com.adempolat.eterationshoppingapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import com.adempolat.eterationshoppingapp.databinding.ActivityMainBinding
import com.adempolat.eterationshoppingapp.viewmodel.CartViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private val cartViewModel: CartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.bottomNavigation

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        // bottomNavigationView.setupWithNavController(navController)

        cartViewModel.cartItemCount.observe(this, Observer { itemCount ->
            val badge = navView.getOrCreateBadge(R.id.navigation_basket)
            if (itemCount > 0) {
                badge.isVisible = true
                badge.number = itemCount
            } else {
                badge.isVisible = false
            }
        })

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_product_list -> {
                    navController.navigate(R.id.navigation_product_list)
                    true
                }
                R.id.navigation_basket -> {
                    navController.navigate(R.id.navigation_basket)
                    true
                }
                R.id.navigation_favorites -> {
                    navController.navigate(R.id.navigation_favorites)
                    true
                }
                R.id.navigation_profile -> {
                    navController.navigate(R.id.navigation_profile)
                    true
                }
                else -> false
            }
        }

    }
}