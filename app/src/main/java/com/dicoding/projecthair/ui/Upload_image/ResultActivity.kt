package com.dicoding.projecthair.ui.Upload_image

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.projecthair.R
import com.google.gson.Gson

class ResultActivity : AppCompatActivity() {

    private lateinit var resultTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var tipsTextView: TextView
    private lateinit var hairstyleImageViews: Array<ImageView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // Initialize views
        resultTextView = findViewById(R.id.result_text_view)
        descriptionTextView = findViewById(R.id.description_text_view)
        tipsTextView = findViewById(R.id.tips_text_view)
        hairstyleImageViews = arrayOf(
            findViewById(R.id.hairstyle_image_1),
            findViewById(R.id.hairstyle_image_2),
            findViewById(R.id.hairstyle_image_3)
        )

        // Get prediction data from intent
        val predictionJson = intent.getStringExtra("prediction_data")
        if (predictionJson != null) {
            val predictionResponse = Gson().fromJson(predictionJson, PredictionResponse::class.java)
            displayPredictionResults(predictionResponse)
        } else {
            resultTextView.text = "No data received!"
        }
    }

    private fun displayPredictionResults(prediction: PredictionResponse) {
        // Update UI with prediction results
        resultTextView.text = "Face Shape: ${prediction.faceShape}"
        descriptionTextView.text = prediction.description
        tipsTextView.text = prediction.tips.joinToString("\n")

        // Load hairstyle images
        prediction.recommendations.forEachIndexed { index, recommendation ->
            if (index < hairstyleImageViews.size) {
                Glide.with(this)
                    .load(recommendation.image) // Load image URL using Glide
                    .into(hairstyleImageViews[index])
            }
        }
    }

    // Data class for parsing the prediction response
    data class PredictionResponse(
        val faceShape: String,
        val description: String,
        val tips: List<String>,
        val recommendations: List<Recommendation>
    )

    data class Recommendation(
        val image: String
    )
}
