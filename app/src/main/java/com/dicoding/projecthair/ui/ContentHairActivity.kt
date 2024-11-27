package com.dicoding.projecthair.ui

import android.os.Bundle
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.projecthair.R

class ContentHairActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_content_hair)

        // Mengatur WindowInsets untuk elemen root
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inisialisasi elemen-elemen di XML
        val progressBar = findViewById<ProgressBar>(R.id.progressBarLoading)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewActiveEvents)

        // Jika menggunakan RecyclerView, tambahkan adapter dan layout manager
        recyclerView.apply {
            // Atur layout manager (contoh LinearLayoutManager)
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@ContentHairActivity)
            // Atur adapter (contoh Adapter kosong)
            adapter = MyAdapter()
        }
    }

    // Contoh Adapter RecyclerView
    class MyAdapter : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
        class ViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view)
        override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ViewHolder {
            val view = android.view.LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_list_item_1, parent, false)
            return ViewHolder(view)
        }
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // Bind data (kosong sementara)
        }
        override fun getItemCount() = 0 // Jumlah item data
    }
}
