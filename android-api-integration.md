# Integrasi API Face Shape Prediction di Android Studio

## Persiapan Awal
1. Tambahkan dependensi berikut di `build.gradle` (Module: app):
```gradle
dependencies {
    // Retrofit untuk HTTP Request
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    
    // OkHttp untuk network request
    implementation 'com.squareup.okhttp3:okhttp:4.10.0'
    
    // Glide untuk loading gambar
    implementation 'com.github.bumptech.glide:glide:4.12.0'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.12.0'
}
```

2. Tambahkan izin internet di `AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

## Struktur Kelas untuk API
1. Model Respons Prediksi:
```kotlin
data class PredictionResponse(
    val face_shape: String,
    val confidence: Double
)
```

2. Interface Retrofit untuk API:
```kotlin
interface FaceShapePredictionService {
    @Multipart
    @POST("predict")
    suspend fun predictFaceShape(
        @Part file: MultipartBody.Part
    ): PredictionResponse
}
```

3. Kelas Utility untuk Membuat Retrofit Instance:
```kotlin
object RetrofitClient {
    private const val BASE_URL = "http://your-api-domain.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val faceShapePredictionService: FaceShapePredictionService = 
        retrofit.create(FaceShapePredictionService::class.java)
}
```

## Contoh Implementasi di Activity
```kotlin
class FaceShapePredictionActivity : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_shape_prediction)

        // Button untuk memilih gambar
        btnPickImage.setOnClickListener {
            openImagePicker()
        }

        // Button untuk mengirim prediksi
        btnPredict.setOnClickListener {
            selectedImageUri?.let { uri ->
                uploadImageAndPredict(uri)
            }
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            selectedImageUri = data?.data
            // Tampilkan gambar yang dipilih
            Glide.with(this)
                .load(selectedImageUri)
                .into(imageViewPreview)
        }
    }

    private fun uploadImageAndPredict(imageUri: Uri) {
        // Konversi URI ke File
        val file = File(getRealPathFromURI(imageUri))
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        // Jalankan di Coroutine
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.faceShapePredictionService.predictFaceShape(body)
                
                // Tampilkan hasil prediksi
                tvFaceShape.text = "Bentuk Wajah: ${response.face_shape}"
                tvConfidence.text = "Kepercayaan: ${response.confidence}%"
            } catch (e: Exception) {
                // Tangani error
                Toast.makeText(this@FaceShapePredictionActivity, 
                    "Gagal melakukan prediksi: ${e.message}", 
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // Utility untuk mendapatkan path file dari URI
    private fun getRealPathFromURI(uri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        val path = cursor?.getString(columnIndex ?: 0)
        cursor?.close()
        return path ?: ""
    }
}
```

## Layout XML (activity_face_shape_prediction.xml)
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout 
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp">

    <ImageView
        android:id="@+id/imageViewPreview"
        android:layout_width="match_parent"
        android:layout_height="300dp"
        android:scaleType="centerCrop"/>

    <Button
        android:id="@+id/btnPickImage"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Pilih Gambar"/>

    <Button
        android:id="@+id/btnPredict"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Prediksi Bentuk Wajah"/>

    <TextView
        android:id="@+id/tvFaceShape"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textSize="18sp"
        android:layout_marginTop="16dp"/>

    <TextView
        android:id="@+id/tvConfidence"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textSize="18sp"/>
</LinearLayout>
```

## Catatan Penting
1. Ganti `BASE_URL` dengan URL API FastAPI Anda
2. Pastikan server API berjalan dan dapat diakses
3. Untuk deployment, pertimbangkan:
   - Handling izin akses storage
   - Validasi ukuran dan tipe file gambar
   - Implementasi loading state
   - Error handling yang komprehensif

## Konfigurasi Keamanan (jika perlu)
Untuk koneksi