package com.dicoding.projecthair.ui.home.viewpager

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.projecthair.R
class ImageSliderAdapter(
    private val imageList: List<ImageData>,
    private val onPageChanged: (Int) -> Unit // Callback untuk memberitahu posisi halaman
) : RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder>() {

    private var currentPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageSliderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_slide, parent, false)
        return ImageSliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageSliderViewHolder, position: Int) {
        val imageData = imageList[position]
        Glide.with(holder.itemView.context)
            .load(imageData.imageUrl)
            .into(holder.imageView)

        // Update posisi saat gambar berubah
        holder.itemView.setOnClickListener {
            // Panggil onPageChanged untuk memperbarui posisi
            onPageChanged(position)

            // Dapatkan URL website dari imageData
            val websiteUrl = imageData.websiteUrl

            // Membuka website menggunakan Intent
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = imageList.size

    fun setCurrentPosition(position: Int) {
        currentPosition = position
        notifyDataSetChanged()
    }

    class ImageSliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.imageViewSlider)
    }
}
