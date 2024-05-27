package com.adempolat.eterationshoppingapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.adempolat.eterationshoppingapp.R
import com.adempolat.eterationshoppingapp.data.Product
import com.adempolat.eterationshoppingapp.databinding.FragmentProductDetailBinding
import com.adempolat.eterationshoppingapp.databinding.FragmentProfileBinding
import com.adempolat.eterationshoppingapp.utils.Constants
import com.adempolat.eterationshoppingapp.viewmodel.CartViewModel
import com.adempolat.eterationshoppingapp.viewmodel.ProductViewModel
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar


class ProductDetailFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!
    private val cartViewModel: CartViewModel by activityViewModels()
    private val productViewModel: ProductViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Toolbar'ı ayarlıyoruz


        val productName = arguments?.getString("productName")
        val productDescription = arguments?.getString("productDescription")
        val productPrice = arguments?.getString("productPrice")?.toDoubleOrNull()
        val productImageUrl = arguments?.getString("productImageUrl")
        val isFavorite = arguments?.getBoolean("isFavorite") ?: false

        binding.toolbar.title = productName
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }


        val product = Product(
            id = arguments?.getString("productId") ?: "",
            name = productName ?: "",
            description = productDescription ?: "",
            imageUrl = productImageUrl ?: "",
            price = productPrice ?: 0.0,
            createdAt = arguments?.getString("createdAt")?: "",
            isFavorite = isFavorite
        )

        binding.productName.text = productName
        binding.productDescription.text = productDescription
        binding.productPrice.text = String.format("%.2f$", product.price)

        Glide.with(binding.productImage.context)
            .load(Constants.FAKE_IMAGE)
            .placeholder(R.drawable.bg_button_add_to_cart) // placeholder resmi (isteğe bağlı)
            .into(binding.productImage)

        binding.favoriteButton.setImageResource(
            if (product.isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
        )

        binding.favoriteButton.setOnClickListener {
            productViewModel.toggleFavorite(product)
            //product.isFavorite = !product.isFavorite
            binding.favoriteButton.setImageResource(
                if (product.isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
            )
        }

        binding.buttonAddToCart.setOnClickListener {
            cartViewModel.addToCart(product)
            Snackbar.make(binding.root, getString(R.string.add_to_cart), Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
