package com.dicoding.projecthair.response

import com.google.gson.annotations.SerializedName

data class HairstyleResponse(
    @SerializedName("face_shape") val faceShape: String,
    @SerializedName("images") val images: List<HairstyleImage>
)

data class HairstyleImage(
    @SerializedName("filename") val filename: String,
    @SerializedName("url") val url: String
)
