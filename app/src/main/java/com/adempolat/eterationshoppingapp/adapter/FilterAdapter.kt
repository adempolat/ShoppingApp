package com.adempolat.eterationshoppingapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adempolat.eterationshoppingapp.databinding.ItemFilterBinding

class FilterAdapter(
    private val items: List<String>,
    private val selectedItems: MutableSet<String>
) : RecyclerView.Adapter<FilterAdapter.FilterViewHolder>() {

    inner class FilterViewHolder(val binding: ItemFilterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val binding = ItemFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        val item = items[position]
        holder.binding.filterItemName.text = item
        holder.binding.filterItemCheckbox.isChecked = selectedItems.contains(item)

        holder.binding.filterItemCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedItems.add(item)
            } else {
                selectedItems.remove(item)
            }
        }

        holder.itemView.setOnClickListener {
            holder.binding.filterItemCheckbox.isChecked = !holder.binding.filterItemCheckbox.isChecked
        }
    }

    override fun getItemCount(): Int = items.size

    fun getSelectedItems(): List<String> {
        return selectedItems.toList()
    }
}
