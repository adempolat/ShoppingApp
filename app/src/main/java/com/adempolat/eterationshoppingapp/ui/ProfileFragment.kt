package com.adempolat.eterationshoppingapp.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.adempolat.eterationshoppingapp.R
import com.adempolat.eterationshoppingapp.adapter.PurchaseHistoryAdapter
import com.adempolat.eterationshoppingapp.databinding.FragmentBasketBinding
import com.adempolat.eterationshoppingapp.databinding.FragmentProductListBinding
import com.adempolat.eterationshoppingapp.databinding.FragmentProfileBinding
import com.adempolat.eterationshoppingapp.viewmodel.CartViewModel
import com.bumptech.glide.Glide

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val cartViewModel: CartViewModel by activityViewModels()
    private val binding get() = _binding!!
    private val PICK_IMAGE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Kullanıcı bilgilerini yükleme ve görüntüleme
        loadUserProfile()

        // Profil resmi seçme işlemi
        binding.profileImageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE)
        }

        // Çıkış yap butonu için click listener
        binding.logoutButton.setOnClickListener {
            activity?.finish()
        }

        // Toplam harcama tutarını güncelle
        cartViewModel.totalSpent.observe(viewLifecycleOwner) { totalSpent ->
            binding.totalSpentTextView.text = String.format("Toplam Harcanan Tutar %.2f TL", totalSpent)
        }

        // Harcama geçmişini güncelle
        cartViewModel.purchaseHistory.observe(viewLifecycleOwner) { history ->
            binding.purchaseHistoryRecyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = PurchaseHistoryAdapter(history)
            }
        }
    }

    private fun loadUserProfile() {
        binding.usernameTextView.text = "Adem Polat"
        // Profil resmi yükleme (placeholder olarak)
        Glide.with(this).load(R.drawable.ic_profile_placeholder).into(binding.profileImageView)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            val selectedImage: Uri? = data?.data
            if (selectedImage != null) {
                Glide.with(this).load(selectedImage).into(binding.profileImageView)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}