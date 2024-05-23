package com.adempolat.eterationshoppingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adempolat.eterationshoppingapp.databinding.ItemPurchaseHistoryBinding

class PurchaseHistoryAdapter(
    private val purchaseHistory: List<Pair<Double, String>>
) : RecyclerView.Adapter<PurchaseHistoryAdapter.PurchaseHistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchaseHistoryViewHolder {
        val binding = ItemPurchaseHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PurchaseHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PurchaseHistoryViewHolder, position: Int) {
        val (amount, time) = purchaseHistory[position]
        holder.bind(amount, time)
    }

    override fun getItemCount(): Int = purchaseHistory.size

    inner class PurchaseHistoryViewHolder(private val binding: ItemPurchaseHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(amount: Double, time: String) {
            binding.amountTextView.text = String.format("%.2f TL", amount)
            binding.timeTextView.text = time
        }
    }
}