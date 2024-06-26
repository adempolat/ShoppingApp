package com.adempolat.eterationshoppingapp.ui

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.adempolat.eterationshoppingapp.R
import com.adempolat.eterationshoppingapp.adapter.PurchaseHistoryAdapter
import com.adempolat.eterationshoppingapp.databinding.FragmentProfileBinding
import com.adempolat.eterationshoppingapp.viewmodel.CartViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val cartViewModel: CartViewModel by activityViewModels()
    private val binding get() = _binding!!
    private val PICK_IMAGE = 1
    private val PREFS_NAME = "profile_prefs"
    private val PROFILE_IMAGE_URI = "profile_image_uri"

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

        // Profil resmini kaydetme işlemi
        binding.saveImageButton.setOnClickListener {
            saveProfileImage()
        }

        // Çıkış yap butonu için click listener
        binding.logoutButton.setOnClickListener {
            activity?.finish()
        }

        // Toplam harcama tutarını güncelle
        cartViewModel.totalSpent.observe(viewLifecycleOwner) { totalSpent ->
            binding.totalSpentTextView.text = String.format(getString(R.string.total_amount_spent), totalSpent)
        }

        // Harcama geçmişini güncelle
        cartViewModel.purchaseHistory.observe(viewLifecycleOwner) { history ->
            binding.purchaseHistoryRecyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = PurchaseHistoryAdapter(history)
            }
        }

        // Harcama geçmişini gösterme/gizleme
        binding.switchPurchaseHistory.setOnCheckedChangeListener() { _, isChecked ->
            binding.purchaseHistoryRecyclerView.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
    }

    private fun loadUserProfile() {
        binding.usernameTextView.text = "Adem Polat"

        // SharedPreferences'tan URI'yi yükle
        val sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val imageUriString = sharedPreferences.getString(PROFILE_IMAGE_URI, null)

        if (imageUriString != null) {
            val imageUri = Uri.parse(imageUriString)
            Glide.with(this).load(imageUri).into(binding.profileImageView)
        } else {
            // Profil resmi yükleme (placeholder olarak)
            Glide.with(this)
                .load(R.drawable.ic_profile_placeholder)
                .transform(CircleCrop())
                .into(binding.profileImageView)
        }
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

    private fun saveProfileImage() {
        val drawable = binding.profileImageView.drawable as BitmapDrawable
        val bitmap = drawable.bitmap
        CoroutineScope(Dispatchers.IO).launch {
            val savedUri = saveImageToGallery(requireContext(), bitmap)
            CoroutineScope(Dispatchers.Main).launch {
                if (savedUri != null) {
                    saveImageUriToPrefs(savedUri)
                    Toast.makeText(requireContext(),
                        getString(R.string.profile_picture_saved, savedUri), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(),
                        getString(R.string.profile_picture_not_saved), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveImageToGallery(context: Context, bitmap: Bitmap): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "profile_image_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/Camera")
        }
        val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        uri?.let {
            context.contentResolver.openOutputStream(it)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                outputStream.flush()
            }
        }
        return uri
    }

    private fun saveImageUriToPrefs(uri: Uri) {
        val sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(PROFILE_IMAGE_URI, uri.toString()).apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
