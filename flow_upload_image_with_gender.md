# Face Shape Prediction Android App

Aplikasi Android ini memungkinkan pengguna untuk memilih gambar dari galeri atau mengambil foto langsung dari kamera, kemudian mengirim gambar bersama dengan informasi gender untuk mendapatkan prediksi bentuk wajah menggunakan API.

## Fitur
- **Floating Action Button (FAB)**: Pengguna dapat memilih untuk mengupload gambar dari galeri atau menggunakan kamera.
- **Pemilihan Gender**: Pengguna dapat memilih jenis kelamin (Male/Female) sebelum mengirim gambar untuk prediksi.
- **Prediksi Bentuk Wajah**: Gambar dan gender yang dipilih dikirim ke API untuk mendapatkan prediksi bentuk wajah.

## Struktur Aplikasi
- **MainActivity**: Aktivitas utama yang menangani UI, termasuk pemilihan gambar dan pemilihan gender.
- **FAB**: Dua FAB yang digunakan untuk membuka galeri dan kamera.
- **RadioGroup**: Digunakan untuk memilih gender (Male/Female).
- **API**: Menggunakan `OkHttp` untuk mengirim gambar dan data gender ke backend API untuk prediksi.

## Prasyarat
Sebelum menjalankan aplikasi ini, pastikan Anda telah melakukan hal-hal berikut:
1. **Android Studio** terinstal di komputer Anda.
2. **Emulator** atau perangkat Android yang terhubung untuk menguji aplikasi.
3. **Backend API** yang mendukung endpoint untuk menerima gambar dan data gender serta memberikan prediksi bentuk wajah.

## Instalasi

1. **Clone Repository**:
   Clone repository ini ke komputer Anda:

   ```bash
   git clone https://github.com/username/face-shape-prediction-android.git

## Build Gradle


```
dependencies {
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'com.squareup.okhttp3:okhttp:4.9.1'
    implementation 'com.google.android.material:material:1.5.0'
    implementation 'com.github.bumptech.glide:glide:4.12.0'
    implementation 'androidx.activity:activity-ktx:1.3.1'
    implementation 'androidx.fragment:fragment-ktx:1.3.6'
}

```

## Layout xml


```
<?xml version="1.0" encoding="utf-8"?>
<androidx.coordinatorlayout.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <androidx.appcompat.widget.Toolbar
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:title="Face Shape Prediction" />

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        android:gravity="center">

        <!-- Pilihan Gender -->
        <RadioGroup
            android:id="@+id/radioGroupGender"
            android:orientation="horizontal"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content">

            <RadioButton
                android:id="@+id/radioMale"
                android:text="Male"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:checked="true" />

            <RadioButton
                android:id="@+id/radioFemale"
                android:text="Female"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content" />
        </RadioGroup>

        <!-- ImageView to display selected image -->
        <ImageView
            android:id="@+id/selectedImage"
            android:layout_width="300dp"
            android:layout_height="300dp"
            android:layout_marginTop="20dp"
            android:src="@drawable/ic_launcher_background" />

        <!-- Floating Action Button -->
        <androidx.constraintlayout.widget.ConstraintLayout
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="20dp">

            <androidx.appcompat.widget.AppCompatImageButton
                android:id="@+id/fabGallery"
                android:layout_width="56dp"
                android:layout_height="56dp"
                android:src="@drawable/ic_gallery"
                android:contentDescription="Select Image"
                android:layout_alignParentLeft="true" />

            <androidx.appcompat.widget.AppCompatImageButton
                android:id="@+id/fabCamera"
                android:layout_width="56dp"
                android:layout_height="56dp"
                android:src="@drawable/ic_camera"
                android:contentDescription="Open Camera"
                android:layout_marginStart="60dp" />
        </androidx.constraintlayout.widget.ConstraintLayout>

    </LinearLayout>

</androidx.coordinatorlayout.widget.CoordinatorLayout>

```

## Main Activity



```
package com.example.faceshapedetection

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var genderGroup: RadioGroup
    private lateinit var selectedImageView: ImageView

    private val getImageResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            Glide.with(this).load(uri).into(selectedImageView)
            sendImageToApi(uri)
        }
    }

    private val openCameraResult = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            selectedImageView.setImageBitmap(bitmap)
            sendImageToApi(bitmap)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        genderGroup = findViewById(R.id.radioGroupGender)
        selectedImageView = findViewById(R.id.selectedImage)

        findViewById<androidx.appcompat.widget.AppCompatImageButton>(R.id.fabGallery).setOnClickListener {
            // Open image gallery
            getImageResult.launch("image/*")
        }

        findViewById<androidx.appcompat.widget.AppCompatImageButton>(R.id.fabCamera).setOnClickListener {
            // Open camera
            openCameraResult.launch()
        }
    }

    private fun sendImageToApi(uri: android.net.Uri) {
        val file = File(uri.path!!)  // Get the file from URI
        val gender = getSelectedGender()
        val requestFile = RequestBody.create(MediaType.parse("image/*"), file)
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        val genderBody = gender.toRequestBody(MultipartBody.FORM)

        val request = Request.Builder()
            .url("http://localhost:8000/predict")  // Ganti dengan URL API Anda
            .post(MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addPart(body)
                .addFormDataPart("gender", genderBody)
                .build())
            .build()

        val client = OkHttpClient()
        client.newCall(request).execute().use { response ->
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                parseResponse(responseBody)
            } else {
                showToast("Upload failed")
            }
        }
    }

    private fun getSelectedGender(): String {
        val selectedRadio: RadioButton = findViewById(genderGroup.checkedRadioButtonId)
        return selectedRadio.text.toString().lowercase()
    }

    private fun parseResponse(responseBody: String?) {
        // Parse the response and display the result
        // This depends on your API response structure
        // Show Toast or Update UI accordingly
        showToast("Prediction success: $responseBody")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}

```

