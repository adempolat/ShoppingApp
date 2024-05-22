package com.adempolat.eterationshoppingapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.adempolat.eterationshoppingapp.data.Product
import com.adempolat.eterationshoppingapp.databinding.FragmentProductDetailBinding
import com.adempolat.eterationshoppingapp.databinding.FragmentProfileBinding
import com.adempolat.eterationshoppingapp.viewmodel.CartViewModel
import com.bumptech.glide.Glide


class ProductDetailFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!
    private val cartViewModel: CartViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val productName = arguments?.getString("productName")
        val productDescription = arguments?.getString("productDescription")
        val productPrice = arguments?.getString("productPrice")?.toDoubleOrNull()
        val productImageUrl = arguments?.getString("productImageUrl")

        val product = Product(
            id = arguments?.getString("productId") ?: "",
            createdAt = arguments?.getString("createdAt") ?:"",
            name = productName ?: "",
            description = productDescription ?: "",
            imageUrl = productImageUrl ?: "",
            price = productPrice ?: 0.0,
        )

        binding.productName.text = productName
        binding.productDescription.text = productDescription
        binding.productPrice.text = String.format("%.2f$", product.price)
        Glide.with(binding.productImage).load(productImageUrl)

        binding.buttonAddToCart.setOnClickListener {
            cartViewModel.addToCart(product)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
