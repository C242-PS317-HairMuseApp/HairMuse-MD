package com.dicoding.projecthair

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dicoding.projecthair.databinding.ActivityMainBinding

import com.dicoding.projecthair.ui.Upload_image.ResultActivity
import com.dicoding.projecthair.ui.Upload_image.UploadImage
import com.dicoding.projecthair.ui.home.viewpager.ImageSliderAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    private var isFabVisible = false // Status FAB

    // Launcher untuk memilih gambar dari galeri
    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            // Tampilkan URI gambar di log atau lakukan tindakan lain
            showToast("Gambar dari galeri berhasil dipilih: $uri")
        } else {
            showToast("Tidak ada gambar yang dipilih")
        }
    }

    // Launcher untuk mengambil gambar menggunakan kamera
    private val launcherCamera = registerForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            // Tampilkan gambar atau lakukan tindakan lain
            showToast("Gambar dari kamera berhasil diambil")
        } else {
            showToast("Tidak ada gambar yang diambil")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView = binding.navView
        val fabCamera = binding.fabCamera
        val fabGallery = binding.fabGallery


        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_camera, R.id.navigation_profil
            )
        )


        // Set initial FAB visibility
        fabCamera.visibility = View.GONE
        fabGallery.visibility = View.GONE

        // Observer untuk item navigation selection
        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_camera -> {
                    // Tampilkan FAB saat tab kamera dipilih
                    if (!isFabVisible) {
                        fabCamera.visibility = View.VISIBLE
                        fabGallery.visibility = View.VISIBLE
                        isFabVisible = true
                    }
                    true
                }
                else -> {
                    // Sembunyikan FAB saat tab lain dipilih
                    if (isFabVisible) {
                        fabCamera.visibility = View.GONE
                        fabGallery.visibility = View.GONE
                        isFabVisible = false
                    }
                    true
                }
            }
        }

        // Listener untuk klik FAB
        fabCamera.setOnClickListener {
            openCamera()
        }

        fabGallery.setOnClickListener {
            val intent = Intent(this, UploadImage::class.java)
            startActivity(intent)
        }
    }

    private fun openCamera() {
        // Memulai launcher untuk mengambil gambar dengan kamera
        launcherCamera.launch(null)
    }

    private fun openGallery() {
        // Memulai launcher untuk memilih gambar dari galeri
        val request = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        launcherGallery.launch(request)
    }

    private fun showToast(message: String) {
        // Menampilkan pesan toast
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
