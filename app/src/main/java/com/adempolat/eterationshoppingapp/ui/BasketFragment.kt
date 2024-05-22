package com.adempolat.eterationshoppingapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.adempolat.eterationshoppingapp.R
import com.adempolat.eterationshoppingapp.adapter.BasketAdapter
import com.adempolat.eterationshoppingapp.data.CartItem
import com.adempolat.eterationshoppingapp.databinding.FragmentBasketBinding
import com.adempolat.eterationshoppingapp.databinding.FragmentProductListBinding
import com.adempolat.eterationshoppingapp.viewmodel.CartViewModel


class BasketFragment : Fragment() {

    private lateinit var binding: FragmentBasketBinding
    private val cartViewModel: CartViewModel by activityViewModels()
    private lateinit var basketAdapter: BasketAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBasketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        basketAdapter = BasketAdapter(cartViewModel)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = basketAdapter
        }

        cartViewModel.cartItems.observe(viewLifecycleOwner, Observer { cartItems ->
            basketAdapter.submitList(cartItems)
        })

        cartViewModel.totalPrice.observe(viewLifecycleOwner, Observer { totalPrice ->
            binding.totalPriceTextView.text = String.format("%.2f$", totalPrice)
        })

        binding.completeButton.setOnClickListener {
            // Sipariş tamamlanma işlemi buraya eklenebilir.
        }
    }

}