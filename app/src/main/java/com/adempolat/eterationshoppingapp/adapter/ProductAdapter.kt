package com.example.shoppingapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.adempolat.eterationshoppingapp.R
import com.adempolat.eterationshoppingapp.data.Product
import com.adempolat.eterationshoppingapp.databinding.ItemProductBinding
import com.adempolat.eterationshoppingapp.utils.Constants
import com.adempolat.eterationshoppingapp.viewmodel.CartViewModel
import com.adempolat.eterationshoppingapp.viewmodel.ProductViewModel
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar


class ProductAdapter(private val productViewModel: ProductViewModel,
                     private var productList: List<Product>,
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

        // Glide ile resim yükleme
        Glide.with(holder.binding.imageViewProduct.context)
            .load(Constants.FAKE_IMAGE)
            .placeholder(R.drawable.bg_button_add_to_cart) // placeholder resmi (isteğe bağlı)
            .into(holder.binding.imageViewProduct)

        holder.binding.favoriteButton.setImageResource(
            if (product.isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
        )

        holder.binding.favoriteButton.setOnClickListener {
            productViewModel.toggleFavorite(product)
            holder.binding.favoriteButton.setImageResource(
                if (product.isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
            )
        }


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

        val product2 = Product(
            id = product.id ?: "",
            name = product.name ?: "",
            description = product.description ?: "",
            imageUrl = product.imageUrl ?: "",
            price = product.price ?: 0.0,
            createdAt = product.createdAt?: ""
        )



        holder.binding.buttonAddToCart.setOnClickListener {
            cartViewModel.addToCart(product2)
            Snackbar.make(holder.binding.root, "Ürün sepete eklendi", Snackbar.LENGTH_SHORT).show()
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

class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}
