package com.dicoding.projecthair.ui.Camera

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dicoding.projecthair.databinding.FragmentCameraBinding


class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    private var currentImageUri: Uri? = null
    private var currentImageBitmap: Bitmap? = null
    // Launcher untuk memilih gambar dari galeri
    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }
    // Launcher untuk mengambil gambar menggunakan kamera
    private val launcherCamera = registerForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            currentImageBitmap = bitmap
            showImage()
        } else {
            Log.d("Camera", "No image captured")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Klik tombol untuk membuka galeri
        binding.galleryButton.setOnClickListener { startGallery() }

        // Klik tombol untuk menganalisis gambar
        binding.CameraButton.setOnClickListener { openCamera() }
    }

    private fun startGallery() {
        // Tampilkan progress saat memuat galeri

        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showImage() {
        // Menampilkan gambar yang dipilih dari galeri
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun openCamera() {
        // Membuka kamera untuk mengambil gambar
        launcherCamera.launch(null)
    }

    private fun showToast(message: String) {
        // Menampilkan pesan toast
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
