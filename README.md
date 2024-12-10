# Creating a README.md file for the user's Android project

readme_content = """
# Face Shape Detection App

Aplikasi Android untuk mendeteksi bentuk wajah pengguna dan memberikan rekomendasi gaya rambut sesuai dengan hasil prediksi. Aplikasi ini menggunakan **FastAPI** sebagai backend untuk memproses gambar dan menghasilkan prediksi.

---

## **Fitur**
- **Upload Foto**: Memungkinkan pengguna untuk memilih foto dari galeri atau kamera.
- **Prediksi Bentuk Wajah**: Mengirim gambar ke endpoint API dan mendapatkan hasil prediksi.
- **Deskripsi & Tips**: Menampilkan deskripsi dan tips berdasarkan bentuk wajah yang terdeteksi.
- **Rekomendasi Gaya Rambut**: Menampilkan rekomendasi gaya rambut berdasarkan hasil prediksi.

---

## **Persyaratan**
- Android Studio **Bumblebee** atau versi lebih baru.
- Min SDK: **21** (Android 5.0 Lollipop).
- FastAPI backend harus berjalan dan dapat diakses melalui endpoint API.

---

## **Instalasi**
1. Clone repository ini:
   ```bash
   git clone https://github.com/HairMuseApp/HairMuse-API.git


```bash
// Import perlu disesuaikan dengan framework yang Anda gunakan
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.apparel.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import okhttp3.*
import java.io.File
import java.io.IOException

class FaceShapeActivity : AppCompatActivity() {

    private lateinit var uploadButton: FloatingActionButton
    private lateinit var predictButton: Button
    private lateinit var imageView: ImageView
    private lateinit var resultTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var tipsTextView: TextView
    private lateinit var hairstyleImageViews: Array<ImageView>

    private var selectedImageFile: File? = null
    private var gender: String = "female" // Default gender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_shape)

        // Inisialisasi view-view
        uploadButton = findViewById(R.id.upload_button)
        predictButton = findViewById(R.id.predict_button)
        imageView = findViewById(R.id.image_view)
        resultTextView = findViewById(R.id.result_text_view)
        descriptionTextView = findViewById(R.id.description_text_view)
        tipsTextView = findViewById(R.id.tips_text_view)
        hairstyleImageViews = arrayOf(
            findViewById(R.id.hairstyle_image_1),
            findViewById(R.id.hairstyle_image_2),
            findViewById(R.id.hairstyle_image_3)
        )

        // Pengaturan button upload
        uploadButton.setOnClickListener {
            // Logika untuk memilih gambar dari gallery/kamera
            // ...
            selectedImageFile = /* File yang dipilih */
            updateUploadButton()
        }

        // Pengaturan button prediksi
        predictButton.setOnClickListener {
            predictFaceShape()
        }
    }

    private fun updateUploadButton() {
        // Ubah tampilan button upload sesuai dengan gambar yang dipilih
        if (selectedImageFile != null) {
            uploadButton.setImageResource(R.drawable.ic_check)
            predictButton.isEnabled = true
        } else {
            uploadButton.setImageResource(R.drawable.ic_upload)
            predictButton.isEnabled = false
        }
    }

    private fun predictFaceShape() {
        // Kirim file gambar ke API FastAPI
        val client = OkHttpClient()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", selectedImageFile?.name, RequestBody.create(MediaType.parse("image/*"), selectedImageFile))
            .addFormDataPart("gender", gender)
            .build()

        val request = Request.Builder()
            .url("YOUR_FASTAPI_ENDPOINT/predict")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Tangani kegagalan permintaan
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val predictionResponse = Gson().fromJson(responseBody, PredictionResponse::class.java)

                    runOnUiThread {
                        // Tampilkan hasil prediksi
                        imageView.setImageURI(predictionResponse.uploadedImage)
                        resultTextView.text = predictionResponse.faceShape
                        descriptionTextView.text = predictionResponse.description
                        tipsTextView.text = predictionResponse.tips.joinToString("\n")

                        // Tampilkan rekomendasi gaya rambut
                        predictionResponse.recommendations.forEachIndexed { index, recommendation ->
                            hairstyleImageViews[index].setImageURI(recommendation.image)
                        }
                    }
                } else {
                    // Tangani kegagalan respons
                }
            }
        })
    }
}

data class PredictionResponse(
    val uploadedImage: String,
    val faceShape: String,
    val confidence: Double,
    val description: String,
    val tips: List<String>,
    val recommendations: List<Recommendation>
)

data class Recommendation(
    val filename: String,
    val image: String
)
