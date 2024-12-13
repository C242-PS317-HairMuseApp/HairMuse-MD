package com.dicoding.projecthair.response

data class PredictionResponse(
    val uploadedImage: String,
    val face_shape: String?,
    val confidence: Double,
    val description: String?,
    val tips: List<String>,
    val recommendations: List<Recommendation>
)

data class Recommendation(
    val filename: String,
    val image: String
)
