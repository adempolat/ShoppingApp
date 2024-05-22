package com.adempolat.eterationshoppingapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adempolat.eterationshoppingapp.R
import com.adempolat.eterationshoppingapp.databinding.FragmentBasketBinding
import com.adempolat.eterationshoppingapp.databinding.FragmentFavoriteBinding
import com.adempolat.eterationshoppingapp.databinding.FragmentProductListBinding

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }


}