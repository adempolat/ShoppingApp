package com.adempolat.eterationshoppingapp.ui

import android.app.AlertDialog
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
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


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
            if (cartItems.isEmpty()) {
                binding.recyclerView.visibility = View.GONE
                binding.emptyBasketMessage.visibility = View.VISIBLE
            } else {
                binding.recyclerView.visibility = View.VISIBLE
                binding.emptyBasketMessage.visibility = View.GONE
            }
        })

        cartViewModel.totalPrice.observe(viewLifecycleOwner, Observer { totalPrice ->
            binding.totalPriceTextView.text = String.format("%.2f$", totalPrice)
        })

        binding.completeButton.setOnClickListener {
            showConfirmationDialog()
        }
    }

    private fun showConfirmationDialog() {
        val totalPrice = cartViewModel.totalPrice.value ?: 0.0
        val message = "%.2f\$  değerindeki ürünleri satın almak istediğinizden emin misiniz?"

        AlertDialog.Builder(requireContext())
            .setTitle("Satın Alma Onayı")
            .setMessage(message.format(totalPrice))
            .setPositiveButton("Evet") { dialog, _ ->
                completePurchase(totalPrice)
                dialog.dismiss()
            }
            .setNegativeButton("Hayır") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun completePurchase(totalPrice: Double) {
        val purchaseTime = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())
        cartViewModel.addPurchaseHistory(totalPrice, purchaseTime)
        cartViewModel.clearCart()
        Snackbar.make(binding.root,
            getString(R.string.finish_shopping_message), Snackbar.LENGTH_LONG).show()
        // Profile ekranındaki toplam harcama tutarını güncelleme
        cartViewModel.updateTotalSpent(totalPrice)
    }

}