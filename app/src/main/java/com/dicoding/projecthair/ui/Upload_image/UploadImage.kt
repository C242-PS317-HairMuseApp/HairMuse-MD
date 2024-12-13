package com.dicoding.projecthair.ui.Upload_image

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.projecthair.R
import com.dicoding.projecthair.response.PredictionResponse
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException

class UploadImage : AppCompatActivity() {

    private var imageUri: Uri? = null
    private var selectedImageFile: File? = null
    private var gender: String = "male" // Default gender
    private lateinit var resultsContainer: LinearLayout
    private lateinit var recommendationsContainer: LinearLayout

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imageUri = it
            try {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
                findViewById<ImageView>(R.id.selectedImage).setImageBitmap(bitmap)

                val filePath = getPathFromUri(it)
                if (filePath != null) {
                    selectedImageFile = File(filePath)
                } else {
                    Toast.makeText(this, "Gagal mendapatkan file dari URI!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, "Gagal memuat gambar!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_image)

        // Initialize views
        resultsContainer = findViewById(R.id.resultsContainer)
        recommendationsContainer = findViewById(R.id.recommendationsContainer)
        val radioGroupGender = findViewById<RadioGroup>(R.id.radioGroupGender)
        val galleryButton = findViewById<Button>(R.id.galleryButton)
        val analyzeButton = findViewById<Button>(R.id.analyzeButton)

        // Initialize image views and text views for recommendations
        val hairstyleImageViews = listOf(
            findViewById<ImageView>(R.id.hairstyle_image_1),
            findViewById<ImageView>(R.id.hairstyle_image_2),
            findViewById<ImageView>(R.id.hairstyle_image_3)
        )
        val hairstyleFilenameTextViews = listOf(
            findViewById<TextView>(R.id.hairstyle_filename_1),
            findViewById<TextView>(R.id.hairstyle_filename_2),
            findViewById<TextView>(R.id.hairstyle_filename_3)
        )

        // Initially hide results
        resultsContainer.visibility = View.GONE

        // Set event listener untuk RadioGroup gender
        radioGroupGender.setOnCheckedChangeListener { _, checkedId ->
            gender = when (checkedId) {
                R.id.radioMale -> "male"
                R.id.radioFemale -> "female"
                else -> "female" // Default gender jika tidak valid
            }
        }

        galleryButton.setOnClickListener {
            openGallery()
        }

        analyzeButton.setOnClickListener {
            if (selectedImageFile == null) {
                Toast.makeText(this, "Pilih gambar terlebih dahulu!", Toast.LENGTH_SHORT).show()
            } else {
                analyzeImage(gender, hairstyleImageViews, hairstyleFilenameTextViews)
            }
        }
    }

    private fun openGallery() {
        pickImage.launch("image/*")
    }

    private fun analyzeImage(gender: String, hairstyleImageViews: List<ImageView>, hairstyleFilenameTextViews: List<TextView>) {
        val client = OkHttpClient.Builder()
            .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .build()

        if (selectedImageFile == null || !selectedImageFile!!.exists()) {
            Toast.makeText(this, "File gambar tidak valid!", Toast.LENGTH_SHORT).show()
            return
        }

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", selectedImageFile!!.name, selectedImageFile!!.asRequestBody("image/*".toMediaTypeOrNull()))
            .build()

        val baseUrl = "https://hairmuseimg2-325820985735.asia-southeast2.run.app/predict?gender=$gender"

        val request = Request.Builder()
            .url(baseUrl)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@UploadImage, "Gagal menghubungi server!", Toast.LENGTH_SHORT).show()
                    resultsContainer.visibility = View.GONE
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    Log.d("API Response", "Response Body: $responseBody")

                    val predictionResponse = try {
                        Gson().fromJson(responseBody, PredictionResponse::class.java)
                    } catch (e: Exception) {
                        Log.e("JSON Parsing", "Error parsing response", e)
                        null
                    }

                    if (predictionResponse != null) {
                        runOnUiThread {
                            // Make results visible
                            resultsContainer.visibility = View.VISIBLE

                            // Display predicted face shape
                            val faceShape = predictionResponse.face_shape?.takeIf { it.isNotBlank() }
                                ?: "Bentuk wajah tidak diketahui"
                            findViewById<TextView>(R.id.resultText).apply {
                                text = faceShape.toUpperCase()
                                setTypeface(typeface, android.graphics.Typeface.BOLD)
                            }

                            // Display description and tips
                            findViewById<TextView>(R.id.descriptionText).text = predictionResponse.description
                            findViewById<TextView>(R.id.tipsText).text = predictionResponse.tips.joinToString("\n")

                            // Display hairstyle recommendations
                            if (predictionResponse.recommendations.isNotEmpty()) {
                                predictionResponse.recommendations.forEachIndexed { index, recommendation ->
                                    if (index < hairstyleImageViews.size) {
                                        val adjustedImgFile = if (gender == "male") {
                                            recommendation.filename.replace("female", "male")
                                        } else {
                                            recommendation.filename
                                        }

                                        val adjustedImageUrl = if (gender == "male") {
                                            recommendation.image.replace("female", "male")
                                        } else {
                                            recommendation.image
                                        }

                                        // Load the image into the ImageView
                                        loadImageWithRetry(gender, adjustedImgFile, adjustedImageUrl, hairstyleImageViews[index])

                                        // Remove the extension from the filename before setting the TextView
                                        hairstyleFilenameTextViews[index].text = removeExtension(adjustedImgFile)
                                    }
                                }
                            } else {
                                Toast.makeText(this@UploadImage, "Tidak ada rekomendasi gaya rambut!", Toast.LENGTH_SHORT).show()
                                resultsContainer.visibility = View.GONE
                            }
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this@UploadImage, "Gagal memproses respons dari server!", Toast.LENGTH_SHORT).show()
                            resultsContainer.visibility = View.GONE
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@UploadImage, "Gagal mendapatkan respons dari server!", Toast.LENGTH_SHORT).show()
                        resultsContainer.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun loadImageWithRetry(
        gender: String,
        imgFile: String,
        imageUrl: String,
        imageView: ImageView,
        retryCount: Int = 3
    ) {
        val baseUrl = "https://hairmuseimg2-325820985735.asia-southeast2.run.app/"

        val fullUrl = baseUrl + imageUrl

        Log.d("LoadImageWithRetry", "Memuat gambar dari URL: $fullUrl")

        Picasso.get()
            .load(fullUrl)
            .placeholder(R.drawable.ic_place_holder)
            .error(R.drawable.ball)
            .into(imageView, object : com.squareup.picasso.Callback {
                override fun onSuccess() {
                    Log.d("Picasso", "Gambar berhasil dimuat: $fullUrl")
                }

                override fun onError(e: Exception?) {
                    if (retryCount > 0) {
                        Log.d("Picasso", "Retry memuat gambar: $fullUrl, sisa percobaan: $retryCount")
                        loadImageWithRetry(gender, imgFile, imageUrl, imageView, retryCount - 1)
                    } else {
                        Log.e("Picasso", "Gagal memuat gambar setelah retry: $fullUrl", e)
                        imageView.setImageResource(R.drawable.ball)
                    }
                }
            })
    }

    private fun removeExtension(filename: String): String {
        return filename.substringBeforeLast(".")
    }

    private fun getPathFromUri(uri: Uri): String? {
        return if (uri.scheme == "content") {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(uri, projection, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    it.getString(columnIndex)
                } else {
                    null
                }
            }
        } else if (uri.scheme == "file") {
            uri.path
        } else {
            null
        }
    }
}