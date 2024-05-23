package com.adempolat.eterationshoppingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adempolat.eterationshoppingapp.databinding.ItemFilterBinding

class FilterAdapter(
    private val items: List<String>,
    private val selectedItems: MutableSet<String>
) : RecyclerView.Adapter<FilterAdapter.FilterViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val binding = ItemFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class FilterViewHolder(private val binding: ItemFilterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            binding.filterCheckBox.text = item
            binding.filterCheckBox.isChecked = selectedItems.contains(item)
            binding.filterCheckBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedItems.add(item)
                } else {
                    selectedItems.remove(item)
                }
            }
        }
    }
}
