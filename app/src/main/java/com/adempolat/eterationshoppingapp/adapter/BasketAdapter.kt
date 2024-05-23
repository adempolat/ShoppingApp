package com.adempolat.eterationshoppingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adempolat.eterationshoppingapp.R
import com.adempolat.eterationshoppingapp.data.CartItem
import com.adempolat.eterationshoppingapp.databinding.ItemBasketBinding
import com.adempolat.eterationshoppingapp.utils.Constants
import com.adempolat.eterationshoppingapp.viewmodel.CartViewModel
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar

class BasketAdapter(private val cartViewModel: CartViewModel) : RecyclerView.Adapter<BasketAdapter.BasketViewHolder>() {

    private var cartItems: List<CartItem> = listOf()

    class BasketViewHolder(val binding: ItemBasketBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketViewHolder {
        val binding = ItemBasketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BasketViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BasketViewHolder, position: Int) {
        val cartItem = cartItems[position]
        holder.binding.productName.text = cartItem.product.name
        holder.binding.productPrice.text = "${cartItem.product.price}$"
        holder.binding.quantity.text = cartItem.quantity.toString()
        Glide.with(holder.binding.productImageView.context)
            .load(Constants.FAKE_IMAGE)
            .placeholder(R.drawable.bg_button_add_to_cart) // placeholder resmi (isteğe bağlı)
            .into(holder.binding.productImageView)

        holder.binding.increaseButton.setOnClickListener {
            cartViewModel.increaseQuantity(cartItem)

        }

        holder.binding.decreaseButton.setOnClickListener {
            if (cartItem.quantity > 1) {
                cartViewModel.decreaseQuantity(cartItem)
            } else {
                cartViewModel.removeFromCart(cartItem.product)
                Snackbar.make(holder.binding.root, "${cartItem.product.name} sepetten çıkartıldı.", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    fun submitList(newCartItems: List<CartItem>) {
        cartItems = newCartItems
        notifyDataSetChanged()
    }
}

