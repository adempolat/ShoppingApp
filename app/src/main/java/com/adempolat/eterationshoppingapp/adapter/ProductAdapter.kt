package com.example.shoppingapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.adempolat.eterationshoppingapp.R
import com.adempolat.eterationshoppingapp.data.Product
import com.adempolat.eterationshoppingapp.databinding.ItemProductBinding
import com.adempolat.eterationshoppingapp.viewmodel.CartViewModel
import com.bumptech.glide.Glide


class ProductAdapter(private var productList: List<Product>,
                     private val cartViewModel: CartViewModel
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var filteredProductList: List<Product> = productList

    class ProductViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = filteredProductList[position]
        holder.binding.textViewProductName.text = product.name
        holder.binding.textViewProductPrice.text = "${product.price}$"
        // Load image with your preferred library (e.g., Glide, Picasso)

        // Glide ile resim yükleme
        Glide.with(holder.binding.imageViewProduct.context)
            .load("https://loremflickr.com/cache/resized/65535_53510541439_3ea2865c74_z_640_480_nofilter.jpg")
            .placeholder(R.drawable.bg_button_add_to_cart) // placeholder resmi (isteğe bağlı)
            .into(holder.binding.imageViewProduct)

        holder.binding.root.setOnClickListener {
            val bundle = Bundle().apply {
                putString("productId", product.id)
                putString("productName", product.name)
                putString("productDescription", product.description)
                putString("productPrice", product.price.toString())
                putString("productImageUrl", product.imageUrl)
            }
            it.findNavController().navigate(R.id.action_navigation_product_list_to_productDetailFragment, bundle)
        }


        holder.binding.buttonAddToCart.setOnClickListener {
            cartViewModel.addToCart(product)
        }

    }

    override fun getItemCount(): Int {
        return filteredProductList.size
    }

    fun updateProducts(newProductList: List<Product>) {
        productList = newProductList
        filteredProductList = newProductList
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        filteredProductList = if (query.isEmpty()) {
            productList
        } else {
            productList.filter { it.name.contains(query, ignoreCase = true) }
        }
        notifyDataSetChanged()
    }
}
