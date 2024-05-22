package com.adempolat.eterationshoppingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adempolat.eterationshoppingapp.data.CartItem
import com.adempolat.eterationshoppingapp.databinding.ItemBasketBinding
import com.adempolat.eterationshoppingapp.viewmodel.CartViewModel

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

        holder.binding.increaseButton.setOnClickListener {
            cartViewModel.increaseQuantity(cartItem)

        }

        holder.binding.decreaseButton.setOnClickListener {
            if (cartItem.quantity > 1) {
                cartViewModel.decreaseQuantity(cartItem)
            } else {
                cartViewModel.removeFromCart(cartItem.product)
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

