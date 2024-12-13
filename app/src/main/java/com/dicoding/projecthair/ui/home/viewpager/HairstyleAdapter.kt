//package com.dicoding.projecthair.ui.home.viewpager
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.dicoding.projecthair.R
//import com.dicoding.projecthair.model.Hairstyle
//import com.dicoding.projecthair.ui.home.HairstyleRecommendationActivity
//import com.squareup.picasso.Picasso
//
//
//class HairstyleAdapter(private val recommendations: List<Hairstyle>) : RecyclerView.Adapter<HairstyleAdapter.HairstyleViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HairstyleViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.recomendasi_item, parent, false)
//        return HairstyleViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: HairstyleViewHolder, position: Int) {
//        val hairstyle = recommendations[position]
//        holder.bind(hairstyle)
//    }
//
//    override fun getItemCount(): Int = recommendations.size
//
//    inner class HairstyleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val imageView: ImageView = itemView.findViewById(R.id.imageViewHairstyle)
//        private val filenameTextView: TextView = itemView.findViewById(R.id.textViewHairstyleName)
//
//        fun bind(hairstyle: Hairstyle) {
//            Picasso.get()
//                .load(hairstyle.imageResId) // Update with the correct image source
//                .placeholder(R.drawable.ic_place_holder)
//                .error(R.drawable.ball)
//                .into(imageView)
//            filenameTextView.text = hairstyle.name
//        }
//    }
//}
